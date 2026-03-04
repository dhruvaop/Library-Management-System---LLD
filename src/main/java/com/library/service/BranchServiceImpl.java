package com.library.service;

import com.library.model.Book;
import com.library.model.Branch;
import com.library.repository.BookRepository;
import com.library.repository.BranchRepository;
import com.library.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class BranchServiceImpl implements BranchService {

    private static final Logger logger = LoggerFactory.getLogger(BranchServiceImpl.class);

    private final BranchRepository branchRepository;
    private final BookRepository bookRepository;

    public BranchServiceImpl(BranchRepository branchRepository, BookRepository bookRepository) {
        this.branchRepository = branchRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Branch createBranch(String name, String address) {
        String branchId = IdGenerator.generateBranchId();
        Branch branch = new Branch(branchId, name, address);
        branchRepository.save(branch);
        logger.info("Branch created -> ID: {}, Name: {}", branchId, name);
        return branch;
    }

    @Override
    public Optional<Branch> findById(String branchId) {
        return branchRepository.findById(branchId);
    }

    @Override
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    @Override
    public void transferBook(String isbn, String fromBranchId, String toBranchId) {
        if (!branchRepository.existsById(fromBranchId)) {
            throw new IllegalArgumentException("Source branch not found: " + fromBranchId);
        }
        if (!branchRepository.existsById(toBranchId)) {
            throw new IllegalArgumentException("Destination branch not found: " + toBranchId);
        }
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(() ->
                new IllegalArgumentException("Book not found: " + isbn));
        if (!book.isAvailable()) {
            throw new IllegalStateException("Cannot transfer a book that is currently borrowed.");
        }
        logger.info("Book transferred -> ISBN: {}, From: {}, To: {}", isbn, fromBranchId, toBranchId);
    }
}

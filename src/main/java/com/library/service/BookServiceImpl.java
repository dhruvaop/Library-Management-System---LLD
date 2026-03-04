package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.repository.BranchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final BranchRepository branchRepository;

    public BookServiceImpl(BookRepository bookRepository, BranchRepository branchRepository) {
        this.bookRepository = bookRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public void addBook(Book book, String branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new IllegalArgumentException("Branch not found: " + branchId);
        }
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalStateException("A book with this ISBN already exists: " + book.getIsbn());
        }
        bookRepository.save(book);
        logger.info("Book added -> ISBN: {}, Title: {}, Branch: {}", book.getIsbn(), book.getTitle(), branchId);
    }

    @Override
    public void removeBook(String isbn, String branchId) {
        bookRepository.findByIsbn(isbn).orElseThrow(() ->
                new IllegalArgumentException("Book not found: " + isbn));
        bookRepository.delete(isbn);
        logger.info("Book removed -> ISBN: {}", isbn);
    }

    @Override
    public void updateBook(Book book) {
        bookRepository.findByIsbn(book.getIsbn()).orElseThrow(() ->
                new IllegalArgumentException("Book not found: " + book.getIsbn()));
        bookRepository.save(book);
        logger.info("Book updated -> ISBN: {}", book.getIsbn());
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public List<Book> searchByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}

package com.library.service;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.LendingRecord;
import com.library.model.LendingStatus;
import com.library.model.Patron;
import com.library.notification.BookReturnedEvent;
import com.library.notification.NotificationService;
import com.library.repository.BookRepository;
import com.library.repository.LendingRepository;
import com.library.repository.PatronRepository;
import com.library.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class LendingServiceImpl implements LendingService {

    private static final Logger logger = LoggerFactory.getLogger(LendingServiceImpl.class);

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final LendingRepository lendingRepository;
    private final NotificationService notificationService;

    public LendingServiceImpl(BookRepository bookRepository, PatronRepository patronRepository,
                               LendingRepository lendingRepository, NotificationService notificationService) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.lendingRepository = lendingRepository;
        this.notificationService = notificationService;
    }

    @Override
    public LendingRecord checkoutBook(String isbn, String patronId, String branchId) {
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(() ->
                new IllegalArgumentException("Book not found: " + isbn));

        Patron patron = patronRepository.findById(patronId).orElseThrow(() ->
                new IllegalArgumentException("Patron not found: " + patronId));

        if (!book.isAvailable()) {
            throw new IllegalStateException("This book is not available right now: " + isbn);
        }

        book.setStatus(BookStatus.BORROWED);
        bookRepository.save(book);

        String recordId = IdGenerator.generateLendingRecordId();
        LendingRecord record = new LendingRecord(recordId, isbn, patronId, branchId, LocalDate.now());
        lendingRepository.save(record);

        patron.addToBorrowingHistory(isbn);
        patronRepository.save(patron);

        logger.info("Checkout -> Book: {}, Patron: {}, Branch: {}, Record: {}", isbn, patronId, branchId, recordId);
        return record;
    }

    @Override
    public LendingRecord returnBook(String isbn, String patronId, String branchId) {
        LendingRecord record = lendingRepository.findActiveByIsbnAndBranchId(isbn, branchId)
                .orElseThrow(() -> new IllegalStateException("No active checkout found for ISBN: " + isbn + " at branch: " + branchId));

        Book book = bookRepository.findByIsbn(isbn).orElseThrow(() ->
                new IllegalArgumentException("Book not found: " + isbn));

        record.setStatus(LendingStatus.RETURNED);
        record.setReturnDate(LocalDate.now());
        lendingRepository.save(record);

        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);

        logger.info("Return -> Book: {}, Patron: {}, Branch: {}", isbn, patronId, branchId);

        notificationService.notifyBookReturned(new BookReturnedEvent(isbn, branchId, patronId));

        return record;
    }

    @Override
    public List<LendingRecord> getActiveLendings() {
        return lendingRepository.findByStatus(LendingStatus.ACTIVE);
    }

    @Override
    public List<LendingRecord> getLendingsByPatron(String patronId) {
        return lendingRepository.findByPatronId(patronId);
    }
}

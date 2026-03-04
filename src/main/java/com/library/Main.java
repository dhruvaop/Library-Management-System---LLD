package com.library;

import com.library.model.Book;
import com.library.model.Branch;
import com.library.model.LendingRecord;
import com.library.model.Patron;
import com.library.model.Reservation;
import com.library.notification.NotificationService;
import com.library.notification.ReservationAlertListener;
import com.library.recommendation.BasedOnBorrowingHistory;
import com.library.recommendation.BasedOnPopularity;
import com.library.repository.BookRepository;
import com.library.repository.BranchRepository;
import com.library.repository.LendingRepository;
import com.library.repository.PatronRepository;
import com.library.repository.ReservationRepository;
import com.library.repository.impl.InMemoryBookRepository;
import com.library.repository.impl.InMemoryBranchRepository;
import com.library.repository.impl.InMemoryLendingRepository;
import com.library.repository.impl.InMemoryPatronRepository;
import com.library.repository.impl.InMemoryReservationRepository;
import com.library.service.BookService;
import com.library.service.BookServiceImpl;
import com.library.service.BranchService;
import com.library.service.BranchServiceImpl;
import com.library.service.LendingService;
import com.library.service.LendingServiceImpl;
import com.library.service.PatronService;
import com.library.service.PatronServiceImpl;
import com.library.service.RecommendationService;
import com.library.service.RecommendationServiceImpl;
import com.library.service.ReservationService;
import com.library.service.ReservationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        BookRepository bookRepository = new InMemoryBookRepository();
        PatronRepository patronRepository = new InMemoryPatronRepository();
        LendingRepository lendingRepository = new InMemoryLendingRepository();
        ReservationRepository reservationRepository = new InMemoryReservationRepository();
        BranchRepository branchRepository = new InMemoryBranchRepository();

        NotificationService notificationService = new NotificationService();
        ReservationAlertListener alertListener = new ReservationAlertListener(reservationRepository, patronRepository);
        notificationService.addListener(alertListener);

        BranchService branchService = new BranchServiceImpl(branchRepository, bookRepository);
        BookService bookService = new BookServiceImpl(bookRepository, branchRepository);
        PatronService patronService = new PatronServiceImpl(patronRepository);
        LendingService lendingService = new LendingServiceImpl(bookRepository, patronRepository, lendingRepository, notificationService);
        ReservationService reservationService = new ReservationServiceImpl(reservationRepository, bookRepository, patronRepository);

        BasedOnBorrowingHistory historyStrategy = new BasedOnBorrowingHistory(lendingRepository);
        BasedOnPopularity popularityStrategy = new BasedOnPopularity(lendingRepository);
        RecommendationService recommendationService = new RecommendationServiceImpl(patronRepository, bookRepository, historyStrategy, popularityStrategy);

        logger.info("========================================");
        logger.info("  Library Management System - Started  ");
        logger.info("========================================");

        Branch mainBranch = branchService.createBranch("Central Library", "123 Main Street");
        Branch northBranch = branchService.createBranch("North Branch", "456 North Avenue");

        Book book1 = new Book("978-0132350884", "Clean Code", "Robert C. Martin", 2008, "Programming");
        Book book2 = new Book("978-0201616224", "The Pragmatic Programmer", "Andrew Hunt", 1999, "Programming");
        Book book3 = new Book("978-0201633610", "Design Patterns", "Gang of Four", 1994, "Programming");
        Book book4 = new Book("978-0134757599", "Refactoring", "Martin Fowler", 2018, "Programming");
        Book book5 = new Book("978-0547928227", "The Hobbit", "J.R.R. Tolkien", 1937, "Fantasy");

        bookService.addBook(book1, mainBranch.getBranchId());
        bookService.addBook(book2, mainBranch.getBranchId());
        bookService.addBook(book3, mainBranch.getBranchId());
        bookService.addBook(book4, northBranch.getBranchId());
        bookService.addBook(book5, northBranch.getBranchId());

        Patron alice = patronService.registerPatron("Alice Johnson", "alice@example.com", "555-1234");
        Patron bob = patronService.registerPatron("Bob Smith", "bob@example.com", "555-5678");

        logger.info("--- Checkout ---");
        LendingRecord aliceRecord = lendingService.checkoutBook(book1.getIsbn(), alice.getPatronId(), mainBranch.getBranchId());
        logger.info("Alice checked out: {}", book1.getTitle());

        LendingRecord bobRecord = lendingService.checkoutBook(book2.getIsbn(), bob.getPatronId(), mainBranch.getBranchId());
        logger.info("Bob checked out: {}", book2.getTitle());

        logger.info("--- Reservation ---");
        Reservation bobReservation = reservationService.reserveBook(book1.getIsbn(), bob.getPatronId(), mainBranch.getBranchId());
        logger.info("Bob reserved: {} (Reservation ID: {})", book1.getTitle(), bobReservation.getReservationId());

        logger.info("--- Return (triggers notification) ---");
        lendingService.returnBook(book1.getIsbn(), alice.getPatronId(), mainBranch.getBranchId());
        logger.info("Alice returned: {}", book1.getTitle());

        logger.info("--- Search ---");
        List<Book> byAuthor = bookService.searchByAuthor("Martin");
        logger.info("Search results for author 'Martin': {} book(s)", byAuthor.size());
        byAuthor.forEach(b -> logger.info("  -> {}", b.getTitle()));

        List<Book> byTitle = bookService.searchByTitle("Code");
        logger.info("Search results for title 'Code': {} book(s)", byTitle.size());
        byTitle.forEach(b -> logger.info("  -> {}", b.getTitle()));

        logger.info("--- Recommendations ---");
        List<Book> aliceRecs = recommendationService.getRecommendationsForPatron(alice.getPatronId());
        logger.info("Recommendations for Alice (based on history): {} book(s)", aliceRecs.size());
        aliceRecs.forEach(b -> logger.info("  -> {} by {}", b.getTitle(), b.getAuthor()));

        List<Book> popularRecs = recommendationService.getPopularBooksForPatron(bob.getPatronId());
        logger.info("Popular books for Bob: {} book(s)", popularRecs.size());
        popularRecs.forEach(b -> logger.info("  -> {} by {}", b.getTitle(), b.getAuthor()));

        logger.info("--- Transfer Book Between Branches ---");
        branchService.transferBook(book3.getIsbn(), mainBranch.getBranchId(), northBranch.getBranchId());

        logger.info("--- Borrowing History ---");
        List<String> history = patronService.getBorrowingHistory(alice.getPatronId());
        logger.info("Alice's borrowing history: {}", history);

        logger.info("--- Active Lendings ---");
        List<LendingRecord> active = lendingService.getActiveLendings();
        logger.info("Total active lendings: {}", active.size());
        active.forEach(r -> logger.info("  -> ISBN: {}, Patron: {}", r.getIsbn(), r.getPatronId()));

        logger.info("========================================");
        logger.info("             Demo Complete              ");
        logger.info("========================================");
    }
}

package com.library.service;

import com.library.model.Reservation;
import com.library.model.ReservationStatus;
import com.library.repository.BookRepository;
import com.library.repository.PatronRepository;
import com.library.repository.ReservationRepository;
import com.library.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                   BookRepository bookRepository,
                                   PatronRepository patronRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    @Override
    public Reservation reserveBook(String isbn, String patronId, String branchId) {
        bookRepository.findByIsbn(isbn).orElseThrow(() ->
                new IllegalArgumentException("Book not found: " + isbn));

        patronRepository.findById(patronId).orElseThrow(() ->
                new IllegalArgumentException("Patron not found: " + patronId));

        List<Reservation> existing = reservationRepository.findByIsbnAndStatus(isbn, ReservationStatus.PENDING);
        boolean alreadyHasOne = existing.stream().anyMatch(r -> r.getPatronId().equals(patronId));

        if (alreadyHasOne) {
            throw new IllegalStateException("This patron already has a pending reservation for this book.");
        }

        String reservationId = IdGenerator.generateReservationId();
        Reservation reservation = new Reservation(reservationId, isbn, patronId, branchId, LocalDate.now());
        reservationRepository.save(reservation);

        logger.info("Reservation created -> ID: {}, Book: {}, Patron: {}", reservationId, isbn, patronId);
        return reservation;
    }

    @Override
    public void cancelReservation(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() ->
                new IllegalArgumentException("Reservation not found: " + reservationId));
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        logger.info("Reservation cancelled -> ID: {}", reservationId);
    }

    @Override
    public List<Reservation> getReservationsByPatron(String patronId) {
        return reservationRepository.findByPatronId(patronId);
    }

    @Override
    public List<Reservation> getPendingReservationsForBook(String isbn) {
        return reservationRepository.findByIsbnAndStatus(isbn, ReservationStatus.PENDING);
    }
}

package com.library.notification;

import com.library.model.Reservation;
import com.library.model.ReservationStatus;
import com.library.repository.PatronRepository;
import com.library.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReservationAlertListener implements BookReturnedListener {

    private static final Logger logger = LoggerFactory.getLogger(ReservationAlertListener.class);

    private final ReservationRepository reservationRepository;
    private final PatronRepository patronRepository;

    public ReservationAlertListener(ReservationRepository reservationRepository, PatronRepository patronRepository) {
        this.reservationRepository = reservationRepository;
        this.patronRepository = patronRepository;
    }

    @Override
    public void onBookReturned(BookReturnedEvent event) {
        List<Reservation> pendingReservations = reservationRepository.findByIsbnAndStatus(
                event.getIsbn(), ReservationStatus.PENDING
        );

        for (Reservation reservation : pendingReservations) {
            patronRepository.findById(reservation.getPatronId()).ifPresent(patron -> {
                logger.info("ALERT -> Hey {}! The book with ISBN {} is now available. Your reservation ID is {}.",
                        patron.getName(), event.getIsbn(), reservation.getReservationId());
            });
        }
    }
}

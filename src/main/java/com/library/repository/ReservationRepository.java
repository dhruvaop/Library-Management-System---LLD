package com.library.repository;

import com.library.model.Reservation;
import com.library.model.ReservationStatus;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    void save(Reservation reservation);
    Optional<Reservation> findById(String reservationId);
    List<Reservation> findByIsbnAndStatus(String isbn, ReservationStatus status);
    List<Reservation> findByPatronId(String patronId);
    List<Reservation> findAll();
}

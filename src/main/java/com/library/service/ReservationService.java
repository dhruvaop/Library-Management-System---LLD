package com.library.service;

import com.library.model.Reservation;
import java.util.List;

public interface ReservationService {
    Reservation reserveBook(String isbn, String patronId, String branchId);
    void cancelReservation(String reservationId);
    List<Reservation> getReservationsByPatron(String patronId);
    List<Reservation> getPendingReservationsForBook(String isbn);
}

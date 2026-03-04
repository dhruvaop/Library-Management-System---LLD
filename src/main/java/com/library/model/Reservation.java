package com.library.model;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private final String reservationId;
    private final String isbn;
    private final String patronId;
    private final String branchId;
    private final LocalDate reservationDate;
    private ReservationStatus status;

    public Reservation(String reservationId, String isbn, String patronId, String branchId, LocalDate reservationDate) {
        this.reservationId = reservationId;
        this.isbn = isbn;
        this.patronId = patronId;
        this.branchId = branchId;
        this.reservationDate = reservationDate;
        this.status = ReservationStatus.PENDING;
    }

    public String getReservationId() { return reservationId; }
    public String getIsbn() { return isbn; }
    public String getPatronId() { return patronId; }
    public String getBranchId() { return branchId; }
    public LocalDate getReservationDate() { return reservationDate; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }

    @Override
    public String toString() {
        return "Reservation{reservationId='" + reservationId + "', isbn='" + isbn + "', patronId='" + patronId + "', status=" + status + "}";
    }
}

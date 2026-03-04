package com.library.model;

import java.time.LocalDate;
import java.util.Objects;

public class LendingRecord {

    private final String recordId;
    private final String isbn;
    private final String patronId;
    private final String branchId;
    private final LocalDate checkoutDate;
    private LocalDate returnDate;
    private LendingStatus status;

    public LendingRecord(String recordId, String isbn, String patronId, String branchId, LocalDate checkoutDate) {
        this.recordId = recordId;
        this.isbn = isbn;
        this.patronId = patronId;
        this.branchId = branchId;
        this.checkoutDate = checkoutDate;
        this.status = LendingStatus.ACTIVE;
    }

    public String getRecordId() { return recordId; }
    public String getIsbn() { return isbn; }
    public String getPatronId() { return patronId; }
    public String getBranchId() { return branchId; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public LendingStatus getStatus() { return status; }
    public void setStatus(LendingStatus status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LendingRecord)) return false;
        LendingRecord that = (LendingRecord) o;
        return Objects.equals(recordId, that.recordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId);
    }

    @Override
    public String toString() {
        return "LendingRecord{recordId='" + recordId + "', isbn='" + isbn + "', patronId='" + patronId + "', status=" + status + "}";
    }
}

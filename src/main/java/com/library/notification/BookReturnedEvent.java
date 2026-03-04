package com.library.notification;

public class BookReturnedEvent {

    private final String isbn;
    private final String branchId;
    private final String returnedByPatronId;

    public BookReturnedEvent(String isbn, String branchId, String returnedByPatronId) {
        this.isbn = isbn;
        this.branchId = branchId;
        this.returnedByPatronId = returnedByPatronId;
    }

    public String getIsbn() { return isbn; }
    public String getBranchId() { return branchId; }
    public String getReturnedByPatronId() { return returnedByPatronId; }
}

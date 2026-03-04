package com.library.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Patron {

    private final String patronId;
    private String name;
    private String email;
    private String phone;
    private final List<String> borrowingHistory;

    public Patron(String patronId, String name, String email, String phone) {
        this.patronId = patronId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.borrowingHistory = new ArrayList<>();
    }

    public String getPatronId() { return patronId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<String> getBorrowingHistory() {
        return new ArrayList<>(borrowingHistory);
    }

    public void addToBorrowingHistory(String isbn) {
        borrowingHistory.add(isbn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patron)) return false;
        Patron patron = (Patron) o;
        return Objects.equals(patronId, patron.patronId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patronId);
    }

    @Override
    public String toString() {
        return "Patron{patronId='" + patronId + "', name='" + name + "', email='" + email + "'}";
    }
}

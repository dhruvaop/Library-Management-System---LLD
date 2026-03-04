package com.library.service;

import com.library.model.Patron;
import java.util.List;
import java.util.Optional;

public interface PatronService {
    Patron registerPatron(String name, String email, String phone);
    void updatePatron(Patron patron);
    Optional<Patron> findById(String patronId);
    Optional<Patron> findByEmail(String email);
    List<Patron> getAllPatrons();
    List<String> getBorrowingHistory(String patronId);
}

package com.library.repository;

import com.library.model.Patron;
import java.util.List;
import java.util.Optional;

public interface PatronRepository {
    void save(Patron patron);
    Optional<Patron> findById(String patronId);
    Optional<Patron> findByEmail(String email);
    List<Patron> findAll();
    void delete(String patronId);
    boolean existsById(String patronId);
}

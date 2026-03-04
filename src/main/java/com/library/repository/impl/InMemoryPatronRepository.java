package com.library.repository.impl;

import com.library.model.Patron;
import com.library.repository.PatronRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryPatronRepository implements PatronRepository {

    private final Map<String, Patron> store = new HashMap<>();

    @Override
    public void save(Patron patron) {
        store.put(patron.getPatronId(), patron);
    }

    @Override
    public Optional<Patron> findById(String patronId) {
        return Optional.ofNullable(store.get(patronId));
    }

    @Override
    public Optional<Patron> findByEmail(String email) {
        return store.values().stream()
                .filter(p -> p.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<Patron> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(String patronId) {
        store.remove(patronId);
    }

    @Override
    public boolean existsById(String patronId) {
        return store.containsKey(patronId);
    }
}

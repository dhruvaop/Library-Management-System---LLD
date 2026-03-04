package com.library.repository.impl;

import com.library.model.Reservation;
import com.library.model.ReservationStatus;
import com.library.repository.ReservationRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryReservationRepository implements ReservationRepository {

    private final Map<String, Reservation> store = new HashMap<>();

    @Override
    public void save(Reservation reservation) {
        store.put(reservation.getReservationId(), reservation);
    }

    @Override
    public Optional<Reservation> findById(String reservationId) {
        return Optional.ofNullable(store.get(reservationId));
    }

    @Override
    public List<Reservation> findByIsbnAndStatus(String isbn, ReservationStatus status) {
        return store.values().stream()
                .filter(r -> r.getIsbn().equals(isbn) && r.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByPatronId(String patronId) {
        return store.values().stream()
                .filter(r -> r.getPatronId().equals(patronId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(store.values());
    }
}

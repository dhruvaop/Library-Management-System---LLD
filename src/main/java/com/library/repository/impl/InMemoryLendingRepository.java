package com.library.repository.impl;

import com.library.model.LendingRecord;
import com.library.model.LendingStatus;
import com.library.repository.LendingRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryLendingRepository implements LendingRepository {

    private final Map<String, LendingRecord> store = new HashMap<>();

    @Override
    public void save(LendingRecord record) {
        store.put(record.getRecordId(), record);
    }

    @Override
    public Optional<LendingRecord> findById(String recordId) {
        return Optional.ofNullable(store.get(recordId));
    }

    @Override
    public List<LendingRecord> findByPatronId(String patronId) {
        return store.values().stream()
                .filter(r -> r.getPatronId().equals(patronId))
                .collect(Collectors.toList());
    }

    @Override
    public List<LendingRecord> findByIsbn(String isbn) {
        return store.values().stream()
                .filter(r -> r.getIsbn().equals(isbn))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LendingRecord> findActiveByIsbnAndBranchId(String isbn, String branchId) {
        return store.values().stream()
                .filter(r -> r.getIsbn().equals(isbn)
                        && r.getBranchId().equals(branchId)
                        && r.getStatus() == LendingStatus.ACTIVE)
                .findFirst();
    }

    @Override
    public List<LendingRecord> findByStatus(LendingStatus status) {
        return store.values().stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<LendingRecord> findAll() {
        return new ArrayList<>(store.values());
    }
}

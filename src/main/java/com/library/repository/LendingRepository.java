package com.library.repository;

import com.library.model.LendingRecord;
import com.library.model.LendingStatus;
import java.util.List;
import java.util.Optional;

public interface LendingRepository {
    void save(LendingRecord record);
    Optional<LendingRecord> findById(String recordId);
    List<LendingRecord> findByPatronId(String patronId);
    List<LendingRecord> findByIsbn(String isbn);
    Optional<LendingRecord> findActiveByIsbnAndBranchId(String isbn, String branchId);
    List<LendingRecord> findByStatus(LendingStatus status);
    List<LendingRecord> findAll();
}

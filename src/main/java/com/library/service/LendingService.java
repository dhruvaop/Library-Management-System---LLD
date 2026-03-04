package com.library.service;

import com.library.model.LendingRecord;
import java.util.List;

public interface LendingService {
    LendingRecord checkoutBook(String isbn, String patronId, String branchId);
    LendingRecord returnBook(String isbn, String patronId, String branchId);
    List<LendingRecord> getActiveLendings();
    List<LendingRecord> getLendingsByPatron(String patronId);
}

package com.library.service;

import com.library.model.Branch;
import java.util.List;
import java.util.Optional;

public interface BranchService {
    Branch createBranch(String name, String address);
    Optional<Branch> findById(String branchId);
    List<Branch> getAllBranches();
    void transferBook(String isbn, String fromBranchId, String toBranchId);
}

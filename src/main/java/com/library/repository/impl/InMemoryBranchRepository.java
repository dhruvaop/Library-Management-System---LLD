package com.library.repository.impl;

import com.library.model.Branch;
import com.library.repository.BranchRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryBranchRepository implements BranchRepository {

    private final Map<String, Branch> store = new HashMap<>();

    @Override
    public void save(Branch branch) {
        store.put(branch.getBranchId(), branch);
    }

    @Override
    public Optional<Branch> findById(String branchId) {
        return Optional.ofNullable(store.get(branchId));
    }

    @Override
    public List<Branch> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean existsById(String branchId) {
        return store.containsKey(branchId);
    }
}

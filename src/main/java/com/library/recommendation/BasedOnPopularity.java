package com.library.recommendation;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.LendingRecord;
import com.library.model.Patron;
import com.library.repository.LendingRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BasedOnPopularity implements RecommendationStrategy {

    private final LendingRepository lendingRepository;

    public BasedOnPopularity(LendingRepository lendingRepository) {
        this.lendingRepository = lendingRepository;
    }

    @Override
    public List<Book> recommend(Patron patron, List<Book> allBooks) {
        Set<String> alreadyRead = lendingRepository.findByPatronId(patron.getPatronId())
                .stream()
                .map(LendingRecord::getIsbn)
                .collect(Collectors.toSet());

        Map<String, Long> borrowCounts = lendingRepository.findAll().stream()
                .collect(Collectors.groupingBy(LendingRecord::getIsbn, Collectors.counting()));

        return allBooks.stream()
                .filter(b -> !alreadyRead.contains(b.getIsbn()))
                .filter(b -> b.getStatus() == BookStatus.AVAILABLE)
                .sorted(Comparator.comparingLong(b -> -borrowCounts.getOrDefault(b.getIsbn(), 0L)))
                .limit(10)
                .collect(Collectors.toList());
    }
}

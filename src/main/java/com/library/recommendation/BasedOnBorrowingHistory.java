package com.library.recommendation;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.LendingRecord;
import com.library.model.Patron;
import com.library.repository.LendingRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BasedOnBorrowingHistory implements RecommendationStrategy {

    private final LendingRepository lendingRepository;

    public BasedOnBorrowingHistory(LendingRepository lendingRepository) {
        this.lendingRepository = lendingRepository;
    }

    @Override
    public List<Book> recommend(Patron patron, List<Book> allBooks) {
        List<LendingRecord> history = lendingRepository.findByPatronId(patron.getPatronId());

        Set<String> alreadyRead = history.stream()
                .map(LendingRecord::getIsbn)
                .collect(Collectors.toSet());

        Set<String> likedAuthors = allBooks.stream()
                .filter(b -> alreadyRead.contains(b.getIsbn()))
                .map(Book::getAuthor)
                .collect(Collectors.toSet());

        Set<String> likedGenres = allBooks.stream()
                .filter(b -> alreadyRead.contains(b.getIsbn()))
                .map(Book::getGenre)
                .collect(Collectors.toSet());

        return allBooks.stream()
                .filter(b -> !alreadyRead.contains(b.getIsbn()))
                .filter(b -> b.getStatus() == BookStatus.AVAILABLE)
                .filter(b -> likedAuthors.contains(b.getAuthor()) || likedGenres.contains(b.getGenre()))
                .limit(10)
                .collect(Collectors.toList());
    }
}

package com.library.service;

import com.library.model.Book;
import com.library.model.Patron;
import com.library.recommendation.BasedOnBorrowingHistory;
import com.library.recommendation.BasedOnPopularity;
import com.library.recommendation.RecommendationStrategy;
import com.library.repository.BookRepository;
import com.library.repository.PatronRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final PatronRepository patronRepository;
    private final BookRepository bookRepository;
    private final RecommendationStrategy byHistory;
    private final RecommendationStrategy byPopularity;

    public RecommendationServiceImpl(PatronRepository patronRepository, BookRepository bookRepository,
                                      BasedOnBorrowingHistory byHistory, BasedOnPopularity byPopularity) {
        this.patronRepository = patronRepository;
        this.bookRepository = bookRepository;
        this.byHistory = byHistory;
        this.byPopularity = byPopularity;
    }

    @Override
    public List<Book> getRecommendationsForPatron(String patronId) {
        Patron patron = patronRepository.findById(patronId).orElseThrow(() ->
                new IllegalArgumentException("Patron not found: " + patronId));
        List<Book> results = byHistory.recommend(patron, bookRepository.findAll());
        logger.info("History-based recommendations for patron {}: {} books found", patronId, results.size());
        return results;
    }

    @Override
    public List<Book> getPopularBooksForPatron(String patronId) {
        Patron patron = patronRepository.findById(patronId).orElseThrow(() ->
                new IllegalArgumentException("Patron not found: " + patronId));
        List<Book> results = byPopularity.recommend(patron, bookRepository.findAll());
        logger.info("Popularity-based recommendations for patron {}: {} books found", patronId, results.size());
        return results;
    }
}

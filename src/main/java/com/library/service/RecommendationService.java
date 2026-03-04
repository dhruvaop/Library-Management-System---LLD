package com.library.service;

import com.library.model.Book;
import java.util.List;

public interface RecommendationService {
    List<Book> getRecommendationsForPatron(String patronId);
    List<Book> getPopularBooksForPatron(String patronId);
}

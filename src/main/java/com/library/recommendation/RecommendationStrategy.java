package com.library.recommendation;

import com.library.model.Book;
import com.library.model.Patron;

import java.util.List;

public interface RecommendationStrategy {
    List<Book> recommend(Patron patron, List<Book> allBooks);
}

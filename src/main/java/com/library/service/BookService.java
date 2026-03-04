package com.library.service;

import com.library.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
    void addBook(Book book, String branchId);
    void removeBook(String isbn, String branchId);
    void updateBook(Book book);
    Optional<Book> findByIsbn(String isbn);
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
    List<Book> getAllBooks();
}

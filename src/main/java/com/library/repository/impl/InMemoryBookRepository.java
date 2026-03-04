package com.library.repository.impl;

import com.library.model.Book;
import com.library.repository.BookRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryBookRepository implements BookRepository {

    private final Map<String, Book> store = new HashMap<>();

    @Override
    public void save(Book book) {
        store.put(book.getIsbn(), book);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(store.get(isbn));
    }

    @Override
    public List<Book> findByTitle(String title) {
        return store.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return store.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(String isbn) {
        store.remove(isbn);
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return store.containsKey(isbn);
    }
}

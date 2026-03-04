package com.library.service;

import com.library.model.Patron;
import com.library.repository.PatronRepository;
import com.library.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class PatronServiceImpl implements PatronService {

    private static final Logger logger = LoggerFactory.getLogger(PatronServiceImpl.class);

    private final PatronRepository patronRepository;

    public PatronServiceImpl(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    @Override
    public Patron registerPatron(String name, String email, String phone) {
        patronRepository.findByEmail(email).ifPresent(p -> {
            throw new IllegalStateException("A patron with this email already exists: " + email);
        });
        String patronId = IdGenerator.generatePatronId();
        Patron patron = new Patron(patronId, name, email, phone);
        patronRepository.save(patron);
        logger.info("Patron registered -> ID: {}, Name: {}", patronId, name);
        return patron;
    }

    @Override
    public void updatePatron(Patron patron) {
        patronRepository.findById(patron.getPatronId()).orElseThrow(() ->
                new IllegalArgumentException("Patron not found: " + patron.getPatronId()));
        patronRepository.save(patron);
        logger.info("Patron updated -> ID: {}", patron.getPatronId());
    }

    @Override
    public Optional<Patron> findById(String patronId) {
        return patronRepository.findById(patronId);
    }

    @Override
    public Optional<Patron> findByEmail(String email) {
        return patronRepository.findByEmail(email);
    }

    @Override
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    @Override
    public List<String> getBorrowingHistory(String patronId) {
        Patron patron = patronRepository.findById(patronId).orElseThrow(() ->
                new IllegalArgumentException("Patron not found: " + patronId));
        return patron.getBorrowingHistory();
    }
}

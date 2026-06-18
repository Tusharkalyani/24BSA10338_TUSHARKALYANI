package com.campusconnect.campusconnect.service;

import com.campusconnect.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.campusconnect.model.LibraryCard;
import com.campusconnect.campusconnect.repository.LibraryCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryCardService {

    private static final Logger log = LoggerFactory.getLogger(LibraryCardService.class);

    private final LibraryCardRepository libraryCardRepository;

    public LibraryCardService(LibraryCardRepository libraryCardRepository) {
        this.libraryCardRepository = libraryCardRepository;
    }

    public LibraryCard createLibraryCard(LibraryCard card) {
        log.info("Creating library card: {}", card);
        LibraryCard saved = libraryCardRepository.save(card);
        log.info("Created library card with ID: {}", saved.getId());
        return saved;
    }

    public List<LibraryCard> createLibraryCardsBulk(List<LibraryCard> cards) {
        log.info("Bulk inserting {} library cards", cards.size());
        List<LibraryCard> saved = libraryCardRepository.saveAll(cards);
        log.info("Bulk insertion complete. Saved {} records", saved.size());
        return saved;
    }

    public List<LibraryCard> getAllLibraryCards() {
        log.info("Fetching all library cards");
        return libraryCardRepository.findAll();
    }

    public LibraryCard getLibraryCardById(String id) {
        log.info("Fetching library card by ID: {}", id);
        return libraryCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LibraryCard not found with ID: " + id));
    }

    public LibraryCard updateLibraryCard(String id, LibraryCard details) {
        log.info("Updating library card ID: {}", id);
        LibraryCard card = getLibraryCardById(id);
        card.setCardNumber(details.getCardNumber());
        card.setIssueDate(details.getIssueDate());
        card.setActive(details.isActive());
        return libraryCardRepository.save(card);
    }

    public LibraryCard patchLibraryCard(String id, LibraryCard details) {
        log.info("Partially updating library card ID: {}", id);
        LibraryCard card = getLibraryCardById(id);
        if (details.getCardNumber() != null) {
            card.setCardNumber(details.getCardNumber());
        }
        if (details.getIssueDate() != null) {
            card.setIssueDate(details.getIssueDate());
        }
        card.setActive(details.isActive());
        return libraryCardRepository.save(card);
    }

    public void deleteLibraryCard(String id) {
        log.info("Deleting library card ID: {}", id);
        LibraryCard card = getLibraryCardById(id);
        libraryCardRepository.delete(card);
        log.info("Deleted library card ID: {}", id);
    }

    public void clearCollection() {
        log.info("Safely clearing library cards collection");
        libraryCardRepository.deleteAll();
        log.info("Library cards collection cleared");
    }
}

package com.campusconnect.campusconnect.controller;

import com.campusconnect.campusconnect.model.LibraryCard;
import com.campusconnect.campusconnect.service.LibraryCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library-cards")
public class LibraryCardController {

    private final LibraryCardService libraryCardService;

    public LibraryCardController(LibraryCardService libraryCardService) {
        this.libraryCardService = libraryCardService;
    }

    @PostMapping
    public ResponseEntity<LibraryCard> createLibraryCard(@RequestBody LibraryCard card) {
        return new ResponseEntity<>(libraryCardService.createLibraryCard(card), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<LibraryCard>> createLibraryCardsBulk(@RequestBody List<LibraryCard> cards) {
        return new ResponseEntity<>(libraryCardService.createLibraryCardsBulk(cards), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LibraryCard>> getAllLibraryCards() {
        return ResponseEntity.ok(libraryCardService.getAllLibraryCards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryCard> getLibraryCardById(@PathVariable String id) {
        return ResponseEntity.ok(libraryCardService.getLibraryCardById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibraryCard> updateLibraryCard(@PathVariable String id, @RequestBody LibraryCard card) {
        return ResponseEntity.ok(libraryCardService.updateLibraryCard(id, card));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LibraryCard> patchLibraryCard(@PathVariable String id, @RequestBody LibraryCard card) {
        return ResponseEntity.ok(libraryCardService.patchLibraryCard(id, card));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibraryCard(@PathVariable String id) {
        libraryCardService.deleteLibraryCard(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCollection() {
        libraryCardService.clearCollection();
        return ResponseEntity.noContent().build();
    }
}

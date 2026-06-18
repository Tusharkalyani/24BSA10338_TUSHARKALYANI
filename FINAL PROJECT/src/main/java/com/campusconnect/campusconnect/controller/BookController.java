package com.campusconnect.campusconnect.controller;

import com.campusconnect.campusconnect.model.Book;
import com.campusconnect.campusconnect.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.createBook(book), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Book>> createBooksBulk(@RequestBody List<Book> books) {
        return new ResponseEntity<>(bookService.createBooksBulk(books), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/department/{deptId}")
    public ResponseEntity<List<Book>> getBooksByDepartment(@PathVariable String deptId) {
        return ResponseEntity.ok(bookService.getBooksByDepartment(deptId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody Book book) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> patchBook(@PathVariable String id, @RequestBody Book book) {
        return ResponseEntity.ok(bookService.patchBook(id, book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCollection() {
        bookService.clearCollection();
        return ResponseEntity.noContent().build();
    }
}

package com.campusconnect.campusconnect.service;

import com.campusconnect.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.campusconnect.model.Book;
import com.campusconnect.campusconnect.model.Department;
import com.campusconnect.campusconnect.repository.BookRepository;
import com.campusconnect.campusconnect.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;
    private final DepartmentRepository departmentRepository;

    public BookService(BookRepository bookRepository, DepartmentRepository departmentRepository) {
        this.bookRepository = bookRepository;
        this.departmentRepository = departmentRepository;
    }

    public Book createBook(Book book) {
        log.info("Creating book: {}", book);
        resolveDepartments(book);
        Book saved = bookRepository.save(book);
        log.info("Created book with ID: {}", saved.getId());
        return saved;
    }

    public List<Book> createBooksBulk(List<Book> books) {
        log.info("Bulk inserting {} books", books.size());
        books.forEach(this::resolveDepartments);
        List<Book> saved = bookRepository.saveAll(books);
        log.info("Bulk insertion complete. Saved {} records", saved.size());
        return saved;
    }

    public List<Book> getAllBooks() {
        log.info("Fetching all books");
        return bookRepository.findAll();
    }

    public Book getBookById(String id) {
        log.info("Fetching book by ID: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
    }

    public List<Book> getBooksByDepartment(String deptId) {
        log.info("Fetching books by department ID: {}", deptId);
        return bookRepository.findByDepartmentsId(deptId);
    }

    public Book updateBook(String id, Book details) {
        log.info("Updating book ID: {}", id);
        Book book = getBookById(id);
        book.setTitle(details.getTitle());
        book.setAuthor(details.getAuthor());
        book.setIsbn(details.getIsbn());
        book.setDepartments(details.getDepartments());
        resolveDepartments(book);
        return bookRepository.save(book);
    }

    public Book patchBook(String id, Book details) {
        log.info("Partially updating book ID: {}", id);
        Book book = getBookById(id);
        if (details.getTitle() != null) {
            book.setTitle(details.getTitle());
        }
        if (details.getAuthor() != null) {
            book.setAuthor(details.getAuthor());
        }
        if (details.getIsbn() != null) {
            book.setIsbn(details.getIsbn());
        }
        if (details.getDepartments() != null) {
            book.setDepartments(details.getDepartments());
            resolveDepartments(book);
        }
        return bookRepository.save(book);
    }

    public void deleteBook(String id) {
        log.info("Deleting book ID: {}", id);
        Book book = getBookById(id);
        bookRepository.delete(book);
        log.info("Deleted book ID: {}", id);
    }

    public void clearCollection() {
        log.info("Safely clearing books collection");
        bookRepository.deleteAll();
        log.info("Books collection cleared");
    }

    private void resolveDepartments(Book book) {
        if (book.getDepartments() != null && !book.getDepartments().isEmpty()) {
            List<Department> resolved = book.getDepartments().stream()
                    .map(d -> departmentRepository.findById(d.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + d.getId())))
                    .collect(Collectors.toList());
            book.setDepartments(resolved);
        }
    }
}

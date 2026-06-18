package com.campusconnect.campusconnect.service;

import com.campusconnect.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.campusconnect.model.Book;
import com.campusconnect.campusconnect.model.Department;
import com.campusconnect.campusconnect.model.LibraryCard;
import com.campusconnect.campusconnect.model.Student;
import com.campusconnect.campusconnect.repository.BookRepository;
import com.campusconnect.campusconnect.repository.DepartmentRepository;
import com.campusconnect.campusconnect.repository.LibraryCardRepository;
import com.campusconnect.campusconnect.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final LibraryCardRepository libraryCardRepository;
    private final BookRepository bookRepository;

    public StudentService(StudentRepository studentRepository,
                          DepartmentRepository departmentRepository,
                          LibraryCardRepository libraryCardRepository,
                          BookRepository bookRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.libraryCardRepository = libraryCardRepository;
        this.bookRepository = bookRepository;
    }

    public Student createStudent(Student student) {
        log.info("Creating student: {}", student);
        resolveRelationships(student);
        Student saved = studentRepository.save(student);
        log.info("Created student with ID: {}", saved.getId());
        return saved;
    }

    public List<Student> createStudentsBulk(List<Student> students) {
        log.info("Bulk inserting {} students", students.size());
        students.forEach(this::resolveRelationships);
        List<Student> saved = studentRepository.saveAll(students);
        log.info("Bulk insertion complete. Saved {} records", saved.size());
        return saved;
    }

    public List<Student> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll();
    }

    public Student getStudentById(String id) {
        log.info("Fetching student by ID: {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    public List<Student> getStudentsByDepartment(String deptId) {
        log.info("Fetching students by department ID: {}", deptId);
        return studentRepository.findByDepartmentId(deptId);
    }

    public Student updateStudent(String id, Student details) {
        log.info("Updating student ID: {}", id);
        Student student = getStudentById(id);
        student.setName(details.getName());
        student.setEmail(details.getEmail());
        student.setDepartment(details.getDepartment());
        student.setLibraryCard(details.getLibraryCard());
        student.setBorrowedBooks(details.getBorrowedBooks());
        resolveRelationships(student);
        return studentRepository.save(student);
    }

    public Student patchStudent(String id, Student details) {
        log.info("Partially updating student ID: {}", id);
        Student student = getStudentById(id);
        if (details.getName() != null) {
            student.setName(details.getName());
        }
        if (details.getEmail() != null) {
            student.setEmail(details.getEmail());
        }
        if (details.getDepartment() != null) {
            student.setDepartment(details.getDepartment());
        }
        if (details.getLibraryCard() != null) {
            student.setLibraryCard(details.getLibraryCard());
        }
        if (details.getBorrowedBooks() != null) {
            student.setBorrowedBooks(details.getBorrowedBooks());
        }
        resolveRelationships(student);
        return studentRepository.save(student);
    }

    public void deleteStudent(String id) {
        log.info("Deleting student ID: {}", id);
        Student student = getStudentById(id);
        studentRepository.delete(student);
        log.info("Deleted student ID: {}", id);
    }

    public void clearCollection() {
        log.info("Safely clearing students collection");
        studentRepository.deleteAll();
        log.info("Students collection cleared");
    }

    public Student borrowBook(String id, String bookId) {
        log.info("Student ID: {} borrowing book ID: {}", id, bookId);
        Student student = getStudentById(id);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));
        
        List<Book> borrowed = student.getBorrowedBooks();
        if (borrowed == null) {
            borrowed = new ArrayList<>();
        }
        // Check contains by ID rather than references directly since database values are detached
        boolean alreadyBorrowed = false;
        for (Book b : borrowed) {
            if (b.getId().equals(bookId)) {
                alreadyBorrowed = true;
                break;
            }
        }
        if (!alreadyBorrowed) {
            borrowed.add(book);
            student.setBorrowedBooks(borrowed);
            student = studentRepository.save(student);
            log.info("Book ID: {} successfully borrowed by student ID: {}", bookId, id);
        } else {
            log.info("Book ID: {} was already borrowed by student ID: {}", bookId, id);
        }
        return student;
    }

    public Student returnBook(String id, String bookId) {
        log.info("Student ID: {} returning book ID: {}", id, bookId);
        Student student = getStudentById(id);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));
        
        List<Book> borrowed = student.getBorrowedBooks();
        if (borrowed != null && borrowed.removeIf(b -> b.getId().equals(bookId))) {
            student.setBorrowedBooks(borrowed);
            student = studentRepository.save(student);
            log.info("Book ID: {} successfully returned by student ID: {}", bookId, id);
        } else {
            log.warn("Book ID: {} was not borrowed by student ID: {}", bookId, id);
        }
        return student;
    }

    private void resolveRelationships(Student student) {
        if (student.getDepartment() != null && student.getDepartment().getId() != null) {
            Department dept = departmentRepository.findById(student.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + student.getDepartment().getId()));
            student.setDepartment(dept);
        }
        if (student.getLibraryCard() != null && student.getLibraryCard().getId() != null) {
            LibraryCard card = libraryCardRepository.findById(student.getLibraryCard().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("LibraryCard not found with ID: " + student.getLibraryCard().getId()));
            student.setLibraryCard(card);
        }
        if (student.getBorrowedBooks() != null && !student.getBorrowedBooks().isEmpty()) {
            List<Book> resolved = student.getBorrowedBooks().stream()
                    .map(b -> bookRepository.findById(b.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + b.getId())))
                    .collect(Collectors.toList());
            student.setBorrowedBooks(resolved);
        }
    }
}

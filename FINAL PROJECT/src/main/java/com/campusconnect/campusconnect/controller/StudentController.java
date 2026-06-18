package com.campusconnect.campusconnect.controller;

import com.campusconnect.campusconnect.model.Student;
import com.campusconnect.campusconnect.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return new ResponseEntity<>(studentService.createStudent(student), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Student>> createStudentsBulk(@RequestBody List<Student> students) {
        return new ResponseEntity<>(studentService.createStudentsBulk(students), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/department/{deptId}")
    public ResponseEntity<List<Student>> getStudentsByDepartment(@PathVariable String deptId) {
        return ResponseEntity.ok(studentService.getStudentsByDepartment(deptId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.updateStudent(id, student));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> patchStudent(@PathVariable String id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.patchStudent(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCollection() {
        studentService.clearCollection();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/borrow/{bookId}")
    public ResponseEntity<Student> borrowBook(@PathVariable String id, @PathVariable String bookId) {
        return ResponseEntity.ok(studentService.borrowBook(id, bookId));
    }

    @PostMapping("/{id}/return/{bookId}")
    public ResponseEntity<Student> returnBook(@PathVariable String id, @PathVariable String bookId) {
        return ResponseEntity.ok(studentService.returnBook(id, bookId));
    }
}

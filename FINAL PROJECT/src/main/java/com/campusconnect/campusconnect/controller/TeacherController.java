package com.campusconnect.campusconnect.controller;

import com.campusconnect.campusconnect.model.Teacher;
import com.campusconnect.campusconnect.service.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
        return new ResponseEntity<>(teacherService.createTeacher(teacher), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Teacher>> createTeachersBulk(@RequestBody List<Teacher> teachers) {
        return new ResponseEntity<>(teacherService.createTeachersBulk(teachers), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable String id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @GetMapping("/department/{deptId}")
    public ResponseEntity<List<Teacher>> getTeachersByDepartment(@PathVariable String deptId) {
        return ResponseEntity.ok(teacherService.getTeachersByDepartment(deptId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable String id, @RequestBody Teacher teacher) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, teacher));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Teacher> patchTeacher(@PathVariable String id, @RequestBody Teacher teacher) {
        return ResponseEntity.ok(teacherService.patchTeacher(id, teacher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCollection() {
        teacherService.clearCollection();
        return ResponseEntity.noContent().build();
    }
}

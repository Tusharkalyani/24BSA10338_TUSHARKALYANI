package com.campusconnect.campusconnect.controller;

import com.campusconnect.campusconnect.model.Department;
import com.campusconnect.campusconnect.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return new ResponseEntity<>(departmentService.createDepartment(department), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Department>> createDepartmentsBulk(@RequestBody List<Department> departments) {
        return new ResponseEntity<>(departmentService.createDepartmentsBulk(departments), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable String id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable String id, @RequestBody Department department) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, department));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Department> patchDepartment(@PathVariable String id, @RequestBody Department department) {
        return ResponseEntity.ok(departmentService.patchDepartment(id, department));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable String id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCollection() {
        departmentService.clearCollection();
        return ResponseEntity.noContent().build();
    }
}

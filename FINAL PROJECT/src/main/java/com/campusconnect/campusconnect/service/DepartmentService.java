package com.campusconnect.campusconnect.service;

import com.campusconnect.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.campusconnect.model.Department;
import com.campusconnect.campusconnect.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(Department department) {
        log.info("Creating department: {}", department);
        Department saved = departmentRepository.save(department);
        log.info("Created department with ID: {}", saved.getId());
        return saved;
    }

    public List<Department> createDepartmentsBulk(List<Department> departments) {
        log.info("Bulk inserting {} departments", departments.size());
        List<Department> saved = departmentRepository.saveAll(departments);
        log.info("Bulk insertion complete. Saved {} records", saved.size());
        return saved;
    }

    public List<Department> getAllDepartments() {
        log.info("Fetching all departments");
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(String id) {
        log.info("Fetching department by ID: {}", id);
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
    }

    public Department updateDepartment(String id, Department deptDetails) {
        log.info("Updating department ID: {}", id);
        Department dept = getDepartmentById(id);
        dept.setName(deptDetails.getName());
        dept.setCode(deptDetails.getCode());
        return departmentRepository.save(dept);
    }

    public Department patchDepartment(String id, Department deptDetails) {
        log.info("Partially updating department ID: {}", id);
        Department dept = getDepartmentById(id);
        if (deptDetails.getName() != null) {
            dept.setName(deptDetails.getName());
        }
        if (deptDetails.getCode() != null) {
            dept.setCode(deptDetails.getCode());
        }
        return departmentRepository.save(dept);
    }

    public void deleteDepartment(String id) {
        log.info("Deleting department ID: {}", id);
        Department dept = getDepartmentById(id);
        departmentRepository.delete(dept);
        log.info("Deleted department ID: {}", id);
    }

    public void clearCollection() {
        log.info("Safely clearing departments collection");
        departmentRepository.deleteAll();
        log.info("Departments collection cleared");
    }
}

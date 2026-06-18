package com.campusconnect.campusconnect.service;

import com.campusconnect.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.campusconnect.model.Department;
import com.campusconnect.campusconnect.model.Teacher;
import com.campusconnect.campusconnect.repository.DepartmentRepository;
import com.campusconnect.campusconnect.repository.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private static final Logger log = LoggerFactory.getLogger(TeacherService.class);

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;

    public TeacherService(TeacherRepository teacherRepository, DepartmentRepository departmentRepository) {
        this.teacherRepository = teacherRepository;
        this.departmentRepository = departmentRepository;
    }

    public Teacher createTeacher(Teacher teacher) {
        log.info("Creating teacher: {}", teacher);
        resolveDepartment(teacher);
        Teacher saved = teacherRepository.save(teacher);
        log.info("Created teacher with ID: {}", saved.getId());
        return saved;
    }

    public List<Teacher> createTeachersBulk(List<Teacher> teachers) {
        log.info("Bulk inserting {} teachers", teachers.size());
        teachers.forEach(this::resolveDepartment);
        List<Teacher> saved = teacherRepository.saveAll(teachers);
        log.info("Bulk insertion complete. Saved {} records", saved.size());
        return saved;
    }

    public List<Teacher> getAllTeachers() {
        log.info("Fetching all teachers");
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(String id) {
        log.info("Fetching teacher by ID: {}", id);
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + id));
    }

    public List<Teacher> getTeachersByDepartment(String deptId) {
        log.info("Fetching teachers by department ID: {}", deptId);
        return teacherRepository.findByDepartmentId(deptId);
    }

    public Teacher updateTeacher(String id, Teacher details) {
        log.info("Updating teacher ID: {}", id);
        Teacher teacher = getTeacherById(id);
        teacher.setName(details.getName());
        teacher.setEmail(details.getEmail());
        teacher.setSpecialization(details.getSpecialization());
        teacher.setDepartment(details.getDepartment());
        teacher.setOfficeDesk(details.getOfficeDesk());
        resolveDepartment(teacher);
        return teacherRepository.save(teacher);
    }

    public Teacher patchTeacher(String id, Teacher details) {
        log.info("Partially updating teacher ID: {}", id);
        Teacher teacher = getTeacherById(id);
        if (details.getName() != null) {
            teacher.setName(details.getName());
        }
        if (details.getEmail() != null) {
            teacher.setEmail(details.getEmail());
        }
        if (details.getSpecialization() != null) {
            teacher.setSpecialization(details.getSpecialization());
        }
        if (details.getDepartment() != null) {
            teacher.setDepartment(details.getDepartment());
            resolveDepartment(teacher);
        }
        if (details.getOfficeDesk() != null) {
            if (teacher.getOfficeDesk() == null) {
                teacher.setOfficeDesk(details.getOfficeDesk());
            } else {
                if (details.getOfficeDesk().getDeskNumber() != null) {
                    teacher.getOfficeDesk().setDeskNumber(details.getOfficeDesk().getDeskNumber());
                }
                if (details.getOfficeDesk().getBuilding() != null) {
                    teacher.getOfficeDesk().setBuilding(details.getOfficeDesk().getBuilding());
                }
                if (details.getOfficeDesk().getFloor() != 0) {
                    teacher.getOfficeDesk().setFloor(details.getOfficeDesk().getFloor());
                }
            }
        }
        return teacherRepository.save(teacher);
    }

    public void deleteTeacher(String id) {
        log.info("Deleting teacher ID: {}", id);
        Teacher teacher = getTeacherById(id);
        teacherRepository.delete(teacher);
        log.info("Deleted teacher ID: {}", id);
    }

    public void clearCollection() {
        log.info("Safely clearing teachers collection");
        teacherRepository.deleteAll();
        log.info("Teachers collection cleared");
    }

    private void resolveDepartment(Teacher teacher) {
        if (teacher.getDepartment() != null && teacher.getDepartment().getId() != null) {
            Department dept = departmentRepository.findById(teacher.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + teacher.getDepartment().getId()));
            teacher.setDepartment(dept);
        }
    }
}

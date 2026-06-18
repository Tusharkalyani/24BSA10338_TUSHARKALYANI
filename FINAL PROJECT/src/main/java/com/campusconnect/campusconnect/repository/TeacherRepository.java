package com.campusconnect.campusconnect.repository;

import com.campusconnect.campusconnect.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends MongoRepository<Teacher, String> {
    Optional<Teacher> findByEmail(String email);
    List<Teacher> findByDepartmentId(String departmentId);
}

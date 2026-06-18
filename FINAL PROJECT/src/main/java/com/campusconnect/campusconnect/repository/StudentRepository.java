package com.campusconnect.campusconnect.repository;

import com.campusconnect.campusconnect.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByEmail(String email);
    List<Student> findByDepartmentId(String departmentId);
    List<Student> findByLibraryCardId(String libraryCardId);
}

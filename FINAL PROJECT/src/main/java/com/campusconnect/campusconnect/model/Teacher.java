package com.campusconnect.campusconnect.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "teachers")
public class Teacher {
    @Id
    private String id;
    private String name;
    private String email;
    private String specialization;
    
    @DBRef
    private Department department;
    
    private OfficeDesk officeDesk; // Embedded document

    public Teacher() {}

    public Teacher(String id, String name, String email, String specialization, Department department, OfficeDesk officeDesk) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.specialization = specialization;
        this.department = department;
        this.officeDesk = officeDesk;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public OfficeDesk getOfficeDesk() { return officeDesk; }
    public void setOfficeDesk(OfficeDesk officeDesk) { this.officeDesk = officeDesk; }

    @Override
    public String toString() {
        return "Teacher{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", department=" + department +
                ", officeDesk=" + officeDesk +
                '}';
    }

    public static TeacherBuilder builder() {
        return new TeacherBuilder();
    }

    public static class TeacherBuilder {
        private String id;
        private String name;
        private String email;
        private String specialization;
        private Department department;
        private OfficeDesk officeDesk;

        public TeacherBuilder id(String id) { this.id = id; return this; }
        public TeacherBuilder name(String name) { this.name = name; return this; }
        public TeacherBuilder email(String email) { this.email = email; return this; }
        public TeacherBuilder specialization(String specialization) { this.specialization = specialization; return this; }
        public TeacherBuilder department(Department department) { this.department = department; return this; }
        public TeacherBuilder officeDesk(OfficeDesk officeDesk) { this.officeDesk = officeDesk; return this; }

        public Teacher build() {
            return new Teacher(id, name, email, specialization, department, officeDesk);
        }
    }
}

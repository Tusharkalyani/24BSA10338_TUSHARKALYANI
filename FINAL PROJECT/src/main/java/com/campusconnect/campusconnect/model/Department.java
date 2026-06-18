package com.campusconnect.campusconnect.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "departments")
public class Department {
    @Id
    private String id;
    private String name;
    private String code;

    public Department() {}

    public Department(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    @Override
    public String toString() {
        return "Department{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public static DepartmentBuilder builder() {
        return new DepartmentBuilder();
    }

    public static class DepartmentBuilder {
        private String id;
        private String name;
        private String code;

        public DepartmentBuilder id(String id) { this.id = id; return this; }
        public DepartmentBuilder name(String name) { this.name = name; return this; }
        public DepartmentBuilder code(String code) { this.code = code; return this; }

        public Department build() {
            return new Department(id, name, code);
        }
    }
}

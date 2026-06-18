package com.campusconnect.campusconnect.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "students")
public class Student {
    @Id
    private String id;
    private String name;
    private String email;
    
    @DBRef
    private Department department;
    
    @DBRef
    private LibraryCard libraryCard;
    
    @DBRef
    private List<Book> borrowedBooks;

    public Student() {}

    public Student(String id, String name, String email, Department department, LibraryCard libraryCard, List<Book> borrowedBooks) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.libraryCard = libraryCard;
        this.borrowedBooks = borrowedBooks;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public LibraryCard getLibraryCard() { return libraryCard; }
    public void setLibraryCard(LibraryCard libraryCard) { this.libraryCard = libraryCard; }

    public List<Book> getBorrowedBooks() { return borrowedBooks; }
    public void setBorrowedBooks(List<Book> borrowedBooks) { this.borrowedBooks = borrowedBooks; }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department=" + department +
                ", libraryCard=" + libraryCard +
                ", borrowedBooks=" + borrowedBooks +
                '}';
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public static class StudentBuilder {
        private String id;
        private String name;
        private String email;
        private Department department;
        private LibraryCard libraryCard;
        private List<Book> borrowedBooks;

        public StudentBuilder id(String id) { this.id = id; return this; }
        public StudentBuilder name(String name) { this.name = name; return this; }
        public StudentBuilder email(String email) { this.email = email; return this; }
        public StudentBuilder department(Department department) { this.department = department; return this; }
        public StudentBuilder libraryCard(LibraryCard libraryCard) { this.libraryCard = libraryCard; return this; }
        public StudentBuilder borrowedBooks(List<Book> borrowedBooks) { this.borrowedBooks = borrowedBooks; return this; }

        public Student build() {
            return new Student(id, name, email, department, libraryCard, borrowedBooks);
        }
    }
}

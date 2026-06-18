package com.campusconnect.campusconnect.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "books")
public class Book {
    @Id
    private String id;
    private String title;
    private String author;
    private String isbn;
    
    @DBRef
    private List<Department> departments;

    public Book() {}

    public Book(String id, String title, String author, String isbn, List<Department> departments) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.departments = departments;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public List<Department> getDepartments() { return departments; }
    public void setDepartments(List<Department> departments) { this.departments = departments; }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", departments=" + departments +
                '}';
    }

    public static BookBuilder builder() {
        return new BookBuilder();
    }

    public static class BookBuilder {
        private String id;
        private String title;
        private String author;
        private String isbn;
        private List<Department> departments;

        public BookBuilder id(String id) { this.id = id; return this; }
        public BookBuilder title(String title) { this.title = title; return this; }
        public BookBuilder author(String author) { this.author = author; return this; }
        public BookBuilder isbn(String isbn) { this.isbn = isbn; return this; }
        public BookBuilder departments(List<Department> departments) { this.departments = departments; return this; }

        public Book build() {
            return new Book(id, title, author, isbn, departments);
        }
    }
}

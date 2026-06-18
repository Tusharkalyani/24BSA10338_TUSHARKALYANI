package com.campusconnect.campusconnect.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "library_cards")
public class LibraryCard {
    @Id
    private String id;
    private String cardNumber;
    private LocalDate issueDate;
    private boolean active;

    public LibraryCard() {}

    public LibraryCard(String id, String cardNumber, LocalDate issueDate, boolean active) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.issueDate = issueDate;
        this.active = active;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "LibraryCard{" +
                "id='" + id + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", issueDate=" + issueDate +
                ", active=" + active +
                '}';
    }

    public static LibraryCardBuilder builder() {
        return new LibraryCardBuilder();
    }

    public static class LibraryCardBuilder {
        private String id;
        private String cardNumber;
        private LocalDate issueDate;
        private boolean active;

        public LibraryCardBuilder id(String id) { this.id = id; return this; }
        public LibraryCardBuilder cardNumber(String cardNumber) { this.cardNumber = cardNumber; return this; }
        public LibraryCardBuilder issueDate(LocalDate issueDate) { this.issueDate = issueDate; return this; }
        public LibraryCardBuilder active(boolean active) { this.active = active; return this; }

        public LibraryCard build() {
            return new LibraryCard(id, cardNumber, issueDate, active);
        }
    }
}

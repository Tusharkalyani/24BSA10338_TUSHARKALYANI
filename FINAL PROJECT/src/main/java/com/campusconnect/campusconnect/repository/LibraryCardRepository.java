package com.campusconnect.campusconnect.repository;

import com.campusconnect.campusconnect.model.LibraryCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryCardRepository extends MongoRepository<LibraryCard, String> {
    Optional<LibraryCard> findByCardNumber(String cardNumber);
}

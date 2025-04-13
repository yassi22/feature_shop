package com.example.todoappdeel3.dao;

import com.example.todoappdeel3.models.GiftCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {
    Optional<GiftCard> findByCode(String code);
}


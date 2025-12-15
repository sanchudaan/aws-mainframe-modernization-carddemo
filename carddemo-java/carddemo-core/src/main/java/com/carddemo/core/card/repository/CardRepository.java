package com.carddemo.core.card.repository;

import com.carddemo.core.card.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Card entity - replaces VSAM CARDDAT file operations.
 * Provides CRUD operations and custom queries for card management.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, String> {

    List<Card> findByAccountId(Long accountId);

    Page<Card> findByAccountId(Long accountId, Pageable pageable);

    List<Card> findByActiveStatus(String activeStatus);

    @Query("SELECT c FROM Card c WHERE c.expirationDate < :date AND c.activeStatus = 'Y'")
    List<Card> findExpiringCardsBefore(@Param("date") LocalDate date);

    @Query("SELECT c FROM Card c WHERE c.expirationDate BETWEEN :startDate AND :endDate")
    List<Card> findCardsExpiringBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT c FROM Card c WHERE c.cardNumber LIKE :prefix%")
    List<Card> findByCardNumberPrefix(@Param("prefix") String prefix);

    @Query("SELECT COUNT(c) FROM Card c WHERE c.activeStatus = :status")
    long countByActiveStatus(@Param("status") String status);

    Optional<Card> findByCardNumberAndActiveStatus(String cardNumber, String activeStatus);

    @Query("SELECT c FROM Card c WHERE c.accountId = :accountId AND c.activeStatus = 'Y'")
    List<Card> findActiveCardsByAccountId(@Param("accountId") Long accountId);
}

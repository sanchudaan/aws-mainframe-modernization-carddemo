package com.carddemo.core.transaction.repository;

import com.carddemo.core.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Transaction entity - replaces VSAM TRANSACT and DALYTRAN file operations.
 * Provides CRUD operations and custom queries for transaction management.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByCardNumber(String cardNumber);

    Page<Transaction> findByCardNumber(String cardNumber, Pageable pageable);

    List<Transaction> findByTypeCode(String typeCode);

    Page<Transaction> findByTypeCode(String typeCode, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.originTimestamp BETWEEN :startDate AND :endDate")
    List<Transaction> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.originTimestamp BETWEEN :startDate AND :endDate")
    Page<Transaction> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.originTimestamp BETWEEN :startDate AND :endDate")
    List<Transaction> findByCardNumberAndDateRange(@Param("cardNumber") String cardNumber, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.processedTimestamp IS NULL")
    List<Transaction> findUnprocessedTransactions();

    @Query("SELECT t FROM Transaction t WHERE t.processedTimestamp IS NULL")
    Page<Transaction> findUnprocessedTransactions(Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.merchantId = :merchantId")
    List<Transaction> findByMerchantId(@Param("merchantId") Long merchantId);

    @Query("SELECT t FROM Transaction t WHERE t.amount > :threshold")
    List<Transaction> findLargeTransactions(@Param("threshold") BigDecimal threshold);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.originTimestamp BETWEEN :startDate AND :endDate")
    BigDecimal sumTransactionsByCardAndDateRange(@Param("cardNumber") String cardNumber, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.typeCode = :typeCode AND t.originTimestamp BETWEEN :startDate AND :endDate")
    long countByTypeCodeAndDateRange(@Param("typeCode") String typeCode, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.categoryCode = :categoryCode")
    List<Transaction> findByCategoryCode(@Param("categoryCode") Integer categoryCode);

    @Query("SELECT t.typeCode, COUNT(t), SUM(t.amount) FROM Transaction t WHERE t.originTimestamp BETWEEN :startDate AND :endDate GROUP BY t.typeCode")
    List<Object[]> getTransactionSummaryByType(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}

package com.carddemo.core.card.repository;

import com.carddemo.core.card.entity.CardCrossReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for CardCrossReference entity - replaces VSAM CCXREF file operations.
 * Provides CRUD operations and custom queries for card-customer-account relationships.
 */
@Repository
public interface CardCrossReferenceRepository extends JpaRepository<CardCrossReference, String> {

    List<CardCrossReference> findByCustomerId(Long customerId);

    List<CardCrossReference> findByAccountId(Long accountId);

    @Query("SELECT x FROM CardCrossReference x WHERE x.customerId = :customerId AND x.accountId = :accountId")
    List<CardCrossReference> findByCustomerIdAndAccountId(@Param("customerId") Long customerId, @Param("accountId") Long accountId);

    @Query("SELECT COUNT(x) FROM CardCrossReference x WHERE x.customerId = :customerId")
    long countByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT COUNT(x) FROM CardCrossReference x WHERE x.accountId = :accountId")
    long countByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT DISTINCT x.accountId FROM CardCrossReference x WHERE x.customerId = :customerId")
    List<Long> findDistinctAccountIdsByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT DISTINCT x.customerId FROM CardCrossReference x WHERE x.accountId = :accountId")
    List<Long> findDistinctCustomerIdsByAccountId(@Param("accountId") Long accountId);
}

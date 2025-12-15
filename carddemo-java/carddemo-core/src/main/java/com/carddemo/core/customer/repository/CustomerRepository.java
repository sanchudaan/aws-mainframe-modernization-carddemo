package com.carddemo.core.customer.repository;

import com.carddemo.core.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Customer entity - replaces VSAM CUSTDAT file operations.
 * Provides CRUD operations and custom queries for customer management.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findBySsn(String ssn);

    List<Customer> findByLastName(String lastName);

    Page<Customer> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);

    List<Customer> findByStateCode(String stateCode);

    Page<Customer> findByStateCode(String stateCode, Pageable pageable);

    List<Customer> findByZipCode(String zipCode);

    @Query("SELECT c FROM Customer c WHERE c.lastName LIKE :prefix%")
    List<Customer> findByLastNameStartingWith(@Param("prefix") String prefix);

    @Query("SELECT c FROM Customer c WHERE c.ficoCreditScore >= :minScore")
    List<Customer> findByMinFicoScore(@Param("minScore") Integer minScore);

    @Query("SELECT c FROM Customer c WHERE c.ficoCreditScore BETWEEN :minScore AND :maxScore")
    List<Customer> findByFicoScoreRange(@Param("minScore") Integer minScore, @Param("maxScore") Integer maxScore);

    @Query("SELECT c FROM Customer c WHERE c.primaryCardHolderIndicator = 'Y'")
    List<Customer> findPrimaryCardHolders();

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.stateCode = :stateCode")
    long countByStateCode(@Param("stateCode") String stateCode);

    @Query("SELECT c FROM Customer c WHERE c.customerId >= :startId AND c.customerId <= :endId ORDER BY c.customerId")
    List<Customer> findCustomersInRange(@Param("startId") Long startId, @Param("endId") Long endId);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Customer> searchByName(@Param("name") String name, Pageable pageable);
}

package com.carddemo.core.account.repository;

import com.carddemo.common.constant.AccountStatus;
import com.carddemo.core.account.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository for Account entity - replaces VSAM ACCTDAT file operations.
 * Provides CRUD operations and custom queries for account management.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByStatus(AccountStatus status);

    Page<Account> findByStatus(AccountStatus status, Pageable pageable);

    List<Account> findByGroupId(String groupId);

    @Query("SELECT a FROM Account a WHERE a.currentBalance > :threshold")
    List<Account> findAccountsWithBalanceAbove(@Param("threshold") BigDecimal threshold);

    @Query("SELECT a FROM Account a WHERE a.currentBalance > a.creditLimit * :percentage / 100")
    List<Account> findAccountsNearCreditLimit(@Param("percentage") int percentage);

    @Query("SELECT a FROM Account a WHERE a.expirationDate < CURRENT_DATE AND a.status = 'ACTIVE'")
    List<Account> findExpiredActiveAccounts();

    @Query("SELECT COUNT(a) FROM Account a WHERE a.status = :status")
    long countByStatus(@Param("status") AccountStatus status);

    @Query("SELECT SUM(a.currentBalance) FROM Account a WHERE a.status = 'ACTIVE'")
    BigDecimal sumActiveAccountBalances();

    @Query("SELECT a FROM Account a WHERE a.addressZip = :zip")
    List<Account> findByAddressZip(@Param("zip") String zip);

    @Query("SELECT a FROM Account a WHERE a.accountId >= :startId AND a.accountId <= :endId ORDER BY a.accountId")
    List<Account> findAccountsInRange(@Param("startId") Long startId, @Param("endId") Long endId);
}

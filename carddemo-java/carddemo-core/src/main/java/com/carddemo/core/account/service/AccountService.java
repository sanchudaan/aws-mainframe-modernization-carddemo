package com.carddemo.core.account.service;

import com.carddemo.common.constant.AccountStatus;
import com.carddemo.core.account.entity.Account;
import com.carddemo.core.account.repository.AccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service for Account operations - replaces COBOL programs:
 * - COACTVWC.cbl (Account View)
 * - COACTUPC.cbl (Account Update)
 * - CBACT01C.cbl (Batch Account Processing)
 */
@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Account> findById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    @Transactional(readOnly = true)
    public Page<Account> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Account> findByStatus(AccountStatus status) {
        return accountRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Page<Account> findByStatus(AccountStatus status, Pageable pageable) {
        return accountRepository.findByStatus(status, pageable);
    }

    @Transactional(readOnly = true)
    public List<Account> findByGroupId(String groupId) {
        return accountRepository.findByGroupId(groupId);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account create(Account account) {
        if (account.getAccountId() != null && accountRepository.existsById(account.getAccountId())) {
            throw new IllegalArgumentException("Account already exists with ID: " + account.getAccountId());
        }
        return accountRepository.save(account);
    }

    public Account update(Account account) {
        if (account.getAccountId() == null || !accountRepository.existsById(account.getAccountId())) {
            throw new IllegalArgumentException("Account not found with ID: " + account.getAccountId());
        }
        return accountRepository.save(account);
    }

    public void delete(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    public Account updateBalance(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
        
        BigDecimal newBalance = account.getCurrentBalance().add(amount);
        account.setCurrentBalance(newBalance);
        
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            account.setCurrentCycleCredit(account.getCurrentCycleCredit().add(amount));
        } else {
            account.setCurrentCycleDebit(account.getCurrentCycleDebit().add(amount.abs()));
        }
        
        return accountRepository.save(account);
    }

    public Account updateStatus(Long accountId, AccountStatus status) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
        
        account.setStatus(status);
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<Account> findAccountsNearCreditLimit(int percentage) {
        return accountRepository.findAccountsNearCreditLimit(percentage);
    }

    @Transactional(readOnly = true)
    public List<Account> findExpiredActiveAccounts() {
        return accountRepository.findExpiredActiveAccounts();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalActiveBalance() {
        BigDecimal sum = accountRepository.sumActiveAccountBalances();
        return sum != null ? sum : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public long countByStatus(AccountStatus status) {
        return accountRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Account> findAccountsInRange(Long startId, Long endId) {
        return accountRepository.findAccountsInRange(startId, endId);
    }

    public void resetCycleAmounts(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
        
        account.setCurrentCycleCredit(BigDecimal.ZERO);
        account.setCurrentCycleDebit(BigDecimal.ZERO);
        accountRepository.save(account);
    }
}

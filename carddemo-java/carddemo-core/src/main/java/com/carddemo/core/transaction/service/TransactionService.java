package com.carddemo.core.transaction.service;

import com.carddemo.core.account.service.AccountService;
import com.carddemo.core.card.entity.CardCrossReference;
import com.carddemo.core.card.service.CardService;
import com.carddemo.core.transaction.entity.Transaction;
import com.carddemo.core.transaction.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for Transaction operations - replaces COBOL programs:
 * - COTRN00C.cbl (Transaction Menu)
 * - COTRN01C.cbl (Transaction List)
 * - COTRN02C.cbl (Transaction Add)
 * - CBTRN01C.cbl (Batch Transaction List)
 * - CBTRN02C.cbl (Batch Transaction Post)
 * - CBTRN03C.cbl (Batch Transaction Interest)
 */
@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardService cardService;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, 
                              CardService cardService, 
                              AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.cardService = cardService;
        this.accountService = accountService;
    }

    @Transactional(readOnly = true)
    public Optional<Transaction> findById(String transactionId) {
        return transactionRepository.findById(transactionId);
    }

    @Transactional(readOnly = true)
    public Page<Transaction> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findByCardNumber(String cardNumber) {
        return transactionRepository.findByCardNumber(cardNumber);
    }

    @Transactional(readOnly = true)
    public Page<Transaction> findByCardNumber(String cardNumber, Pageable pageable) {
        return transactionRepository.findByCardNumber(cardNumber, pageable);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findByTypeCode(String typeCode) {
        return transactionRepository.findByTypeCode(typeCode);
    }

    @Transactional(readOnly = true)
    public Page<Transaction> findByTypeCode(String typeCode, Pageable pageable) {
        return transactionRepository.findByTypeCode(typeCode, pageable);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Page<Transaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return transactionRepository.findByDateRange(startDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findByCardNumberAndDateRange(String cardNumber, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByCardNumberAndDateRange(cardNumber, startDate, endDate);
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction create(Transaction transaction) {
        if (transaction.getTransactionId() == null) {
            transaction.setTransactionId(generateTransactionId());
        }
        
        if (transaction.getOriginTimestamp() == null) {
            transaction.setOriginTimestamp(LocalDateTime.now());
        }
        
        return transactionRepository.save(transaction);
    }

    public Transaction createAndProcess(Transaction transaction) {
        Transaction created = create(transaction);
        return processTransaction(created.getTransactionId());
    }

    public Transaction processTransaction(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + transactionId));
        
        if (transaction.isProcessed()) {
            throw new IllegalStateException("Transaction already processed: " + transactionId);
        }
        
        Optional<CardCrossReference> crossRef = cardService.findCrossReference(transaction.getCardNumber());
        if (crossRef.isPresent()) {
            Long accountId = crossRef.get().getAccountId();
            accountService.updateBalance(accountId, transaction.getAmount().negate());
        }
        
        transaction.setProcessedTimestamp(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findUnprocessedTransactions() {
        return transactionRepository.findUnprocessedTransactions();
    }

    @Transactional(readOnly = true)
    public Page<Transaction> findUnprocessedTransactions(Pageable pageable) {
        return transactionRepository.findUnprocessedTransactions(pageable);
    }

    public int processAllPendingTransactions() {
        List<Transaction> pending = transactionRepository.findUnprocessedTransactions();
        int processed = 0;
        
        for (Transaction transaction : pending) {
            try {
                processTransaction(transaction.getTransactionId());
                processed++;
            } catch (Exception e) {
                // Log error and continue with next transaction
            }
        }
        
        return processed;
    }

    @Transactional(readOnly = true)
    public List<Transaction> findByMerchantId(Long merchantId) {
        return transactionRepository.findByMerchantId(merchantId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findLargeTransactions(BigDecimal threshold) {
        return transactionRepository.findLargeTransactions(threshold);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumTransactionsByCardAndDateRange(String cardNumber, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal sum = transactionRepository.sumTransactionsByCardAndDateRange(cardNumber, startDate, endDate);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public long countByTypeCodeAndDateRange(String typeCode, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.countByTypeCodeAndDateRange(typeCode, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findByCategoryCode(Integer categoryCode) {
        return transactionRepository.findByCategoryCode(categoryCode);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getTransactionSummaryByType(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.getTransactionSummaryByType(startDate, endDate);
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}

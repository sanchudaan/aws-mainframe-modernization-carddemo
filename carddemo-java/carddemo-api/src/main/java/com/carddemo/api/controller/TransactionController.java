package com.carddemo.api.controller;

import com.carddemo.api.dto.TransactionDto;
import com.carddemo.core.transaction.entity.Transaction;
import com.carddemo.core.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Transaction operations - replaces CICS transactions:
 * - CTRN (Transaction Menu) -> GET /api/transactions
 * - CTLI (Transaction List) -> GET /api/transactions/card/{cardNumber}
 * - CTAD (Transaction Add) -> POST /api/transactions
 */
@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Transaction management APIs")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @Operation(summary = "List all transactions", description = "Replaces CICS transaction CTRN")
    public ResponseEntity<Page<TransactionDto>> listTransactions(Pageable pageable) {
        Page<TransactionDto> transactions = transactionService.findAll(pageable).map(TransactionDto::from);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable String transactionId) {
        return transactionService.findById(transactionId)
                .map(TransactionDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new transaction", description = "Replaces CICS transaction CTAD")
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        Transaction transaction = transactionDto.toEntity();
        Transaction created = transactionService.create(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionDto.from(created));
    }

    @PostMapping("/process")
    @Operation(summary = "Create and process transaction")
    public ResponseEntity<TransactionDto> createAndProcessTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        Transaction transaction = transactionDto.toEntity();
        Transaction processed = transactionService.createAndProcess(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionDto.from(processed));
    }

    @PostMapping("/{transactionId}/process")
    @Operation(summary = "Process existing transaction")
    public ResponseEntity<TransactionDto> processTransaction(@PathVariable String transactionId) {
        Transaction processed = transactionService.processTransaction(transactionId);
        return ResponseEntity.ok(TransactionDto.from(processed));
    }

    @GetMapping("/card/{cardNumber}")
    @Operation(summary = "List transactions by card number", description = "Replaces CICS transaction CTLI")
    public ResponseEntity<Page<TransactionDto>> listByCardNumber(@PathVariable String cardNumber, Pageable pageable) {
        Page<TransactionDto> transactions = transactionService.findByCardNumber(cardNumber, pageable).map(TransactionDto::from);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/type/{typeCode}")
    @Operation(summary = "List transactions by type code")
    public ResponseEntity<Page<TransactionDto>> listByTypeCode(@PathVariable String typeCode, Pageable pageable) {
        Page<TransactionDto> transactions = transactionService.findByTypeCode(typeCode, pageable).map(TransactionDto::from);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/date-range")
    @Operation(summary = "List transactions by date range")
    public ResponseEntity<Page<TransactionDto>> listByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        Page<TransactionDto> transactions = transactionService.findByDateRange(startDate, endDate, pageable).map(TransactionDto::from);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/card/{cardNumber}/date-range")
    @Operation(summary = "List transactions by card and date range")
    public ResponseEntity<List<TransactionDto>> listByCardAndDateRange(
            @PathVariable String cardNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<TransactionDto> transactions = transactionService.findByCardNumberAndDateRange(cardNumber, startDate, endDate).stream()
                .map(TransactionDto::from)
                .toList();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/unprocessed")
    @Operation(summary = "List unprocessed transactions")
    public ResponseEntity<Page<TransactionDto>> listUnprocessed(Pageable pageable) {
        Page<TransactionDto> transactions = transactionService.findUnprocessedTransactions(pageable).map(TransactionDto::from);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/process-all")
    @Operation(summary = "Process all pending transactions")
    public ResponseEntity<Integer> processAllPending() {
        int processed = transactionService.processAllPendingTransactions();
        return ResponseEntity.ok(processed);
    }

    @GetMapping("/merchant/{merchantId}")
    @Operation(summary = "List transactions by merchant ID")
    public ResponseEntity<List<TransactionDto>> listByMerchantId(@PathVariable Long merchantId) {
        List<TransactionDto> transactions = transactionService.findByMerchantId(merchantId).stream()
                .map(TransactionDto::from)
                .toList();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/large")
    @Operation(summary = "List large transactions above threshold")
    public ResponseEntity<List<TransactionDto>> listLargeTransactions(@RequestParam BigDecimal threshold) {
        List<TransactionDto> transactions = transactionService.findLargeTransactions(threshold).stream()
                .map(TransactionDto::from)
                .toList();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/card/{cardNumber}/sum")
    @Operation(summary = "Sum transactions by card and date range")
    public ResponseEntity<BigDecimal> sumByCardAndDateRange(
            @PathVariable String cardNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(transactionService.sumTransactionsByCardAndDateRange(cardNumber, startDate, endDate));
    }

    @GetMapping("/type/{typeCode}/count")
    @Operation(summary = "Count transactions by type and date range")
    public ResponseEntity<Long> countByTypeAndDateRange(
            @PathVariable String typeCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(transactionService.countByTypeCodeAndDateRange(typeCode, startDate, endDate));
    }

    @GetMapping("/category/{categoryCode}")
    @Operation(summary = "List transactions by category code")
    public ResponseEntity<List<TransactionDto>> listByCategoryCode(@PathVariable Integer categoryCode) {
        List<TransactionDto> transactions = transactionService.findByCategoryCode(categoryCode).stream()
                .map(TransactionDto::from)
                .toList();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get transaction summary by type")
    public ResponseEntity<List<Object[]>> getTransactionSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(transactionService.getTransactionSummaryByType(startDate, endDate));
    }
}

package com.carddemo.api.controller;

import com.carddemo.api.dto.AccountDto;
import com.carddemo.common.constant.AccountStatus;
import com.carddemo.core.account.entity.Account;
import com.carddemo.core.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for Account operations - replaces CICS transactions:
 * - CAVW (Account View) -> GET /api/accounts/{id}
 * - CAUP (Account Update) -> PUT /api/accounts/{id}
 * - CALI (Account List) -> GET /api/accounts
 */
@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Account management APIs")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @Operation(summary = "List all accounts", description = "Replaces CICS transaction CALI")
    public ResponseEntity<Page<AccountDto>> listAccounts(Pageable pageable) {
        Page<AccountDto> accounts = accountService.findAll(pageable).map(AccountDto::from);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get account by ID", description = "Replaces CICS transaction CAVW")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long accountId) {
        return accountService.findById(accountId)
                .map(AccountDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new account")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto accountDto) {
        Account account = accountDto.toEntity();
        Account created = accountService.create(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(AccountDto.from(created));
    }

    @PutMapping("/{accountId}")
    @Operation(summary = "Update account", description = "Replaces CICS transaction CAUP")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long accountId, @Valid @RequestBody AccountDto accountDto) {
        Account account = accountDto.toEntity();
        account.setAccountId(accountId);
        Account updated = accountService.update(account);
        return ResponseEntity.ok(AccountDto.from(updated));
    }

    @DeleteMapping("/{accountId}")
    @Operation(summary = "Delete account")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.delete(accountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "List accounts by status")
    public ResponseEntity<Page<AccountDto>> listAccountsByStatus(@PathVariable AccountStatus status, Pageable pageable) {
        Page<AccountDto> accounts = accountService.findByStatus(status, pageable).map(AccountDto::from);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/group/{groupId}")
    @Operation(summary = "List accounts by group ID")
    public ResponseEntity<List<AccountDto>> listAccountsByGroupId(@PathVariable String groupId) {
        List<AccountDto> accounts = accountService.findByGroupId(groupId).stream()
                .map(AccountDto::from)
                .toList();
        return ResponseEntity.ok(accounts);
    }

    @PatchMapping("/{accountId}/balance")
    @Operation(summary = "Update account balance")
    public ResponseEntity<AccountDto> updateBalance(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        Account updated = accountService.updateBalance(accountId, amount);
        return ResponseEntity.ok(AccountDto.from(updated));
    }

    @PatchMapping("/{accountId}/status")
    @Operation(summary = "Update account status")
    public ResponseEntity<AccountDto> updateStatus(@PathVariable Long accountId, @RequestParam AccountStatus status) {
        Account updated = accountService.updateStatus(accountId, status);
        return ResponseEntity.ok(AccountDto.from(updated));
    }

    @GetMapping("/near-limit")
    @Operation(summary = "List accounts near credit limit")
    public ResponseEntity<List<AccountDto>> listAccountsNearCreditLimit(@RequestParam(defaultValue = "90") int percentage) {
        List<AccountDto> accounts = accountService.findAccountsNearCreditLimit(percentage).stream()
                .map(AccountDto::from)
                .toList();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/expired-active")
    @Operation(summary = "List expired but active accounts")
    public ResponseEntity<List<AccountDto>> listExpiredActiveAccounts() {
        List<AccountDto> accounts = accountService.findExpiredActiveAccounts().stream()
                .map(AccountDto::from)
                .toList();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/total-balance")
    @Operation(summary = "Get total balance of all active accounts")
    public ResponseEntity<BigDecimal> getTotalActiveBalance() {
        return ResponseEntity.ok(accountService.getTotalActiveBalance());
    }

    @GetMapping("/count/{status}")
    @Operation(summary = "Count accounts by status")
    public ResponseEntity<Long> countByStatus(@PathVariable AccountStatus status) {
        return ResponseEntity.ok(accountService.countByStatus(status));
    }

    @PostMapping("/{accountId}/reset-cycle")
    @Operation(summary = "Reset cycle amounts for account")
    public ResponseEntity<Void> resetCycleAmounts(@PathVariable Long accountId) {
        accountService.resetCycleAmounts(accountId);
        return ResponseEntity.ok().build();
    }
}

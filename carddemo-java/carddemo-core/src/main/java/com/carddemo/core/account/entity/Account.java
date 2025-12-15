package com.carddemo.core.account.entity;

import com.carddemo.common.constant.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Account entity - migrated from COBOL copybook CVACT01Y.cpy.
 * Original COBOL record length: 300 bytes.
 * 
 * Field mappings:
 * - ACCT-ID (PIC 9(11)) -> accountId (Long)
 * - ACCT-ACTIVE-STATUS (PIC X) -> status (AccountStatus)
 * - ACCT-CURR-BAL (PIC S9(10)V99 COMP-3) -> currentBalance (BigDecimal)
 * - ACCT-CREDIT-LIMIT (PIC S9(10)V99 COMP-3) -> creditLimit (BigDecimal)
 * - ACCT-CASH-CREDIT-LIMIT (PIC S9(10)V99 COMP-3) -> cashCreditLimit (BigDecimal)
 * - ACCT-OPEN-DATE (PIC X(10)) -> openDate (LocalDate)
 * - ACCT-EXPIRATION-DATE (PIC X(10)) -> expirationDate (LocalDate)
 * - ACCT-REISSUE-DATE (PIC X(10)) -> reissueDate (LocalDate)
 * - ACCT-CURR-CYC-CREDIT (PIC S9(10)V99 COMP-3) -> currentCycleCredit (BigDecimal)
 * - ACCT-CURR-CYC-DEBIT (PIC S9(10)V99 COMP-3) -> currentCycleDebit (BigDecimal)
 * - ACCT-ADDR-ZIP (PIC X(10)) -> addressZip (String)
 * - ACCT-GROUP-ID (PIC X(10)) -> groupId (String)
 */
@Entity
@Table(name = "accounts", indexes = {
    @Index(name = "idx_account_status", columnList = "status"),
    @Index(name = "idx_account_group_id", columnList = "group_id")
})
public class Account {

    @Id
    @Column(name = "account_id")
    private Long accountId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    private AccountStatus status;

    @NotNull
    @Column(name = "current_balance", precision = 12, scale = 2)
    private BigDecimal currentBalance;

    @NotNull
    @Column(name = "credit_limit", precision = 12, scale = 2)
    private BigDecimal creditLimit;

    @NotNull
    @Column(name = "cash_credit_limit", precision = 12, scale = 2)
    private BigDecimal cashCreditLimit;

    @NotNull
    @Column(name = "open_date")
    private LocalDate openDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "reissue_date")
    private LocalDate reissueDate;

    @NotNull
    @Column(name = "current_cycle_credit", precision = 12, scale = 2)
    private BigDecimal currentCycleCredit;

    @NotNull
    @Column(name = "current_cycle_debit", precision = 12, scale = 2)
    private BigDecimal currentCycleDebit;

    @Size(max = 10)
    @Column(name = "address_zip", length = 10)
    private String addressZip;

    @Size(max = 10)
    @Column(name = "group_id", length = 10)
    private String groupId;

    @Version
    @Column(name = "version")
    private Long version;

    public Account() {
        this.currentBalance = BigDecimal.ZERO;
        this.creditLimit = BigDecimal.ZERO;
        this.cashCreditLimit = BigDecimal.ZERO;
        this.currentCycleCredit = BigDecimal.ZERO;
        this.currentCycleDebit = BigDecimal.ZERO;
        this.status = AccountStatus.ACTIVE;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCashCreditLimit() {
        return cashCreditLimit;
    }

    public void setCashCreditLimit(BigDecimal cashCreditLimit) {
        this.cashCreditLimit = cashCreditLimit;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDate getReissueDate() {
        return reissueDate;
    }

    public void setReissueDate(LocalDate reissueDate) {
        this.reissueDate = reissueDate;
    }

    public BigDecimal getCurrentCycleCredit() {
        return currentCycleCredit;
    }

    public void setCurrentCycleCredit(BigDecimal currentCycleCredit) {
        this.currentCycleCredit = currentCycleCredit;
    }

    public BigDecimal getCurrentCycleDebit() {
        return currentCycleDebit;
    }

    public void setCurrentCycleDebit(BigDecimal currentCycleDebit) {
        this.currentCycleDebit = currentCycleDebit;
    }

    public String getAddressZip() {
        return addressZip;
    }

    public void setAddressZip(String addressZip) {
        this.addressZip = addressZip;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal getAvailableCredit() {
        return creditLimit.subtract(currentBalance);
    }

    public BigDecimal getAvailableCashCredit() {
        return cashCreditLimit.subtract(currentBalance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", status=" + status +
                ", currentBalance=" + currentBalance +
                ", creditLimit=" + creditLimit +
                '}';
    }
}

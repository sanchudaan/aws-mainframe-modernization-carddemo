package com.carddemo.core.transaction.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Transaction entity - migrated from COBOL copybook CVTRA05Y.cpy.
 * Original COBOL record length: 350 bytes.
 * 
 * Field mappings:
 * - TRAN-ID (PIC X(16)) -> transactionId (String)
 * - TRAN-TYPE-CD (PIC X(2)) -> typeCode (String)
 * - TRAN-CAT-CD (PIC 9(4)) -> categoryCode (Integer)
 * - TRAN-SOURCE (PIC X(10)) -> source (String)
 * - TRAN-DESC (PIC X(100)) -> description (String)
 * - TRAN-AMT (PIC S9(9)V99 COMP-3) -> amount (BigDecimal)
 * - TRAN-MERCHANT-ID (PIC 9(9)) -> merchantId (Long)
 * - TRAN-MERCHANT-NAME (PIC X(50)) -> merchantName (String)
 * - TRAN-MERCHANT-CITY (PIC X(50)) -> merchantCity (String)
 * - TRAN-MERCHANT-ZIP (PIC X(10)) -> merchantZip (String)
 * - TRAN-CARD-NUM (PIC X(16)) -> cardNumber (String)
 * - TRAN-ORIG-TS (PIC X(26)) -> originTimestamp (LocalDateTime)
 * - TRAN-PROC-TS (PIC X(26)) -> processedTimestamp (LocalDateTime)
 */
@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_transaction_card", columnList = "card_number"),
    @Index(name = "idx_transaction_type", columnList = "type_code"),
    @Index(name = "idx_transaction_date", columnList = "origin_timestamp"),
    @Index(name = "idx_transaction_merchant", columnList = "merchant_id")
})
public class Transaction {

    @Id
    @Size(max = 16)
    @Column(name = "transaction_id", length = 16)
    private String transactionId;

    @NotNull
    @Size(max = 2)
    @Column(name = "type_code", length = 2)
    private String typeCode;

    @Column(name = "category_code")
    private Integer categoryCode;

    @Size(max = 10)
    @Column(name = "source", length = 10)
    private String source;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @NotNull
    @Column(name = "amount", precision = 11, scale = 2)
    private BigDecimal amount;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Size(max = 50)
    @Column(name = "merchant_name", length = 50)
    private String merchantName;

    @Size(max = 50)
    @Column(name = "merchant_city", length = 50)
    private String merchantCity;

    @Size(max = 10)
    @Column(name = "merchant_zip", length = 10)
    private String merchantZip;

    @NotNull
    @Size(max = 16)
    @Column(name = "card_number", length = 16)
    private String cardNumber;

    @NotNull
    @Column(name = "origin_timestamp")
    private LocalDateTime originTimestamp;

    @Column(name = "processed_timestamp")
    private LocalDateTime processedTimestamp;

    @Version
    @Column(name = "version")
    private Long version;

    public Transaction() {
        this.amount = BigDecimal.ZERO;
        this.originTimestamp = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Integer categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantCity() {
        return merchantCity;
    }

    public void setMerchantCity(String merchantCity) {
        this.merchantCity = merchantCity;
    }

    public String getMerchantZip() {
        return merchantZip;
    }

    public void setMerchantZip(String merchantZip) {
        this.merchantZip = merchantZip;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDateTime getOriginTimestamp() {
        return originTimestamp;
    }

    public void setOriginTimestamp(LocalDateTime originTimestamp) {
        this.originTimestamp = originTimestamp;
    }

    public LocalDateTime getProcessedTimestamp() {
        return processedTimestamp;
    }

    public void setProcessedTimestamp(LocalDateTime processedTimestamp) {
        this.processedTimestamp = processedTimestamp;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean isProcessed() {
        return processedTimestamp != null;
    }

    public boolean isCredit() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isDebit() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", typeCode='" + typeCode + '\'' +
                ", amount=" + amount +
                ", cardNumber='" + getMaskedCardNumber() + '\'' +
                '}';
    }
}

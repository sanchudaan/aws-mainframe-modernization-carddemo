package com.carddemo.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Transaction entity.
 * Used for REST API request/response payloads.
 */
public record TransactionDto(
        @Size(max = 16) String transactionId,
        @NotNull @Size(max = 2) String typeCode,
        Integer categoryCode,
        @Size(max = 10) String source,
        @Size(max = 100) String description,
        @NotNull BigDecimal amount,
        Long merchantId,
        @Size(max = 50) String merchantName,
        @Size(max = 50) String merchantCity,
        @Size(max = 10) String merchantZip,
        @NotNull @Size(max = 16) String cardNumber,
        String maskedCardNumber,
        LocalDateTime originTimestamp,
        LocalDateTime processedTimestamp,
        boolean isProcessed,
        boolean isCredit,
        boolean isDebit
) {
    public static TransactionDto from(com.carddemo.core.transaction.entity.Transaction transaction) {
        return new TransactionDto(
                transaction.getTransactionId(),
                transaction.getTypeCode(),
                transaction.getCategoryCode(),
                transaction.getSource(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getMerchantId(),
                transaction.getMerchantName(),
                transaction.getMerchantCity(),
                transaction.getMerchantZip(),
                transaction.getCardNumber(),
                transaction.getMaskedCardNumber(),
                transaction.getOriginTimestamp(),
                transaction.getProcessedTimestamp(),
                transaction.isProcessed(),
                transaction.isCredit(),
                transaction.isDebit()
        );
    }

    public com.carddemo.core.transaction.entity.Transaction toEntity() {
        com.carddemo.core.transaction.entity.Transaction transaction = new com.carddemo.core.transaction.entity.Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setTypeCode(typeCode);
        transaction.setCategoryCode(categoryCode);
        transaction.setSource(source);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setMerchantId(merchantId);
        transaction.setMerchantName(merchantName);
        transaction.setMerchantCity(merchantCity);
        transaction.setMerchantZip(merchantZip);
        transaction.setCardNumber(cardNumber);
        transaction.setOriginTimestamp(originTimestamp);
        transaction.setProcessedTimestamp(processedTimestamp);
        return transaction;
    }
}

package com.carddemo.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Data Transfer Object for Card entity.
 * Used for REST API request/response payloads.
 */
public record CardDto(
        @Size(max = 16) String cardNumber,
        @NotNull Long accountId,
        String maskedCardNumber,
        @Size(max = 50) String embossedName,
        LocalDate expirationDate,
        String activeStatus,
        boolean isActive,
        boolean isExpired
) {
    public static CardDto from(com.carddemo.core.card.entity.Card card) {
        return new CardDto(
                card.getCardNumber(),
                card.getAccountId(),
                card.getMaskedCardNumber(),
                card.getEmbossedName(),
                card.getExpirationDate(),
                card.getActiveStatus(),
                card.isActive(),
                card.isExpired()
        );
    }

    public com.carddemo.core.card.entity.Card toEntity() {
        com.carddemo.core.card.entity.Card card = new com.carddemo.core.card.entity.Card();
        card.setCardNumber(cardNumber);
        card.setAccountId(accountId);
        card.setEmbossedName(embossedName);
        card.setExpirationDate(expirationDate);
        card.setActiveStatus(activeStatus);
        return card;
    }
}

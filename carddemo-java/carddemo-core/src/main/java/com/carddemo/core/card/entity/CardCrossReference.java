package com.carddemo.core.card.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Card Cross Reference entity - migrated from COBOL copybook CVACT03Y.cpy.
 * Original VSAM file: CCXREF (Card Cross Reference File).
 * 
 * Field mappings:
 * - XREF-CARD-NUM (PIC X(16)) -> cardNumber (String)
 * - XREF-CUST-ID (PIC 9(9)) -> customerId (Long)
 * - XREF-ACCT-ID (PIC 9(11)) -> accountId (Long)
 */
@Entity
@Table(name = "card_cross_references", indexes = {
    @Index(name = "idx_xref_customer", columnList = "customer_id"),
    @Index(name = "idx_xref_account", columnList = "account_id")
})
public class CardCrossReference {

    @Id
    @Size(max = 16)
    @Column(name = "card_number", length = 16)
    private String cardNumber;

    @NotNull
    @Column(name = "customer_id")
    private Long customerId;

    @NotNull
    @Column(name = "account_id")
    private Long accountId;

    @Version
    @Column(name = "version")
    private Long version;

    public CardCrossReference() {
    }

    public CardCrossReference(String cardNumber, Long customerId, Long accountId) {
        this.cardNumber = cardNumber;
        this.customerId = customerId;
        this.accountId = accountId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardCrossReference that = (CardCrossReference) o;
        return Objects.equals(cardNumber, that.cardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber);
    }

    @Override
    public String toString() {
        return "CardCrossReference{" +
                "cardNumber='" + cardNumber + '\'' +
                ", customerId=" + customerId +
                ", accountId=" + accountId +
                '}';
    }
}

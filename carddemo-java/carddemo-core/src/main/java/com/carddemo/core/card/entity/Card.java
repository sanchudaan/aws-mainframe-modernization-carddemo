package com.carddemo.core.card.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Card entity - migrated from COBOL copybook CVACT02Y.cpy (Card Data).
 * Original COBOL record length: 150 bytes.
 * 
 * Field mappings:
 * - CARD-NUM (PIC X(16)) -> cardNumber (String)
 * - CARD-ACCT-ID (PIC 9(11)) -> accountId (Long)
 * - CARD-CVV-CD (PIC 9(3)) -> cvvCode (String)
 * - CARD-EMBOSSED-NAME (PIC X(50)) -> embossedName (String)
 * - CARD-EXPIRAION-DATE (PIC X(10)) -> expirationDate (LocalDate)
 * - CARD-ACTIVE-STATUS (PIC X) -> activeStatus (String)
 */
@Entity
@Table(name = "cards", indexes = {
    @Index(name = "idx_card_account_id", columnList = "account_id"),
    @Index(name = "idx_card_status", columnList = "active_status")
})
public class Card {

    @Id
    @Size(max = 16)
    @Column(name = "card_number", length = 16)
    private String cardNumber;

    @NotNull
    @Column(name = "account_id")
    private Long accountId;

    @Size(max = 3)
    @Column(name = "cvv_code", length = 3)
    private String cvvCode;

    @Size(max = 50)
    @Column(name = "embossed_name", length = 50)
    private String embossedName;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Size(max = 1)
    @Column(name = "active_status", length = 1)
    private String activeStatus;

    @Version
    @Column(name = "version")
    private Long version;

    public Card() {
        this.activeStatus = "Y";
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(String cvvCode) {
        this.cvvCode = cvvCode;
    }

    public String getEmbossedName() {
        return embossedName;
    }

    public void setEmbossedName(String embossedName) {
        this.embossedName = embossedName;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean isActive() {
        return "Y".equals(activeStatus);
    }

    public boolean isExpired() {
        return expirationDate != null && expirationDate.isBefore(LocalDate.now());
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
        Card card = (Card) o;
        return Objects.equals(cardNumber, card.cardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber);
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber='" + getMaskedCardNumber() + '\'' +
                ", accountId=" + accountId +
                ", activeStatus='" + activeStatus + '\'' +
                '}';
    }
}

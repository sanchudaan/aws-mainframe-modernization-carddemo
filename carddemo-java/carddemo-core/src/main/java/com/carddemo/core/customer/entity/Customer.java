package com.carddemo.core.customer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Customer entity - migrated from COBOL copybook CVCUS01Y.cpy.
 * Original COBOL record length: 500 bytes.
 * 
 * Field mappings:
 * - CUST-ID (PIC 9(9)) -> customerId (Long)
 * - CUST-FIRST-NAME (PIC X(25)) -> firstName (String)
 * - CUST-MIDDLE-NAME (PIC X(25)) -> middleName (String)
 * - CUST-LAST-NAME (PIC X(25)) -> lastName (String)
 * - CUST-ADDR-LINE-1 (PIC X(50)) -> addressLine1 (String)
 * - CUST-ADDR-LINE-2 (PIC X(50)) -> addressLine2 (String)
 * - CUST-ADDR-LINE-3 (PIC X(50)) -> addressLine3 (String)
 * - CUST-ADDR-STATE-CD (PIC X(2)) -> stateCode (String)
 * - CUST-ADDR-COUNTRY-CD (PIC X(3)) -> countryCode (String)
 * - CUST-ADDR-ZIP (PIC X(10)) -> zipCode (String)
 * - CUST-PHONE-NUM-1 (PIC X(15)) -> phoneNumber1 (String)
 * - CUST-PHONE-NUM-2 (PIC X(15)) -> phoneNumber2 (String)
 * - CUST-SSN (PIC 9(9)) -> ssn (String)
 * - CUST-GOVT-ISSUED-ID (PIC X(20)) -> governmentId (String)
 * - CUST-DOB-YYYY-MM-DD (PIC X(10)) -> dateOfBirth (LocalDate)
 * - CUST-EFT-ACCOUNT-ID (PIC X(10)) -> eftAccountId (String)
 * - CUST-PRI-CARD-HOLDER-IND (PIC X) -> primaryCardHolderIndicator (String)
 * - CUST-FICO-CREDIT-SCORE (PIC 9(3)) -> ficoCreditScore (Integer)
 */
@Entity
@Table(name = "customers", indexes = {
    @Index(name = "idx_customer_ssn", columnList = "ssn"),
    @Index(name = "idx_customer_last_name", columnList = "last_name"),
    @Index(name = "idx_customer_state", columnList = "state_code")
})
public class Customer {

    @Id
    @Column(name = "customer_id")
    private Long customerId;

    @NotNull
    @Size(max = 25)
    @Column(name = "first_name", length = 25)
    private String firstName;

    @Size(max = 25)
    @Column(name = "middle_name", length = 25)
    private String middleName;

    @NotNull
    @Size(max = 25)
    @Column(name = "last_name", length = 25)
    private String lastName;

    @Size(max = 50)
    @Column(name = "address_line_1", length = 50)
    private String addressLine1;

    @Size(max = 50)
    @Column(name = "address_line_2", length = 50)
    private String addressLine2;

    @Size(max = 50)
    @Column(name = "address_line_3", length = 50)
    private String addressLine3;

    @Size(max = 2)
    @Column(name = "state_code", length = 2)
    private String stateCode;

    @Size(max = 3)
    @Column(name = "country_code", length = 3)
    private String countryCode;

    @Size(max = 10)
    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Size(max = 15)
    @Column(name = "phone_number_1", length = 15)
    private String phoneNumber1;

    @Size(max = 15)
    @Column(name = "phone_number_2", length = 15)
    private String phoneNumber2;

    @Size(max = 9)
    @Column(name = "ssn", length = 9)
    private String ssn;

    @Size(max = 20)
    @Column(name = "government_id", length = 20)
    private String governmentId;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 10)
    @Column(name = "eft_account_id", length = 10)
    private String eftAccountId;

    @Size(max = 1)
    @Column(name = "primary_card_holder_indicator", length = 1)
    private String primaryCardHolderIndicator;

    @Column(name = "fico_credit_score")
    private Integer ficoCreditScore;

    @Version
    @Column(name = "version")
    private Long version;

    public Customer() {
        this.countryCode = "USA";
        this.primaryCardHolderIndicator = "Y";
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getGovernmentId() {
        return governmentId;
    }

    public void setGovernmentId(String governmentId) {
        this.governmentId = governmentId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEftAccountId() {
        return eftAccountId;
    }

    public void setEftAccountId(String eftAccountId) {
        this.eftAccountId = eftAccountId;
    }

    public String getPrimaryCardHolderIndicator() {
        return primaryCardHolderIndicator;
    }

    public void setPrimaryCardHolderIndicator(String primaryCardHolderIndicator) {
        this.primaryCardHolderIndicator = primaryCardHolderIndicator;
    }

    public Integer getFicoCreditScore() {
        return ficoCreditScore;
    }

    public void setFicoCreditScore(Integer ficoCreditScore) {
        this.ficoCreditScore = ficoCreditScore;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (firstName != null) {
            sb.append(firstName.trim());
        }
        if (middleName != null && !middleName.isBlank()) {
            sb.append(" ").append(middleName.trim());
        }
        if (lastName != null) {
            sb.append(" ").append(lastName.trim());
        }
        return sb.toString().trim();
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (addressLine1 != null && !addressLine1.isBlank()) {
            sb.append(addressLine1.trim());
        }
        if (addressLine2 != null && !addressLine2.isBlank()) {
            sb.append(", ").append(addressLine2.trim());
        }
        if (addressLine3 != null && !addressLine3.isBlank()) {
            sb.append(", ").append(addressLine3.trim());
        }
        if (stateCode != null) {
            sb.append(", ").append(stateCode);
        }
        if (zipCode != null) {
            sb.append(" ").append(zipCode);
        }
        if (countryCode != null) {
            sb.append(", ").append(countryCode);
        }
        return sb.toString();
    }

    public boolean isPrimaryCardHolder() {
        return "Y".equals(primaryCardHolderIndicator);
    }

    public String getMaskedSsn() {
        if (ssn == null || ssn.length() < 4) {
            return "***-**-****";
        }
        return "***-**-" + ssn.substring(ssn.length() - 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + getFullName() + '\'' +
                ", stateCode='" + stateCode + '\'' +
                '}';
    }
}

package com.carddemo.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Data Transfer Object for Customer entity.
 * Used for REST API request/response payloads.
 */
public record CustomerDto(
        Long customerId,
        @NotNull @Size(max = 25) String firstName,
        @Size(max = 25) String middleName,
        @NotNull @Size(max = 25) String lastName,
        String fullName,
        @Size(max = 50) String addressLine1,
        @Size(max = 50) String addressLine2,
        @Size(max = 50) String addressLine3,
        @Size(max = 2) String stateCode,
        @Size(max = 3) String countryCode,
        @Size(max = 10) String zipCode,
        String fullAddress,
        @Size(max = 15) String phoneNumber1,
        @Size(max = 15) String phoneNumber2,
        String maskedSsn,
        @Size(max = 20) String governmentId,
        LocalDate dateOfBirth,
        @Size(max = 10) String eftAccountId,
        boolean isPrimaryCardHolder,
        Integer ficoCreditScore
) {
    public static CustomerDto from(com.carddemo.core.customer.entity.Customer customer) {
        return new CustomerDto(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getMiddleName(),
                customer.getLastName(),
                customer.getFullName(),
                customer.getAddressLine1(),
                customer.getAddressLine2(),
                customer.getAddressLine3(),
                customer.getStateCode(),
                customer.getCountryCode(),
                customer.getZipCode(),
                customer.getFullAddress(),
                customer.getPhoneNumber1(),
                customer.getPhoneNumber2(),
                customer.getMaskedSsn(),
                customer.getGovernmentId(),
                customer.getDateOfBirth(),
                customer.getEftAccountId(),
                customer.isPrimaryCardHolder(),
                customer.getFicoCreditScore()
        );
    }

    public com.carddemo.core.customer.entity.Customer toEntity() {
        com.carddemo.core.customer.entity.Customer customer = new com.carddemo.core.customer.entity.Customer();
        customer.setCustomerId(customerId);
        customer.setFirstName(firstName);
        customer.setMiddleName(middleName);
        customer.setLastName(lastName);
        customer.setAddressLine1(addressLine1);
        customer.setAddressLine2(addressLine2);
        customer.setAddressLine3(addressLine3);
        customer.setStateCode(stateCode);
        customer.setCountryCode(countryCode);
        customer.setZipCode(zipCode);
        customer.setPhoneNumber1(phoneNumber1);
        customer.setPhoneNumber2(phoneNumber2);
        customer.setGovernmentId(governmentId);
        customer.setDateOfBirth(dateOfBirth);
        customer.setEftAccountId(eftAccountId);
        customer.setPrimaryCardHolderIndicator(isPrimaryCardHolder ? "Y" : "N");
        customer.setFicoCreditScore(ficoCreditScore);
        return customer;
    }
}

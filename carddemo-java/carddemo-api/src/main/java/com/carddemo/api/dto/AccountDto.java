package com.carddemo.api.dto;

import com.carddemo.common.constant.AccountStatus;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for Account entity.
 * Used for REST API request/response payloads.
 */
public record AccountDto(
        Long accountId,
        @NotNull AccountStatus status,
        @NotNull BigDecimal currentBalance,
        @NotNull BigDecimal creditLimit,
        @NotNull BigDecimal cashCreditLimit,
        @NotNull LocalDate openDate,
        LocalDate expirationDate,
        LocalDate reissueDate,
        BigDecimal currentCycleCredit,
        BigDecimal currentCycleDebit,
        String addressZip,
        String groupId,
        BigDecimal availableCredit,
        BigDecimal availableCashCredit
) {
    public static AccountDto from(com.carddemo.core.account.entity.Account account) {
        return new AccountDto(
                account.getAccountId(),
                account.getStatus(),
                account.getCurrentBalance(),
                account.getCreditLimit(),
                account.getCashCreditLimit(),
                account.getOpenDate(),
                account.getExpirationDate(),
                account.getReissueDate(),
                account.getCurrentCycleCredit(),
                account.getCurrentCycleDebit(),
                account.getAddressZip(),
                account.getGroupId(),
                account.getAvailableCredit(),
                account.getAvailableCashCredit()
        );
    }

    public com.carddemo.core.account.entity.Account toEntity() {
        com.carddemo.core.account.entity.Account account = new com.carddemo.core.account.entity.Account();
        account.setAccountId(accountId);
        account.setStatus(status);
        account.setCurrentBalance(currentBalance);
        account.setCreditLimit(creditLimit);
        account.setCashCreditLimit(cashCreditLimit);
        account.setOpenDate(openDate);
        account.setExpirationDate(expirationDate);
        account.setReissueDate(reissueDate);
        if (currentCycleCredit != null) {
            account.setCurrentCycleCredit(currentCycleCredit);
        }
        if (currentCycleDebit != null) {
            account.setCurrentCycleDebit(currentCycleDebit);
        }
        account.setAddressZip(addressZip);
        account.setGroupId(groupId);
        return account;
    }
}

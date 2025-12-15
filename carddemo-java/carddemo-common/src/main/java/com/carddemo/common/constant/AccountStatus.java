package com.carddemo.common.constant;

/**
 * Account status enumeration - replaces COBOL ACCT-ACTIVE-STATUS field.
 * Maps to: 'Y' = Active, 'N' = Inactive
 */
public enum AccountStatus {
    ACTIVE("Y"),
    INACTIVE("N");

    private final String code;

    AccountStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static AccountStatus fromCode(String code) {
        for (AccountStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown account status code: " + code);
    }
}

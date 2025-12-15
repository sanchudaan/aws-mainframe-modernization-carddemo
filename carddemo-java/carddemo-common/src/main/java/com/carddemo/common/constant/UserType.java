package com.carddemo.common.constant;

/**
 * User type enumeration - replaces COBOL 88-level conditions for CDEMO-USER-TYPE.
 * Maps to: 'A' = Admin, 'U' = User
 */
public enum UserType {
    ADMIN("A"),
    USER("U");

    private final String code;

    UserType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static UserType fromCode(String code) {
        for (UserType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown user type code: " + code);
    }
}

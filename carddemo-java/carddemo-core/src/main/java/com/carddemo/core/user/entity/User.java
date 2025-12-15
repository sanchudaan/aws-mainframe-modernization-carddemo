package com.carddemo.core.user.entity;

import com.carddemo.common.constant.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * User entity - migrated from COBOL copybook COUSR01Y.cpy (User Security).
 * Original VSAM file: USRSEC (User Security File).
 * 
 * Field mappings:
 * - SEC-USR-ID (PIC X(8)) -> userId (String)
 * - SEC-USR-FNAME (PIC X(20)) -> firstName (String)
 * - SEC-USR-LNAME (PIC X(20)) -> lastName (String)
 * - SEC-USR-PWD (PIC X(8)) -> password (String) - stored hashed
 * - SEC-USR-TYPE (PIC X) -> userType (UserType)
 * - SEC-USR-UPDTDT-YYYYMMDD (PIC 9(8)) -> lastUpdateDate (String)
 * - SEC-USR-UPDTTM-HHMMSS (PIC 9(6)) -> lastUpdateTime (String)
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_type", columnList = "user_type"),
    @Index(name = "idx_user_last_name", columnList = "last_name")
})
public class User {

    @Id
    @Size(max = 8)
    @Column(name = "user_id", length = 8)
    private String userId;

    @NotNull
    @Size(max = 20)
    @Column(name = "first_name", length = 20)
    private String firstName;

    @NotNull
    @Size(max = 20)
    @Column(name = "last_name", length = 20)
    private String lastName;

    @NotNull
    @Size(max = 100)
    @Column(name = "password", length = 100)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", length = 10)
    private UserType userType;

    @Size(max = 8)
    @Column(name = "last_update_date", length = 8)
    private String lastUpdateDate;

    @Size(max = 6)
    @Column(name = "last_update_time", length = 6)
    private String lastUpdateTime;

    @Version
    @Column(name = "version")
    private Long version;

    public User() {
        this.userType = UserType.USER;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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
        if (lastName != null) {
            sb.append(" ").append(lastName.trim());
        }
        return sb.toString().trim();
    }

    public boolean isAdmin() {
        return UserType.ADMIN.equals(userType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + getFullName() + '\'' +
                ", userType=" + userType +
                '}';
    }
}

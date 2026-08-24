package za.ac.cput.domain.auth;

import jakarta.persistence.*;
import za.ac.cput.domain.enums.UserType;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private int userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean revoked;

    protected RefreshToken() {
        // Required by JPA
    }

    private RefreshToken(Builder builder) {
        this.id = builder.id;
        this.token = builder.token;
        this.userId = builder.userId;
        this.userType = builder.userType;
        this.expiryDate = builder.expiryDate;
        this.revoked = builder.revoked;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return userId;
    }

    public UserType getUserType() {
        return userType;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                ", userType=" + userType +
                ", expiryDate=" + expiryDate +
                ", revoked=" + revoked +
                '}';
    }

    public static class Builder {
        private int id;
        private String token;
        private int userId;
        private UserType userType;
        private LocalDateTime expiryDate;
        private boolean revoked;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder setUserType(UserType userType) {
            this.userType = userType;
            return this;
        }

        public Builder setExpiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder setRevoked(boolean revoked) {
            this.revoked = revoked;
            return this;
        }

        public Builder copy(RefreshToken refreshToken) {
            this.id = refreshToken.id;
            this.token = refreshToken.token;
            this.userId = refreshToken.userId;
            this.userType = refreshToken.userType;
            this.expiryDate = refreshToken.expiryDate;
            this.revoked = refreshToken.revoked;
            return this;
        }

        public RefreshToken build() {
            return new RefreshToken(this);
        }
    }
}
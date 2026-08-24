package za.ac.cput.domain;

import jakarta.persistence.*;
import za.ac.cput.domain.enums.UserType;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "verification_token")
public class VerificationToken {

    //  Variables/Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tokenId;

    @Column(nullable = false, unique = true)
    private String token;

    // No JPA relationship possible — User is @MappedSuperclass, not a real table.
    // Store the userId + userType instead, and resolve it in the service layer.
    @Column(nullable = false)
    private int userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    //  Constructors
    protected VerificationToken(){}

    private VerificationToken(Builder builder){
        this.tokenId = builder.tokenId;
        this.token = builder.token;
        this.userId = builder.userId;
        this.userType = builder.userType;
        this.expiryDate = builder.expiryDate;
    }

    //  Getters
    public int getTokenId() {
        return tokenId;
    }
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    public int getUserId() {
        return userId;
    }

    public UserType getUserType() {
        return userType;
    }
    public String getToken() {
        return token;
    }
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }


    //  toString
    @Override
    public String toString() {
        return "VerificationToken{" +
                "tokenId=" + tokenId +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                ", userType=" + userType +
                ", expiryDate=" + expiryDate +
                '}';
    }

    //  Builder Class
    public static class Builder{
        private int tokenId;
        private String token;
        private int userId;
        private UserType userType;
        private LocalDateTime expiryDate;

        public Builder setTokenId(int tokenId) {
            this.tokenId = tokenId;
            return this;
        }

        public Builder setExpiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
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

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        //  Auto-generates a UUID token and sets 24hr expiry
        public Builder generateFor(int userId, UserType userType) {
            this.userId = userId;
            this.userType = userType;
            this.token = UUID.randomUUID().toString();
            this.expiryDate = LocalDateTime.now().plusHours(24);
            return this;
        }

        public Builder copy(VerificationToken vt) {
            this.tokenId = vt.tokenId;
            this.token = vt.token;
            this.userId = vt.userId;
            this.userType = vt.userType;
            this.expiryDate = vt.expiryDate;
            return this;
        }

        public VerificationToken build(){
            return new VerificationToken(this);
        }
    }
}

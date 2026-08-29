package za.ac.cput.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_request")
public class PasswordResetRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resetId;

    @Column(nullable = false)
    private String email;

    // Bcrypt hash of the 6-digit code — never store the raw code.
    @Column(nullable = false)
    private String codeHash;

    // Issued only after the code is successfully verified. Required by
    // resetPassword() as proof the verify step actually happened — stops
    // someone from skipping straight to reset-password with a guessed code.
    private String resetSessionToken;

    private boolean verified;
    private boolean used;
    private int attempts;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    protected PasswordResetRequest() {
        // Required by JPA
    }

    private PasswordResetRequest(Builder builder) {
        this.resetId = builder.resetId;
        this.email = builder.email;
        this.codeHash = builder.codeHash;
        this.resetSessionToken = builder.resetSessionToken;
        this.verified = builder.verified;
        this.used = builder.used;
        this.attempts = builder.attempts;
        this.expiryDate = builder.expiryDate;
        this.createdDate = builder.createdDate;
    }

    public int getResetId() { return resetId; }
    public String getEmail() { return email; }
    public String getCodeHash() { return codeHash; }
    public String getResetSessionToken() { return resetSessionToken; }
    public boolean isVerified() { return verified; }
    public boolean isUsed() { return used; }
    public int getAttempts() { return attempts; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public LocalDateTime getCreatedDate() { return createdDate; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    @Override
    public String toString() {
        return "PasswordResetRequest{" +
                "resetId=" + resetId +
                ", email='" + email + '\'' +
                ", verified=" + verified +
                ", used=" + used +
                ", attempts=" + attempts +
                ", expiryDate=" + expiryDate +
                '}';
    }

    public static class Builder {
        private int resetId;
        private String email;
        private String codeHash;
        private String resetSessionToken;
        private boolean verified;
        private boolean used;
        private int attempts;
        private LocalDateTime expiryDate;
        private LocalDateTime createdDate;

        public Builder setResetId(int resetId) { this.resetId = resetId; return this; }
        public Builder setEmail(String email) { this.email = email; return this; }
        public Builder setCodeHash(String codeHash) { this.codeHash = codeHash; return this; }
        public Builder setResetSessionToken(String resetSessionToken) { this.resetSessionToken = resetSessionToken; return this; }
        public Builder setVerified(boolean verified) { this.verified = verified; return this; }
        public Builder setUsed(boolean used) { this.used = used; return this; }
        public Builder setAttempts(int attempts) { this.attempts = attempts; return this; }
        public Builder setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; return this; }
        public Builder setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; return this; }

        public Builder copy(PasswordResetRequest r) {
            this.resetId = r.resetId;
            this.email = r.email;
            this.codeHash = r.codeHash;
            this.resetSessionToken = r.resetSessionToken;
            this.verified = r.verified;
            this.used = r.used;
            this.attempts = r.attempts;
            this.expiryDate = r.expiryDate;
            this.createdDate = r.createdDate;
            return this;
        }

        public PasswordResetRequest build() {
            return new PasswordResetRequest(this);
        }
    }
}
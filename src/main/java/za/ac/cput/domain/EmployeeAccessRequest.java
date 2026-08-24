package za.ac.cput.domain;

import jakarta.persistence.*;
import za.ac.cput.domain.enums.RequestStatus;
import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.domain.user.ClinicStaff;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_access_request")
public class EmployeeAccessRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType requestedUserType; // DOCTOR or CLINIC_STAFF only

    @Enumerated(EnumType.STRING)
    private StaffRole requestedStaffRole; // null for DOCTOR; NURSE for CLINIC_STAFF (never ADMIN)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    private LocalDateTime requestDate;
    private LocalDateTime processedDate;

    // ClinicStaff is a real @Entity (not @MappedSuperclass like User), so a
    // direct @ManyToOne here is valid — unlike Notification/VerificationToken,
    // which had to work around User being a @MappedSuperclass.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by")
    private ClinicStaff processedBy;

    private String adminNotes;

    protected EmployeeAccessRequest() {
        // Required by JPA
    }

    private EmployeeAccessRequest(Builder builder) {
        this.requestId = builder.requestId;
        this.email = builder.email;
        this.requestedUserType = builder.requestedUserType;
        this.requestedStaffRole = builder.requestedStaffRole;
        this.status = builder.status;
        this.requestDate = builder.requestDate;
        this.processedDate = builder.processedDate;
        this.processedBy = builder.processedBy;
        this.adminNotes = builder.adminNotes;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getEmail() {
        return email;
    }

    public UserType getRequestedUserType() {
        return requestedUserType;
    }

    public StaffRole getRequestedStaffRole() {
        return requestedStaffRole;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public LocalDateTime getProcessedDate() {
        return processedDate;
    }

    public ClinicStaff getProcessedBy() {
        return processedBy;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    @Override
    public String toString() {
        return "EmployeeAccessRequest{" +
                "requestId=" + requestId +
                ", email='" + email + '\'' +
                ", requestedUserType=" + requestedUserType +
                ", requestedStaffRole=" + requestedStaffRole +
                ", status=" + status +
                ", requestDate=" + requestDate +
                ", processedDate=" + processedDate +
                ", adminNotes='" + adminNotes + '\'' +
                '}';
    }

    public static class Builder {
        private int requestId;
        private String email;
        private UserType requestedUserType;
        private StaffRole requestedStaffRole;
        private RequestStatus status;
        private LocalDateTime requestDate;
        private LocalDateTime processedDate;
        private ClinicStaff processedBy;
        private String adminNotes;

        public Builder setRequestId(int requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setRequestedUserType(UserType requestedUserType) {
            this.requestedUserType = requestedUserType;
            return this;
        }

        public Builder setRequestedStaffRole(StaffRole requestedStaffRole) {
            this.requestedStaffRole = requestedStaffRole;
            return this;
        }

        public Builder setStatus(RequestStatus status) {
            this.status = status;
            return this;
        }

        public Builder setRequestDate(LocalDateTime requestDate) {
            this.requestDate = requestDate;
            return this;
        }

        public Builder setProcessedDate(LocalDateTime processedDate) {
            this.processedDate = processedDate;
            return this;
        }

        public Builder setProcessedBy(ClinicStaff processedBy) {
            this.processedBy = processedBy;
            return this;
        }

        public Builder setAdminNotes(String adminNotes) {
            this.adminNotes = adminNotes;
            return this;
        }

        public Builder copy(EmployeeAccessRequest request) {
            this.requestId = request.requestId;
            this.email = request.email;
            this.requestedUserType = request.requestedUserType;
            this.requestedStaffRole = request.requestedStaffRole;
            this.status = request.status;
            this.requestDate = request.requestDate;
            this.processedDate = request.processedDate;
            this.processedBy = request.processedBy;
            this.adminNotes = request.adminNotes;
            return this;
        }

        public EmployeeAccessRequest build() {
            return new EmployeeAccessRequest(this);
        }
    }
}
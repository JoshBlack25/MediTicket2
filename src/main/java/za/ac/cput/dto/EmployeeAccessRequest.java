package za.ac.cput.dto;

import za.ac.cput.domain.enums.RequestStatus;
import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserType;

import java.time.LocalDateTime;

public class EmployeeAccessRequest {

    private int requestId;
    private String email;
    private UserType requestedUserType;
    private StaffRole requestedStaffRole;
    private RequestStatus status;
    private LocalDateTime requestDate;
    private LocalDateTime processedDate;
    private String adminNotes;

    public EmployeeAccessRequest() {}

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getRequestedUserType() {
        return requestedUserType;
    }

    public void setRequestedUserType(UserType requestedUserType) {
        this.requestedUserType = requestedUserType;
    }

    public StaffRole getRequestedStaffRole() {
        return requestedStaffRole;
    }

    public void setRequestedStaffRole(StaffRole requestedStaffRole) {
        this.requestedStaffRole = requestedStaffRole;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDateTime getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(LocalDateTime processedDate) {
        this.processedDate = processedDate;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
}
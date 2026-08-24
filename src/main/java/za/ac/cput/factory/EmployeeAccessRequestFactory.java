package za.ac.cput.factory;

import za.ac.cput.domain.EmployeeAccessRequest;
import za.ac.cput.domain.enums.RequestStatus;
import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;

public class EmployeeAccessRequestFactory {

    public static EmployeeAccessRequest createEmployeeAccessRequest(
            int requestId,
            String email,
            UserType requestedUserType,
            StaffRole requestedStaffRole,
            RequestStatus status,
            LocalDateTime requestDate,
            LocalDateTime processedDate,
            ClinicStaff processedBy,
            String adminNotes) {

        if (Helper.isNullOrEmpty(email)) return null;
        if (!Helper.isValidObject(requestedUserType)) return null;
        if (!Helper.isValidObject(status)) return null;

        // Only DOCTOR or CLINIC_STAFF may be requested via this flow
        if (requestedUserType != UserType.DOCTOR && requestedUserType != UserType.CLINIC_STAFF) return null;

        // Doctors carry no staffRole; clinic staff requests must carry one
        if (requestedUserType == UserType.DOCTOR && requestedStaffRole != null) return null;
        if (requestedUserType == UserType.CLINIC_STAFF && requestedStaffRole == null) return null;

        // Self-service requests can never target ADMIN — that stays invite-only
        if (requestedStaffRole == StaffRole.ADMIN) return null;

        return new EmployeeAccessRequest.Builder()
                .setRequestId(requestId)
                .setEmail(email)
                .setRequestedUserType(requestedUserType)
                .setRequestedStaffRole(requestedStaffRole)
                .setStatus(status)
                .setRequestDate(requestDate)
                .setProcessedDate(processedDate)
                .setProcessedBy(processedBy)
                .setAdminNotes(adminNotes)
                .build();
    }

    // Convenience overload for the common case: a brand-new PENDING request,
    // not yet processed by anyone.
    public static EmployeeAccessRequest createEmployeeAccessRequest(
            String email, UserType requestedUserType, StaffRole requestedStaffRole) {

        return createEmployeeAccessRequest(
                0,
                email,
                requestedUserType,
                requestedStaffRole,
                RequestStatus.PENDING,
                LocalDateTime.now(),
                null,
                null,
                null
        );
    }
}
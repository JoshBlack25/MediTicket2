package za.ac.cput.service.impl;

import za.ac.cput.domain.enums.UserType;

// Not a CRUD entity service — no domain object/repository behind it,
// so it does NOT extend IService<T, ID>. Just defines the actions this service performs.
public interface IEmailService {

    void sendVerificationEmail(String toEmail, String firstName, String token);

    void sendEmployeeInviteEmail(String toEmail, UserType userType, String token);

    void sendPasswordResetEmail(String toEmail, String code);
}
package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.service.impl.IEmailService;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private final JavaMailSender mailSender;

    @Value("${app.verification.base-url}")
    private String verificationBaseUrl;

    @Value("${app.employee-invite.base-url}")
    private String employeeInviteBaseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String toEmail, String firstName, String token) {
        String verificationLink = verificationBaseUrl + "?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verify your Mediticket account");
        message.setText(
                "Hi " + firstName + ",\n\n" +
                        "Thanks for signing up to Mediticket. Please verify your email by clicking the link below:\n\n" +
                        verificationLink + "\n\n" +
                        "This link will expire in 24 hours.\n\n" +
                        "If you did not create this account, please ignore this email."
        );

        mailSender.send(message);
    }

    @Override
    public void sendEmployeeInviteEmail(String toEmail, UserType userType, String token) {
        String role = userType == UserType.DOCTOR ? "doctor" : "clinic staff";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("You're invited to join MediTicket as a " + role);
        message.setText(
                "Hello,\n\n" +
                        "You have been invited to register as a " + role + " on MediTicket.\n\n" +
                        "To complete your signup, open the MediTicket desktop app, choose " +
                        "\"Already have an invite code?\" on the access request screen, and paste in the code below:\n\n" +
                        token + "\n\n" +
                        "This code will expire in 48 hours.\n\n" +
                        "If you did not expect this invitation, please ignore this email."
        );

        mailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("MediTicket Password Reset");
        message.setText(
                "Hello,\n\n" +
                        "Your password reset code is:\n\n" +
                        code + "\n\n" +
                        "This code will expire in 10 minutes.\n\n" +
                        "If you did not request a password reset, you can safely ignore this email."
        );
        mailSender.send(message);
    }
}
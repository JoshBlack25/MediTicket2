package za.ac.cput.service.impl;

public interface IPasswordResetService {

    // Always "succeeds" from the caller's perspective for enumeration safety —
    // returns true only to signal the operation ran, not that the email exists.
    // The controller uses this return value purely for logging/testing; the
    // HTTP response wording stays identical either way.
    boolean requestReset(String email);

    // Returns the reset session token on success, null on failure (wrong
    // code, expired, too many attempts, or no pending request).
    String verifyCode(String email, String code);

    // Requires the resetSessionToken issued by verifyCode(). Returns true
    // only if that token is valid, unused, unexpired, and tied to this email.
    boolean resetPassword(String email, String resetSessionToken, String newPassword);
}
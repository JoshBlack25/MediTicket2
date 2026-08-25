package za.ac.cput.dto;

public class ResetPasswordRequest {
    private String email;
    private String resetSessionToken;
    private String newPassword;
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getResetSessionToken() { return resetSessionToken; }
    public void setResetSessionToken(String resetSessionToken) { this.resetSessionToken = resetSessionToken; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
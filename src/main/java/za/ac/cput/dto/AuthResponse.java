package za.ac.cput.dto;

public class AuthResponse {

    private String accessToken;
    private String refreshToken;

    protected AuthResponse() {
        // Required for JSON serialization
    }

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
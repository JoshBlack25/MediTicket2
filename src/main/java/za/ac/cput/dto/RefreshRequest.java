package za.ac.cput.dto;

public class RefreshRequest {

    private String refreshToken;

    protected RefreshRequest() {
        // Required for JSON deserialization
    }

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
package za.ac.cput.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.enums.UserType;
import za.ac.cput.dto.EmployeeInviteResponse;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtService {

    private static final String INVITE_PURPOSE = "EMPLOYEE_INVITE";

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Value("${app.jwt.invite-expiration-ms}")
    private long inviteExpirationMs;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    //  Generates a JWT for a given user, embedding their id and role (userType)
    public String generateToken(int userId, String userType){
        return generateToken(userId, userType, null);
    }

    //  Overload for ClinicStaff logins — embeds staffRole (NURSE/ADMIN) so
    //  JwtAuthFilter can grant a ROLE_ADMIN authority without a DB lookup on
    //  every request. Pass null staffRole for Patient/Doctor tokens.
    public String generateToken(int userId, String userType, String staffRole){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userType", userType)
                .issuedAt(now)
                .expiration(expiry);

        if (staffRole != null) {
            builder.claim("staffRole", staffRole);
        }

        return builder.signWith(getSigningKey()).compact();
    }

    public int extractUserId(String token){
        String subject = extractClaim(token, Claims::getSubject);
        return Integer.parseInt(subject);
    }

    public String extractUserType(String token){
        return extractClaim(token, claims -> claims.get("userType", String.class));
    }

    //  Returns null if the token has no staffRole claim (Patient/Doctor tokens,
    //  or any token issued before this field existed)
    public String extractStaffRole(String token){
        return extractClaim(token, claims -> claims.get("staffRole", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    //  Generates a JWT that carries an email + intended role (and staffRole,
    //  for clinic staff invites), used for the Doctor/ClinicStaff signup-invite
    //  link. No userId exists yet, so email is the subject instead. The
    //  "purpose" claim stops this being usable as a normal access token, and
    //  vice versa. Pass null staffRole for DOCTOR invites.
    public String generateInviteToken(String email, UserType userType, StaffRole staffRole) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + inviteExpirationMs);

        JwtBuilder builder = Jwts.builder()
                .subject(email)
                .claim("userType", userType.name())
                .claim("purpose", INVITE_PURPOSE)
                .issuedAt(now)
                .expiration(expiry);

        if (staffRole != null) {
            builder.claim("staffRole", staffRole.name());
        }

        return builder.signWith(getSigningKey()).compact();
    }

    //  Parses and validates an invite token, returning the embedded email/role.
    //  Returns null on any failure — expired, tampered, wrong purpose, or an
    //  unrecognised userType/staffRole — so callers just need a single null check.
    public EmployeeInviteResponse parseInviteToken(String token) {
        try {
            Claims claims = extractAllClaims(token);

            String purpose = claims.get("purpose", String.class);
            if (!INVITE_PURPOSE.equals(purpose)) {
                return null;
            }

            String email = claims.getSubject();
            UserType userType = UserType.valueOf(claims.get("userType", String.class));

            String staffRoleClaim = claims.get("staffRole", String.class);
            StaffRole staffRole = staffRoleClaim != null ? StaffRole.valueOf(staffRoleClaim) : null;

            return new EmployeeInviteResponse(email, userType, staffRole);

        } catch (JwtException | IllegalArgumentException e) {
            // JwtException covers expired/malformed/bad-signature tokens;
            // IllegalArgumentException covers UserType.valueOf()/StaffRole.valueOf() on a bad value
            return null;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
package za.ac.cput.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // TEMPORARY DEBUGGING
        System.out.println(">>> SECURITY REQUEST: "
                + request.getMethod() + " "
                + request.getRequestURI());

        System.out.println(">>> AUTH HEADER PRESENT: "
                + (authHeader != null));

        // No token, or not a Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println(">>> NO BEARER TOKEN");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (jwtService.isTokenValid(token)) {

            System.out.println(">>> JWT IS VALID");

            int userId = jwtService.extractUserId(token);
            String userType = jwtService.extractUserType(token);
            String staffRole = jwtService.extractStaffRole(token);

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userType));

            if (staffRole != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + staffRole));
            }

            var authToken = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);

            System.out.println(">>> AUTHENTICATION CREATED: "
                    + authToken);
        } else {
            System.out.println(">>> JWT IS INVALID");
        }

        System.out.println(">>> BEFORE FILTER CHAIN AUTHENTICATION: "
                + SecurityContextHolder.getContext().getAuthentication());

        filterChain.doFilter(request, response);
    }
}
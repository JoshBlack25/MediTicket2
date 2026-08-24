package za.ac.cput.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import za.ac.cput.security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("Not used — JWT auth only");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Restricted: ADMIN only. Must come BEFORE the general
                        // /api/auth/** permitAll below — Spring Security uses
                        // the first matching rule.
                        .requestMatchers("/api/auth/employee/invite").hasRole("ADMIN")
                        .requestMatchers("/api/auth/employee/access-requests/**").hasRole("ADMIN")

                        // Public: no account exists yet at this point in each flow
                        .requestMatchers(
                                "/api/auth/signup",
                                "/api/auth/verify",
                                "/api/auth/login",
                                "/api/auth/refresh",
                                "/api/auth/resend-verification",
                                "/api/auth/change-password",
                                "/api/auth/bootstrap/admin",
                                "/api/auth/employee/invite/verify",
                                "/api/auth/employee/signup/**",
                                "/api/auth/employee/request-access",
                                "/api/auth/forgot-password",
                                "/api/auth/verify-reset-code",
                                "/api/auth/reset-password"
                        ).permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
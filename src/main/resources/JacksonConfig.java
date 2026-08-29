package za.ac.cput.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * All domain classes (User, Patient, Appointment, Payment, Name, etc.) use
 * the Builder pattern with no public setters and a protected/private
 * constructor. Jackson can't deserialize incoming JSON (@RequestBody) into
 * these classes by default, since it normally relies on setters or a
 * public no-arg constructor + setters.
 *
 * This tells Jackson to read/write private fields directly via reflection
 * instead, so @RequestBody deserialization works app-wide without adding
 * setters to every domain class or breaking the existing Builder pattern
 * (which is still used everywhere for constructing objects in code).
 *
 * Uses the Jackson 3 / Spring Boot 4 API — this project is on Spring Boot
 * 4.1.0, which replaced Jackson 2's ObjectMapper.setVisibility(accessor,
 * level) with a VisibilityChecker-based approach instead.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer jacksonFieldVisibilityCustomizer() {
        return builder -> builder.changeDefaultVisibility(
                visibilityChecker -> visibilityChecker.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
        );
    }
}
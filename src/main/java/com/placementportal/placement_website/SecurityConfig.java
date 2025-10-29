package com.placementportal.placement_website;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // ✅ Allow schedule and other student APIs
                .requestMatchers("/student/**").permitAll()
                // ✅ Allow static files (HTML, CSS, JS)
                .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/images/**").permitAll()
                // ✅ Everything else can still be open for now
                .anyRequest().permitAll()
            )
            // Disable Spring login and CSRF for testing
            .csrf(csrf -> csrf.disable())
            .formLogin(login -> login.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}

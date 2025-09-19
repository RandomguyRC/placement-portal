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
                .anyRequest().permitAll() // allow all pages without login
            )
            .csrf(csrf -> csrf.disable()) // disable CSRF for now
            .formLogin(login -> login.disable()) // disable spring login page
            .httpBasic(basic -> basic.disable()); // disable basic auth

        return http.build();
    }
}

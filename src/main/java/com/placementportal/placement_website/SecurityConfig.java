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






// package com.placementportal.placement_website;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
//                 .requestMatchers("/login/student").permitAll()  // allow student login POST
//                 .anyRequest().authenticated()
//             )
//             .formLogin(form -> form
//                 .loginPage("/login")                  // your custom login page
//                 .loginProcessingUrl("/login/student") // this matches your form action
//                 .defaultSuccessUrl("/opportunities", true)
//                 .failureUrl("/login?error=true")
//                 .permitAll()
//             )
//             .logout(logout -> logout
//                 .logoutUrl("/logout")
//                 .logoutSuccessUrl("/login?logout=true")
//                 .invalidateHttpSession(true)
//                 .deleteCookies("JSESSIONID")
//                 .permitAll()
//             )
//             // ✅ Disable CSRF only for /applications/apply so Apply form doesn’t cause logout
//             .csrf(csrf -> csrf
//                 .ignoringRequestMatchers("/applications/apply")
//             );

//         return http.build();
//     }
// }

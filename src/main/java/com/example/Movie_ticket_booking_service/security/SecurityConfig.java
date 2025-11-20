package com.example.Movie_ticket_booking_service.security;

import com.example.Movie_ticket_booking_service.enums.PredefinedRoles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/auth/register").permitAll()
                            .requestMatchers("/auth/login").permitAll()
                            .requestMatchers("/movie/**").permitAll()
                            .requestMatchers("/seat/**").permitAll()
                            .requestMatchers(("/showtime/**")).permitAll()
                            .requestMatchers(("/cinema/**")).permitAll()
                            .requestMatchers(POST,("/invoice/**")).hasAnyRole(PredefinedRoles.USER,PredefinedRoles.STAFF)
                            .requestMatchers(GET,("/invoice/**")).hasAnyRole(PredefinedRoles.USER)
                            .requestMatchers(POST,("/chatbot/chat")).hasRole(PredefinedRoles.USER)
                            .requestMatchers(POST,("/user/enable-2fa")).hasRole(PredefinedRoles.USER)
                            .requestMatchers(POST, "/user/verify-code-2fa").hasRole(PredefinedRoles.USER)
                            .requestMatchers("/vnpay/**").hasAnyRole(PredefinedRoles.USER,PredefinedRoles.STAFF)
                            .requestMatchers(POST, "/auth/logout").hasAnyRole(PredefinedRoles.USER,PredefinedRoles.STAFF, PredefinedRoles.ADMIN)
                            .requestMatchers(GET,("/user/info/**")).hasAnyRole(PredefinedRoles.USER,PredefinedRoles.STAFF, PredefinedRoles.ADMIN)
                            .requestMatchers(PUT,("/user/info/**")).hasAnyRole(PredefinedRoles.USER,PredefinedRoles.STAFF, PredefinedRoles.ADMIN)
                            .requestMatchers("/admin/**").hasRole(PredefinedRoles.ADMIN)
                            .requestMatchers("/staff/**").hasRole(PredefinedRoles.STAFF)
                            .anyRequest()
                            .authenticated();
                });
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}

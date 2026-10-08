package com.samana.config;

import com.samana.jwt.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
public class SecurityConfig {


    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    // =====================================================
    // PASSWORD ENCODER
    // =====================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }


    // =====================================================
    // AUTHENTICATION MANAGER
    // =====================================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();

    }


    // =====================================================
    // SECURITY FILTER CHAIN
    // =====================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {


        http

            // =================================================
            // CORS
            // =================================================

            .cors(cors ->
                cors.configurationSource(
                    corsConfigurationSource()
                )
            )


            // =================================================
            // CSRF
            // =================================================

            .csrf(csrf ->
                csrf.disable()
            )


            // =================================================
            // SESSION MANAGEMENT
            // =================================================

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )


            // =================================================
            // AUTHORIZATION
            // =================================================

            .authorizeHttpRequests(auth -> auth

                // =================================================
                // PUBLIC UPLOADED IMAGES / VIDEOS
                // =================================================

                .requestMatchers(
                    "/uploads/**"
                ).permitAll()


                // =================================================
                // PUBLIC LOGIN & REGISTRATION
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/users/login",
                    "/api/users/register"
                ).permitAll()


                // =================================================
                // PUBLIC CONTACT FORM
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/contact"
                ).permitAll()


                // =================================================
                // CORS PREFLIGHT
                // =================================================

                .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                ).permitAll()


                // =================================================
                // AUTH APIs
                // =================================================

                .requestMatchers(
                    "/api/auth/**"
                ).permitAll()


                // =================================================
                // PUBLIC GET APIs
                // =================================================

                .requestMatchers(
        HttpMethod.GET,
        "/api/categories/**",
        "/api/experiences/**",
        "/api/locations/**"
).permitAll()

.requestMatchers(
        HttpMethod.GET,
        "/api/hosts/profile/**"
).permitAll()

.requestMatchers(
        HttpMethod.GET,
        "/api/hosts/me"
).hasRole("HOST")


                // =================================================
                // USER BOOKING APIs
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/bookings"
                ).hasRole("USER")


                .requestMatchers(
                    HttpMethod.GET,
                    "/api/bookings/my"
                ).hasRole("USER")


                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/bookings/*/cancel"
                ).hasRole("USER")


                // =================================================
                // ADMIN BOOKING APIs
                // =================================================

                .requestMatchers(
                    "/api/bookings/admin/**"
                ).hasRole("ADMIN")


                // =================================================
                // HOST APIs
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/hosts/become"
                ).hasAnyRole("USER", "HOST")


                .requestMatchers(
                    HttpMethod.POST,
                    "/api/hosts/profile"
                ).hasRole("HOST")


                .requestMatchers(
                    HttpMethod.POST,
                    "/api/hosts/profile/media"
                ).hasRole("HOST")


                .requestMatchers(
                    HttpMethod.POST,
                    "/api/hosts"
                ).hasRole("HOST")


                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/hosts/**"
                ).hasRole("HOST")


                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/hosts/**"
                ).hasRole("HOST")


                // =================================================
                // EXPERIENCE APIs
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/experiences"
                ).hasRole("HOST")


                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/experiences/**"
                ).hasRole("HOST")


                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/experiences/**"
                ).hasRole("HOST")


                // =================================================
                // ADMIN APIs
                // =================================================

                .requestMatchers(
                    "/api/admin/**"
                ).hasRole("ADMIN")


                .requestMatchers(
                    "/api/payments/admin/**"
                ).hasRole("ADMIN")


                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/payments/*/status"
                ).hasRole("ADMIN")


                // =================================================
                // EVERYTHING ELSE
                // =================================================

                .anyRequest().authenticated()

            )


            // =================================================
            // JWT FILTER
            // =================================================

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );


        return http.build();

    }


    // =====================================================
    // CORS CONFIGURATION
    // =====================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();


        configuration.setAllowedOrigins(
            List.of(
                "http://127.0.0.1:3000",
                "http://localhost:3000",
                "http://127.0.0.1:5500",
                "http://localhost:5500",
                "https://samana-frontend-82716904797.asia-south1.run.app"
            )
        );


        configuration.setAllowedMethods(
            List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
            )
        );


        configuration.setAllowedHeaders(
            List.of(
                "*"
            )
        );


        configuration.setAllowCredentials(
            true
        );


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration(
            "/**",
            configuration
        );


        return source;

    }

}
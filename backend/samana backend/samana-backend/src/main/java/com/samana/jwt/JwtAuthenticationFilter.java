package com.samana.jwt;

import com.samana.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        String authHeader =
                request.getHeader("Authorization");

        String token = null;
        String username = null;


        // =====================================================
        // GET TOKEN FROM HEADER
        // =====================================================

        if (authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            try {

                username =
                        jwtUtil.extractUsername(token);

            } catch (Exception e) {

                System.out.println(
                        "Invalid JWT: " + e.getMessage()
                );
            }
        }


        // =====================================================
        // AUTHENTICATE USER
        // =====================================================

        if (username != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            try {

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(username);


                if (jwtUtil.validateToken(
                        token,
                        userDetails)) {


                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );


                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );


                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );


                    // =================================================
                    // DEBUG
                    // =================================================

                    System.out.println(
                            "========== JWT SUCCESS =========="
                    );

                    System.out.println(
                            "USERNAME: " + username
                    );

                    System.out.println(
                            "AUTHORITIES: " +
                            userDetails.getAuthorities()
                    );

                    System.out.println(
                            "METHOD: " +
                            request.getMethod()
                    );

                    System.out.println(
                            "URL: " +
                            request.getRequestURI()
                    );

                    System.out.println(
                            "================================"
                    );

                } else {

                    System.out.println(
                            "JWT TOKEN IS INVALID OR EXPIRED"
                    );
                }

            } catch (Exception e) {

                System.out.println(
                        "JWT authentication error: "
                        + e.getMessage()
                );

                SecurityContextHolder
                        .clearContext();
            }
        }


        // =====================================================
        // CONTINUE FILTER
        // =====================================================

        filterChain.doFilter(
                request,
                response
        );
    }
}
package com.samana.controller;

import com.samana.dto.LoginRequest;
import com.samana.dto.LoginResponse;
import com.samana.dto.RegisterRequest;
import com.samana.dto.UpdateUserRequest;
import com.samana.dto.UserResponse;
import com.samana.entity.User;
import com.samana.service.UserDetailsService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/register")
    public UserResponse register(
            @Valid @RequestBody RegisterRequest request) {

        User user = userDetailsService.registerUser(request);

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        User user = userDetailsService.getUserByEmail(
                request.getEmail()
        );

        if (user == null) {
            throw new RuntimeException(
                    "Invalid Email or Password"
            );
        }

        String token = userDetailsService.login(
                request.getEmail(),
                request.getPassword()
        );

        if (token == null) {
            throw new RuntimeException(
                    "Invalid Email or Password"
            );
        }

        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {

        return userDetailsService.getAllUsers()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(
            @PathVariable Long id) {

        User user = userDetailsService.getUserById(id);

        if (user == null) {
            throw new RuntimeException(
                    "User not found"
            );
        }

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request,
            Authentication authentication) {

        User user = userDetailsService.updateUser(
                id,
                request,
                authentication
        );

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    @DeleteMapping("/{id}")
    public String deleteUser(
            @PathVariable Long id) {

        return userDetailsService.deleteUser(id);
    }
}
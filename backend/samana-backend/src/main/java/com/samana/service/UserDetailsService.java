package com.samana.service;

import com.samana.dto.RegisterRequest;
import com.samana.dto.UpdateUserRequest;
import com.samana.entity.HostProfile;
import com.samana.entity.User;
import com.samana.enums.Role;
import com.samana.jwt.JwtUtil;
import com.samana.repository.HostProfileRepository;
import com.samana.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HostProfileRepository hostProfileRepository;


    // =========================================================
    // REGISTER USER
    // =========================================================

    public User registerUser(RegisterRequest request) {

        User user = new User();

        user.setName(
                request.getName()
        );

        user.setEmail(
                request.getEmail()
        );

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // -----------------------------------------------------
        // NEW USERS ARE CURRENTLY REGISTERED AS USER
        // -----------------------------------------------------

        user.setRole(
                Role.USER
        );


        // -----------------------------------------------------
        // SAVE USER FIRST
        // -----------------------------------------------------

        user =
                userRepository.save(
                        user
                );


        // -----------------------------------------------------
        // HOST PROFILE
        //
        // Your current registration system does not yet have
        // a HOST registration option.
        //
        // Therefore we do NOT create a HostProfile here yet.
        //
        // We will create it automatically when a USER
        // becomes a HOST.
        // -----------------------------------------------------


        return user;
    }


    // =========================================================
    // LOGIN
    // =========================================================

    public String login(
            String email,
            String password
    ) {

        Optional<User> optionalUser =
                userRepository.findByEmail(
                        email
                );

        if (optionalUser.isPresent()) {

            User user =
                    optionalUser.get();

            if (
                    passwordEncoder.matches(
                            password,
                            user.getPassword()
                    )
            ) {

                return jwtUtil.generateToken(
                        user.getEmail()
                );
            }
        }

        return null;
    }


    // =========================================================
    // GET USER BY EMAIL
    // =========================================================

    public User getUserByEmail(
            String email
    ) {

        return userRepository
                .findByEmail(email)
                .orElse(null);
    }


    // =========================================================
    // GET ALL USERS
    // =========================================================

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }


    // =========================================================
    // GET USER BY ID
    // =========================================================

    public User getUserById(
            Long id
    ) {

        return userRepository
                .findById(id)
                .orElse(null);
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    public User updateUser(
            Long id,
            UpdateUserRequest request,
            Authentication authentication
    ) {

        User existingUser =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );


        String loggedInEmail =
                authentication.getName();


        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );


        boolean isOwner =
                existingUser.getEmail()
                        .equals(loggedInEmail);


        if (!isAdmin && !isOwner) {

            throw new RuntimeException(
                    "You are not allowed to update this user"
            );
        }


        existingUser.setName(
                request.getName()
        );

        existingUser.setEmail(
                request.getEmail()
        );


        return userRepository.save(
                existingUser
        );
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    public String deleteUser(
            Long id
    ) {

        userRepository.deleteById(
                id
        );

        return "User Deleted Successfully";
    }


    // =========================================================
    // GENERATE PASSWORD HASH
    // =========================================================

    public String generatePasswordHash(
            String password
    ) {

        return passwordEncoder.encode(
                password
        );
    }
}
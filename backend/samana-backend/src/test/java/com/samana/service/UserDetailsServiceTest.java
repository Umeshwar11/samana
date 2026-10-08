package com.samana.service;

import com.samana.dto.RegisterRequest;
import com.samana.dto.UpdateUserRequest;
import com.samana.entity.User;
import com.samana.enums.Role;
import com.samana.jwt.JwtUtil;
import com.samana.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserDetailsService userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Karan");
        user.setEmail("karan@gmail.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);
    }

    // =========================================================
    // 1. generatePasswordHash()
    // =========================================================

    @Test
    void testGeneratePasswordHash() {

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        String result =
                userDetailsService.generatePasswordHash("password123");

        assertEquals("encodedPassword", result);

        verify(passwordEncoder)
                .encode("password123");
    }

    // =========================================================
    // 2. login() - correct credentials
    // =========================================================

    @Test
    void testLoginWithCorrectCredentials() {

        when(userRepository.findByEmail("karan@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "password123",
                "encodedPassword"))
                .thenReturn(true);

        when(jwtUtil.generateToken("karan@gmail.com"))
                .thenReturn("jwt-token");

        String result =
                userDetailsService.login(
                        "karan@gmail.com",
                        "password123");

        assertEquals("jwt-token", result);

        verify(userRepository)
                .findByEmail("karan@gmail.com");

        verify(passwordEncoder)
                .matches("password123", "encodedPassword");

        verify(jwtUtil)
                .generateToken("karan@gmail.com");
    }

    // =========================================================
    // 3. login() - wrong password
    // =========================================================

    @Test
    void testLoginWithWrongPassword() {

        when(userRepository.findByEmail("karan@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrongPassword",
                "encodedPassword"))
                .thenReturn(false);

        String result =
                userDetailsService.login(
                        "karan@gmail.com",
                        "wrongPassword");

        assertNull(result);

        verify(jwtUtil, never())
                .generateToken(anyString());
    }

    // =========================================================
    // 4. login() - unknown email
    // =========================================================

    @Test
    void testLoginWithUnknownEmail() {

        when(userRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        String result =
                userDetailsService.login(
                        "unknown@gmail.com",
                        "password123");

        assertNull(result);

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtUtil, never())
                .generateToken(anyString());
    }

    // =========================================================
    // 5. getUserByEmail() - user exists
    // =========================================================

    @Test
    void testGetUserByEmailWhenUserExists() {

        when(userRepository.findByEmail("karan@gmail.com"))
                .thenReturn(Optional.of(user));

        User result =
                userDetailsService.getUserByEmail(
                        "karan@gmail.com");

        assertNotNull(result);
        assertEquals("karan@gmail.com", result.getEmail());
        assertEquals("Karan", result.getName());

        verify(userRepository)
                .findByEmail("karan@gmail.com");
    }

    // =========================================================
    // 6. getUserByEmail() - user does not exist
    // =========================================================

    @Test
    void testGetUserByEmailWhenUserDoesNotExist() {

        when(userRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        User result =
                userDetailsService.getUserByEmail(
                        "unknown@gmail.com");

        assertNull(result);
    }

    // =========================================================
    // 7. getUserById() - user exists
    // =========================================================

    @Test
    void testGetUserByIdWhenUserExists() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result =
                userDetailsService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("karan@gmail.com", result.getEmail());

        verify(userRepository)
                .findById(1L);
    }

    // =========================================================
    // 8. getUserById() - user does not exist
    // =========================================================

    @Test
    void testGetUserByIdWhenUserDoesNotExist() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        User result =
                userDetailsService.getUserById(999L);

        assertNull(result);
    }

    // =========================================================
    // 9. registerUser()
    // =========================================================

    @Test
    void testRegisterUser() {

        RegisterRequest request = new RegisterRequest();

        request.setName("New User");
        request.setEmail("newuser@gmail.com");
        request.setPassword("password123");

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result =
                userDetailsService.registerUser(request);

        assertNotNull(result);

        assertEquals("New User", result.getName());
        assertEquals("newuser@gmail.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(Role.USER, result.getRole());

        verify(passwordEncoder)
                .encode("password123");

        verify(userRepository)
                .save(any(User.class));
    }

    // =========================================================
    // 10. getAllUsers()
    // =========================================================

    @Test
    void testGetAllUsers() {

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Test User");
        user2.setEmail("test@gmail.com");
        user2.setPassword("encodedPassword");
        user2.setRole(Role.USER);

        List<User> users =
                List.of(user, user2);

        when(userRepository.findAll())
                .thenReturn(users);

        List<User> result =
                userDetailsService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(
                "karan@gmail.com",
                result.get(0).getEmail()
        );

        assertEquals(
                "test@gmail.com",
                result.get(1).getEmail()
        );

        verify(userRepository)
                .findAll();
    }

    // =========================================================
    // 11. updateUser() - owner can update
    // =========================================================

    @Test
    void testUpdateUserAsOwner() {

        UpdateUserRequest request =
                new UpdateUserRequest();

        request.setName("Updated Karan");
        request.setEmail("updated@gmail.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        doReturn(
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        ).when(authentication).getAuthorities();

        when(authentication.getName())
                .thenReturn("karan@gmail.com");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result =
                userDetailsService.updateUser(
                        1L,
                        request,
                        authentication
                );

        assertNotNull(result);

        assertEquals(
                "Updated Karan",
                result.getName()
        );

        assertEquals(
                "updated@gmail.com",
                result.getEmail()
        );

        verify(userRepository)
                .save(any(User.class));
    }

    // =========================================================
    // 12. updateUser() - admin can update
    // =========================================================

    @Test
    void testUpdateUserAsAdmin() {

        UpdateUserRequest request =
                new UpdateUserRequest();

        request.setName("Admin Updated");
        request.setEmail("adminupdated@gmail.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        doReturn(
                List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN")
                )
        ).when(authentication).getAuthorities();

        when(authentication.getName())
                .thenReturn("admin@gmail.com");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result =
                userDetailsService.updateUser(
                        1L,
                        request,
                        authentication
                );

        assertNotNull(result);

        assertEquals(
                "Admin Updated",
                result.getName()
        );

        assertEquals(
                "adminupdated@gmail.com",
                result.getEmail()
        );

        verify(userRepository)
                .save(any(User.class));
    }

    // =========================================================
    // 13. updateUser() - unauthorized user
    // =========================================================

    @Test
    void testUpdateUserUnauthorized() {

        UpdateUserRequest request =
                new UpdateUserRequest();

        request.setName("Hacker");
        request.setEmail("hacker@gmail.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        doReturn(
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        ).when(authentication).getAuthorities();

        when(authentication.getName())
                .thenReturn("otheruser@gmail.com");

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> userDetailsService.updateUser(
                                1L,
                                request,
                                authentication
                        )
                );

        assertEquals(
                "You are not allowed to update this user",
                exception.getMessage()
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    // =========================================================
    // 14. updateUser() - user not found
    // =========================================================

    @Test
    void testUpdateUserWhenUserDoesNotExist() {

        UpdateUserRequest request =
                new UpdateUserRequest();

        request.setName("Updated User");
        request.setEmail("updated@gmail.com");

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> userDetailsService.updateUser(
                                999L,
                                request,
                                authentication
                        )
                );

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    // =========================================================
    // 15. deleteUser()
    // =========================================================

    @Test
    void testDeleteUser() {

        doNothing()
                .when(userRepository)
                .deleteById(1L);

        String result =
                userDetailsService.deleteUser(1L);

        assertEquals(
                "User Deleted Successfully",
                result
        );

        verify(userRepository)
                .deleteById(1L);
    }
}
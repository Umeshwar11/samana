package com.samana.controller;

import com.samana.dto.HostProfileRequest;
import com.samana.dto.HostProfileResponse;
import com.samana.entity.HostProfile;
import com.samana.entity.User;
import com.samana.enums.Role;
import com.samana.repository.HostProfileRepository;
import com.samana.repository.UserRepository;
import com.samana.service.HostProfileService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/hosts")
@CrossOrigin("*")
public class HostProfileController {

    private final HostProfileService hostProfileService;
    private final UserRepository userRepository;
    private final HostProfileRepository hostProfileRepository;

    public HostProfileController(
            HostProfileService hostProfileService,
            UserRepository userRepository,
            HostProfileRepository hostProfileRepository) {

        this.hostProfileService = hostProfileService;
        this.userRepository = userRepository;
        this.hostProfileRepository = hostProfileRepository;
    }

    // ---------------------------------------------------------
    // BECOME HOST
    // ---------------------------------------------------------

    @PostMapping("/become")
    public ResponseEntity<HostProfileResponse> becomeHost(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found."));

        if (user.getRole() == Role.ADMIN) {

            throw new RuntimeException(
                    "Admin cannot become a host."
            );
        }

        if (user.getRole() == Role.HOST) {

            HostProfile existingProfile =
                    hostProfileRepository
                            .findByUser(user)
                            .orElse(null);

            if (existingProfile != null) {

                return ResponseEntity.ok(
                        hostProfileService
                                .getHostProfileByUserId(
                                        user.getId()
                                )
                );
            }
        }

        user.setRole(Role.HOST);

        userRepository.save(user);

        HostProfile hostProfile =
                new HostProfile();

        hostProfile.setUser(user);

        hostProfileRepository.save(hostProfile);

        return ResponseEntity.ok(
                hostProfileService
                        .getHostProfileByUserId(
                                user.getId()
                        )
        );
    }

    // ---------------------------------------------------------
    // SAVE PROFILE DETAILS
    // ---------------------------------------------------------

    @PostMapping("/profile")
    public ResponseEntity<HostProfileResponse> saveHostProfile(
            @Valid @RequestBody HostProfileRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                hostProfileService.saveHostProfile(
                        request,
                        email
                )
        );
    }

    // ---------------------------------------------------------
    // UPLOAD PROFILE PHOTOS
    // ---------------------------------------------------------

    @PostMapping(
            value = "/profile/media",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<HostProfileResponse> uploadHostMedia(

            @RequestPart(
                    value = "profilePhoto",
                    required = false
            )
            MultipartFile profilePhoto,

            @RequestPart(
                    value = "familyPhoto",
                    required = false
            )
            MultipartFile familyPhoto,

            @RequestPart(
                    value = "housePhoto",
                    required = false
            )
            MultipartFile housePhoto,

            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                hostProfileService.saveHostMedia(
                        email,
                        profilePhoto,
                        familyPhoto,
                        housePhoto
                )
        );
    }

    // ---------------------------------------------------------
    // GET MY PROFILE
    // ---------------------------------------------------------

    @GetMapping("/me")
    public ResponseEntity<HostProfileResponse> getMyHostProfile(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                hostProfileService.getMyHostProfile(
                        email
                )
        );
    }

    // ---------------------------------------------------------
    // GET PUBLIC HOST PROFILE
    // ---------------------------------------------------------

    @GetMapping("/profile/{userId}")
    public ResponseEntity<HostProfileResponse> getHostProfile(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                hostProfileService.getHostProfileByUserId(
                        userId
                )
        );
    }
}
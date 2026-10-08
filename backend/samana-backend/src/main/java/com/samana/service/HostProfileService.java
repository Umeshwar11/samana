package com.samana.service;

import com.samana.dto.HostProfileRequest;
import com.samana.dto.HostProfileResponse;
import com.samana.entity.HostProfile;
import com.samana.entity.User;
import com.samana.repository.ExperienceRepository;
import com.samana.repository.HostProfileRepository;
import com.samana.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class HostProfileService {

    private final HostProfileRepository hostProfileRepository;
    private final UserRepository userRepository;
    private final ExperienceRepository experienceRepository;

    private static final String UPLOAD_DIRECTORY = "uploads/hosts";

    public HostProfileService(
            HostProfileRepository hostProfileRepository,
            UserRepository userRepository,
            ExperienceRepository experienceRepository) {

        this.hostProfileRepository = hostProfileRepository;
        this.userRepository = userRepository;
        this.experienceRepository = experienceRepository;
    }

    // =========================================================
    // SAVE HOST PROFILE DETAILS
    // =========================================================

    public HostProfileResponse saveHostProfile(
            HostProfileRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found."));

        HostProfile hostProfile =
                hostProfileRepository.findByUser(user)
                        .orElseGet(() -> {

                            HostProfile newProfile =
                                    new HostProfile();

                            newProfile.setUser(user);

                            return newProfile;
                        });

        // -----------------------------------------------------
        // BASIC PROFILE INFORMATION
        // -----------------------------------------------------

        hostProfile.setLocation(
                request.getLocation()
        );

        hostProfile.setEducation(
                request.getEducation()
        );

        hostProfile.setProfession(
                request.getProfession()
        );

        hostProfile.setAboutHost(
                request.getAboutHost()
        );

        // -----------------------------------------------------
        // FAMILY INFORMATION
        // -----------------------------------------------------

        hostProfile.setFamilyDescription(
                request.getFamilyDescription()
        );

        // -----------------------------------------------------
        // HOSTING EXPERIENCE
        // -----------------------------------------------------

        hostProfile.setPreviousHostingExperience(
                request.getPreviousHostingExperience()
        );

        hostProfile.setYearsOfHosting(
                request.getYearsOfHosting()
        );

        // -----------------------------------------------------
        // SOCIAL MEDIA / WEBSITE LINKS
        // -----------------------------------------------------

        hostProfile.setInstagramUrl(
                request.getInstagramUrl()
        );

        hostProfile.setFacebookUrl(
                request.getFacebookUrl()
        );

        hostProfile.setLinkedinUrl(
                request.getLinkedinUrl()
        );

        hostProfile.setTwitterUrl(
                request.getTwitterUrl()
        );

        hostProfile.setYoutubeUrl(
                request.getYoutubeUrl()
        );

        hostProfile.setWebsiteUrl(
                request.getWebsiteUrl()
        );

        // -----------------------------------------------------
        // SAVE PROFILE
        // -----------------------------------------------------

        hostProfile =
                hostProfileRepository.save(hostProfile);

        return convertToResponse(hostProfile);
    }

    // =========================================================
    // SAVE HOST PROFILE PHOTOS
    // =========================================================

    public HostProfileResponse saveHostMedia(
            String email,
            MultipartFile profilePhoto,
            MultipartFile familyPhoto,
            MultipartFile housePhoto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found."));

        HostProfile hostProfile =
                hostProfileRepository.findByUser(user)
                        .orElseGet(() -> {

                            HostProfile newProfile =
                                    new HostProfile();

                            newProfile.setUser(user);

                            return newProfile;
                        });

        try {

            // -------------------------------------------------
            // CREATE USER HOST DIRECTORY
            // -------------------------------------------------

            Path hostDirectory = Paths.get(
                    UPLOAD_DIRECTORY,
                    String.valueOf(user.getId())
            );

            Files.createDirectories(hostDirectory);

            // -------------------------------------------------
            // PROFILE PHOTO
            // -------------------------------------------------

            if (profilePhoto != null &&
                    !profilePhoto.isEmpty()) {

                String fileUrl = saveImage(
                        profilePhoto,
                        hostDirectory,
                        user.getId(),
                        "profile"
                );

                hostProfile.setProfilePhotoUrl(fileUrl);
            }

            // -------------------------------------------------
            // FAMILY PHOTO
            // -------------------------------------------------

            if (familyPhoto != null &&
                    !familyPhoto.isEmpty()) {

                String fileUrl = saveImage(
                        familyPhoto,
                        hostDirectory,
                        user.getId(),
                        "family"
                );

                hostProfile.setFamilyPhotoUrl(fileUrl);
            }

            // -------------------------------------------------
            // HOUSE PHOTO
            // -------------------------------------------------

            if (housePhoto != null &&
                    !housePhoto.isEmpty()) {

                String fileUrl = saveImage(
                        housePhoto,
                        hostDirectory,
                        user.getId(),
                        "house"
                );

                hostProfile.setHousePhotoUrl(fileUrl);
            }

            // -------------------------------------------------
            // SAVE HOST PROFILE
            // -------------------------------------------------

            hostProfile =
                    hostProfileRepository.save(hostProfile);

            return convertToResponse(hostProfile);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to save host images.",
                    e
            );
        }
    }

    // =========================================================
    // SAVE IMAGE
    // =========================================================

    private String saveImage(
            MultipartFile file,
            Path directory,
            Long userId,
            String prefix) throws IOException {

        String originalFilename =
                file.getOriginalFilename();

        String extension = "";

        if (originalFilename != null &&
                originalFilename.contains(".")) {

            extension =
                    originalFilename.substring(
                            originalFilename.lastIndexOf(".")
                    );
        }

        String fileName =
                prefix + "_" +
                UUID.randomUUID() +
                extension;

        Path filePath =
                directory.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath
        );

        return "/uploads/hosts/" +
                userId +
                "/" +
                fileName;
    }

    // =========================================================
    // GET MY HOST PROFILE
    // =========================================================

    public HostProfileResponse getMyHostProfile(
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found."));

        HostProfile hostProfile =
                hostProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Host profile not found."
                                ));

        return convertToResponse(hostProfile);
    }

    // =========================================================
    // GET HOST PROFILE BY USER ID
    // =========================================================

    public HostProfileResponse getHostProfileByUserId(
            Long userId) {

        HostProfile hostProfile =
                hostProfileRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Host profile not found."
                                ));

        return convertToResponse(hostProfile);
    }

    // =========================================================
    // CONVERT ENTITY TO RESPONSE
    // =========================================================

    private HostProfileResponse convertToResponse(
            HostProfile hostProfile) {

        User user = hostProfile.getUser();

        // -----------------------------------------------------
        // EXPERIENCE COUNT
        // -----------------------------------------------------

        long experiencesHosted = 0L;

        if (user != null) {

            List<?> experiences =
                    experienceRepository.findByHostId(
                            user.getId()
                    );

            experiencesHosted =
                    experiences.size();
        }

        HostProfileResponse response =
                new HostProfileResponse();

        // =====================================================
        // BASIC INFORMATION
        // =====================================================

        response.setId(
                hostProfile.getId()
        );

        response.setUserId(
                user != null
                        ? user.getId()
                        : null
        );

        response.setHostName(
                user != null
                        ? user.getName()
                        : null
        );

        response.setEmail(
                user != null
                        ? user.getEmail()
                        : null
        );

        // =====================================================
        // PROFILE PHOTO
        // =====================================================

        response.setProfilePhotoUrl(
                hostProfile.getProfilePhotoUrl()
        );

        // =====================================================
        // PERSONAL INFORMATION
        // =====================================================

        response.setLocation(
                hostProfile.getLocation()
        );

        response.setEducation(
                hostProfile.getEducation()
        );

        response.setProfession(
                hostProfile.getProfession()
        );

        response.setAboutHost(
                hostProfile.getAboutHost()
        );

        // =====================================================
        // FAMILY INFORMATION
        // =====================================================

        response.setFamilyDescription(
                hostProfile.getFamilyDescription()
        );

        response.setFamilyPhotoUrl(
                hostProfile.getFamilyPhotoUrl()
        );

        response.setHousePhotoUrl(
                hostProfile.getHousePhotoUrl()
        );

        // =====================================================
        // HOSTING EXPERIENCE
        // =====================================================

        response.setPreviousHostingExperience(
                hostProfile.getPreviousHostingExperience()
        );

        response.setYearsOfHosting(
                hostProfile.getYearsOfHosting()
        );

        // =====================================================
        // SOCIAL MEDIA / WEBSITE LINKS
        // =====================================================

        response.setInstagramUrl(
                hostProfile.getInstagramUrl()
        );

        response.setFacebookUrl(
                hostProfile.getFacebookUrl()
        );

        response.setLinkedinUrl(
                hostProfile.getLinkedinUrl()
        );

        response.setTwitterUrl(
                hostProfile.getTwitterUrl()
        );

        response.setYoutubeUrl(
                hostProfile.getYoutubeUrl()
        );

        response.setWebsiteUrl(
                hostProfile.getWebsiteUrl()
        );

        // =====================================================
        // EXPERIENCE STATISTICS
        // =====================================================

        response.setExperiencesHosted(
                experiencesHosted
        );

        // =====================================================
        // RATING STATISTICS
        // =====================================================

        // Rating system will be connected later.
        response.setAverageRating(0.0);

        response.setReviewCount(0L);

        return response;
    }
}
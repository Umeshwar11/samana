package com.samana.service;

import com.samana.dto.ExperienceRequest;
import com.samana.dto.ExperienceResponse;
import com.samana.entity.Category;
import com.samana.entity.Experience;
import com.samana.entity.User;
import com.samana.repository.CategoryRepository;
import com.samana.repository.ExperienceRepository;
import com.samana.repository.HostProfileRepository;
import com.samana.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExperienceService {

    // =========================================================
    // UPLOAD DIRECTORY
    // =========================================================

    private static final String UPLOAD_DIRECTORY =
            "uploads/experiences";


    // =========================================================
    // REPOSITORIES
    // =========================================================

    private final ExperienceRepository experienceRepository;

    private final UserRepository userRepository;

    private final HostProfileRepository hostProfileRepository;

    private final CategoryRepository categoryRepository;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ExperienceService(
            ExperienceRepository experienceRepository,
            UserRepository userRepository,
            HostProfileRepository hostProfileRepository,
            CategoryRepository categoryRepository
    ) {

        this.experienceRepository =
                experienceRepository;

        this.userRepository =
                userRepository;

        this.hostProfileRepository =
                hostProfileRepository;

        this.categoryRepository =
                categoryRepository;
    }


    // =========================================================
    // CREATE EXPERIENCE WITH IMAGES
    // =========================================================

    public ExperienceResponse createExperienceWithMedia(

            ExperienceRequest request,

            String email,

            MultipartFile coverImage,

            List<MultipartFile> images

    ) {

        // -----------------------------------------------------
        // FIND HOST
        // -----------------------------------------------------

        User host =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Host user not found."
                                )
                        );


        // -----------------------------------------------------
        // CHECK HOST PROFILE
        // -----------------------------------------------------

        hostProfileRepository.findByUserId(host.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Please complete your host profile before creating an experience."
                        )
                );


        // -----------------------------------------------------
        // FIND CATEGORY
        // -----------------------------------------------------

        Category category =
                categoryRepository.findById(
                        request.getCategoryId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Category not found."
                        )
                );


        // -----------------------------------------------------
        // CREATE EXPERIENCE
        // -----------------------------------------------------

        Experience experience =
                new Experience();


        // -----------------------------------------------------
        // BASIC INFORMATION
        // -----------------------------------------------------

        experience.setTitle(
                request.getTitle()
        );

        experience.setDescription(
                request.getDescription()
        );

        experience.setPrice(
                request.getPrice()
        );

        experience.setDuration(
                request.getDuration()
        );

        experience.setAvailable(
                true
        );


        // -----------------------------------------------------
        // EXPERIENCE DETAILS
        // -----------------------------------------------------

        experience.setLocation(
                request.getLocation()
        );

        experience.setMaxGuests(
                request.getMaxGuests()
        );

        experience.setMeetingPoint(
                request.getMeetingPoint()
        );

        experience.setWhatYouWillDo(
                request.getWhatYouWillDo()
        );

        experience.setIncluded(
                request.getIncluded()
        );

        experience.setNotIncluded(
                request.getNotIncluded()
        );

        experience.setCancellationPolicy(
                request.getCancellationPolicy()
        );


        // -----------------------------------------------------
        // VIDEO URL
        // -----------------------------------------------------

        if (request.getVideoUrl() != null) {

            experience.setVideoUrl(
                    request.getVideoUrl()
            );
        }


        // -----------------------------------------------------
        // HOST + CATEGORY
        // -----------------------------------------------------

        experience.setHost(
                host
        );

        experience.setCategory(
                category
        );


        // -----------------------------------------------------
        // SAVE FIRST
        //
        // We need the generated ID before creating:
        //
        // uploads/experiences/{experienceId}/
        // -----------------------------------------------------

        experience =
                experienceRepository.save(
                        experience
                );


        // -----------------------------------------------------
        // CREATE EXPERIENCE UPLOAD DIRECTORY
        // -----------------------------------------------------

        Path experienceUploadDirectory =
                Paths.get(
                        UPLOAD_DIRECTORY,
                        String.valueOf(
                                experience.getId()
                        )
                );


        try {

            Files.createDirectories(
                    experienceUploadDirectory
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not create upload directory.",
                    e
            );
        }


        // =====================================================
        // COVER IMAGE
        // =====================================================

        if (
                coverImage != null &&
                !coverImage.isEmpty()
        ) {

            String coverImageUrl =
                    saveImage(
                            coverImage,
                            experience.getId(),
                            "cover"
                    );

            experience.setCoverImageUrl(
                    coverImageUrl
            );
        }


        // =====================================================
        // ADDITIONAL IMAGES
        // =====================================================

        List<String> imageUrls =
                new ArrayList<>();


        if (
                images != null &&
                !images.isEmpty()
        ) {

            for (
                    MultipartFile image :
                    images
            ) {

                if (
                        image == null ||
                        image.isEmpty()
                ) {
                    continue;
                }


                // Only allow image files

                if (
                        image.getContentType() == null ||
                        !image.getContentType()
                                .startsWith("image/")
                ) {

                    throw new RuntimeException(
                            "Only image files are allowed."
                    );
                }


                String imageUrl =
                        saveImage(
                                image,
                                experience.getId(),
                                "image"
                        );


                imageUrls.add(
                        imageUrl
                );
            }
        }


        // =====================================================
        // STORE ADDITIONAL IMAGE URLS
        // =====================================================

        if (!imageUrls.isEmpty()) {

            experience.setImageUrls(
                    String.join(
                            ",",
                            imageUrls
                    )
            );

        } else {

            experience.setImageUrls(
                    null
            );
        }


        // =====================================================
        // SAVE UPDATED EXPERIENCE
        // =====================================================

        experience =
                experienceRepository.save(
                        experience
                );


        // =====================================================
        // RETURN RESPONSE
        // =====================================================

        return convertToResponse(
                experience
        );
    }


    // =========================================================
    // SAVE IMAGE
    // =========================================================

    private String saveImage(

            MultipartFile file,

            Long experienceId,

            String prefix

    ) {

        try {

            // -------------------------------------------------
            // ORIGINAL FILE NAME
            // -------------------------------------------------

            String originalFileName =
                    file.getOriginalFilename();


            String extension = "";


            if (
                    originalFileName != null &&
                    originalFileName.contains(".")
            ) {

                extension =
                        originalFileName.substring(
                                originalFileName.lastIndexOf(".")
                        );
            }


            // -------------------------------------------------
            // CREATE UNIQUE FILE NAME
            // -------------------------------------------------

            String fileName =
                    prefix +
                    "_" +
                    UUID.randomUUID() +
                    extension;


            // -------------------------------------------------
            // DIRECTORY
            // -------------------------------------------------

            Path directory =
                    Paths.get(
                            UPLOAD_DIRECTORY,
                            String.valueOf(
                                    experienceId
                            )
                    );


            Files.createDirectories(
                    directory
            );


            // -------------------------------------------------
            // FINAL FILE PATH
            // -------------------------------------------------

            Path filePath =
                    directory.resolve(
                            fileName
                    );


            // -------------------------------------------------
            // SAVE FILE
            // -------------------------------------------------

            Files.copy(
                    file.getInputStream(),
                    filePath
            );


            // -------------------------------------------------
            // URL RETURNED TO FRONTEND
            // -------------------------------------------------

            return
                    "/uploads/experiences/" +
                    experienceId +
                    "/" +
                    fileName;


        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not save image.",
                    e
            );
        }
    }


    // =========================================================
    // GET ALL EXPERIENCES
    // =========================================================

    public List<ExperienceResponse> getAllExperiences() {

        return experienceRepository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // =========================================================
    // GET EXPERIENCE BY ID
    // =========================================================

    public ExperienceResponse getExperienceById(
            Long id
    ) {

        Experience experience =
                experienceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Experience not found."
                                )
                        );


        return convertToResponse(
                experience
        );
    }


    // =========================================================
    // UPDATE EXPERIENCE
    // =========================================================

    public ExperienceResponse updateExperience(

            Long id,

            ExperienceRequest request,

            String email

    ) {

        Experience experience =
                experienceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Experience not found."
                                )
                        );


        // -----------------------------------------------------
        // CHECK HOST
        // -----------------------------------------------------

        if (
                experience.getHost() == null ||
                !experience.getHost()
                        .getEmail()
                        .equals(email)
        ) {

            throw new RuntimeException(
                    "You are not allowed to update this experience."
            );
        }


        // -----------------------------------------------------
        // FIND CATEGORY
        // -----------------------------------------------------

        Category category =
                categoryRepository.findById(
                        request.getCategoryId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Category not found."
                        )
                );


        // -----------------------------------------------------
        // BASIC INFORMATION
        // -----------------------------------------------------

        experience.setTitle(
                request.getTitle()
        );

        experience.setDescription(
                request.getDescription()
        );

        experience.setPrice(
                request.getPrice()
        );

        experience.setDuration(
                request.getDuration()
        );


        // -----------------------------------------------------
        // EXPERIENCE DETAILS
        // -----------------------------------------------------

        experience.setLocation(
                request.getLocation()
        );

        experience.setMaxGuests(
                request.getMaxGuests()
        );

        experience.setMeetingPoint(
                request.getMeetingPoint()
        );

        experience.setWhatYouWillDo(
                request.getWhatYouWillDo()
        );

        experience.setIncluded(
                request.getIncluded()
        );

        experience.setNotIncluded(
                request.getNotIncluded()
        );

        experience.setCancellationPolicy(
                request.getCancellationPolicy()
        );


        // -----------------------------------------------------
        // VIDEO URL
        // -----------------------------------------------------

        experience.setVideoUrl(
                request.getVideoUrl()
        );


        // -----------------------------------------------------
        // CATEGORY
        // -----------------------------------------------------

        experience.setCategory(
                category
        );


        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        experience =
                experienceRepository.save(
                        experience
                );


        return convertToResponse(
                experience
        );
    }


    // =========================================================
    // DELETE EXPERIENCE
    // =========================================================

    public void deleteExperience(

            Long id,

            String email

    ) {

        Experience experience =
                experienceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Experience not found."
                                )
                        );


        // -----------------------------------------------------
        // CHECK HOST
        // -----------------------------------------------------

        if (
                experience.getHost() == null ||
                !experience.getHost()
                        .getEmail()
                        .equals(email)
        ) {

            throw new RuntimeException(
                    "You are not allowed to delete this experience."
            );
        }


        // -----------------------------------------------------
        // DELETE DATABASE RECORD
        // -----------------------------------------------------

        experienceRepository.delete(
                experience
        );


        // -----------------------------------------------------
        // DELETE UPLOAD DIRECTORY
        // -----------------------------------------------------

        Path directory =
                Paths.get(
                        UPLOAD_DIRECTORY,
                        String.valueOf(id)
                );


        try {

            if (Files.exists(directory)) {

                Files.walk(directory)
                        .sorted(
                                (a, b) ->
                                        b.compareTo(a)
                        )
                        .forEach(path -> {

                            try {

                                Files.deleteIfExists(
                                        path
                                );

                            } catch (IOException e) {

                                System.err.println(
                                        "Could not delete: " +
                                        path
                                );
                            }
                        });
            }

        } catch (IOException e) {

            System.err.println(
                    "Could not delete experience files: " +
                    e.getMessage()
            );
        }
    }


    // =========================================================
    // CONVERT ENTITY → RESPONSE
    // =========================================================

    private ExperienceResponse convertToResponse(
            Experience experience
    ) {

        Long hostId = null;

        if (experience.getHost() != null) {

            hostId =
                    experience.getHost().getId();
        }


        Long categoryId = null;

        if (experience.getCategory() != null) {

            categoryId =
                    experience.getCategory().getId();
        }


        return new ExperienceResponse(

                // ---------------------------------------------
                // BASIC
                // ---------------------------------------------

                experience.getId(),

                experience.getTitle(),

                experience.getDescription(),

                experience.getPrice(),

                experience.getDuration(),

                experience.getAvailable(),

                hostId,

                categoryId,


                // ---------------------------------------------
                // DETAILS
                // ---------------------------------------------

                experience.getLocation(),

                experience.getMaxGuests(),

                experience.getMeetingPoint(),

                experience.getWhatYouWillDo(),

                experience.getIncluded(),

                experience.getNotIncluded(),

                experience.getCancellationPolicy(),


                // ---------------------------------------------
                // MEDIA
                // ---------------------------------------------

                experience.getCoverImageUrl(),

                experience.getVideoUrl(),

                experience.getImageUrls()
        );
    }
}
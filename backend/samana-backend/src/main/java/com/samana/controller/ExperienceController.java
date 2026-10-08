package com.samana.controller;

import com.samana.dto.ExperienceRequest;
import com.samana.dto.ExperienceResponse;
import com.samana.service.ExperienceService;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/experiences")
@CrossOrigin(origins = "*")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }


    // =========================================================
    // CREATE EXPERIENCE
    // =========================================================

    @PostMapping(
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ExperienceResponse> createExperience(

            @Valid
            @ModelAttribute
            ExperienceRequest request,

            @RequestPart(
                value = "coverImage",
                required = false
            )
            MultipartFile coverImage,

            @RequestPart(
                value = "images",
                required = false
            )
            List<MultipartFile> images,

            Authentication authentication
    ) {

        String email = authentication.getName();

        ExperienceResponse response =
                experienceService.createExperienceWithMedia(
                        request,
                        email,
                        coverImage,
                        images
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // GET ALL EXPERIENCES
    // =========================================================

    @GetMapping
    public ResponseEntity<List<ExperienceResponse>> getAllExperiences() {

        List<ExperienceResponse> experiences =
                experienceService.getAllExperiences();

        return ResponseEntity.ok(experiences);
    }


    // =========================================================
    // GET EXPERIENCE BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponse> getExperienceById(
            @PathVariable Long id
    ) {

        ExperienceResponse experience =
                experienceService.getExperienceById(id);

        return ResponseEntity.ok(experience);
    }


    // =========================================================
    // UPDATE EXPERIENCE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponse> updateExperience(

            @PathVariable Long id,

            @Valid
            @RequestBody
            ExperienceRequest request,

            Authentication authentication
    ) {

        String email = authentication.getName();

        ExperienceResponse response =
                experienceService.updateExperience(
                        id,
                        request,
                        email
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // DELETE EXPERIENCE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(

            @PathVariable Long id,

            Authentication authentication
    ) {

        String email = authentication.getName();

        experienceService.deleteExperience(
                id,
                email
        );

        return ResponseEntity.noContent().build();
    }
}
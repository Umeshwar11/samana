package com.samana.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samana.dto.ExperiencePackageRequest;
import com.samana.dto.ExperiencePackageResponse;
import com.samana.service.ExperiencePackageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ExperiencePackageController
 * Tests API endpoints with mock service layer
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ExperiencePackageController Integration Tests")
public class ExperiencePackageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExperiencePackageService packageService;

    @Autowired
    private ObjectMapper objectMapper;

    private ExperiencePackageRequest validRequest;
    private ExperiencePackageResponse testResponse;

    @BeforeEach
    void setUp() {
        validRequest = new ExperiencePackageRequest();
        validRequest.setName("3-Day Package");
        validRequest.setDescription("Complete 3-day experience");
        validRequest.setPrice(5000.0);
        validRequest.setDuration(3);
        validRequest.setMaxGuests(5);
        validRequest.setIncluded("Accommodation, meals, activities");
        validRequest.setNotIncluded("Travel from city");

        testResponse = new ExperiencePackageResponse();
        testResponse.setId(1L);
        testResponse.setName("3-Day Package");
        testResponse.setPrice(5000.0);
        testResponse.setDuration(3);
        testResponse.setMaxGuests(5);
        testResponse.setAvailable(true);
        testResponse.setIsFeatured(false);
        testResponse.setExperienceId(1L);
        testResponse.setCreatedAt(LocalDateTime.now());
        testResponse.setUpdatedAt(LocalDateTime.now());
    }

    // ==========================================
    // CREATE TESTS
    // ==========================================

    @Test
    @DisplayName("Create package successfully")
    void testCreatePackageSuccess() throws Exception {
        // Arrange
        when(packageService.createPackage(anyLong(), any(ExperiencePackageRequest.class)))
                .thenReturn(testResponse);

        // Act & Assert
        mockMvc.perform(post("/api/packages")
                .param("experienceId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("3-Day Package"))
                .andExpect(jsonPath("$.price").value(5000.0));

        verify(packageService, times(1)).createPackage(anyLong(), any());
    }

    @Test
    @DisplayName("Create package fails with invalid data")
    void testCreatePackageBadRequest() throws Exception {
        // Arrange
        validRequest.setPrice(-1000.0); // Invalid price

        // Act & Assert
        mockMvc.perform(post("/api/packages")
                .param("experienceId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    // ==========================================
    // READ TESTS
    // ==========================================

    @Test
    @DisplayName("Get package by ID successfully")
    void testGetPackageByIdSuccess() throws Exception {
        // Arrange
        when(packageService.getPackageById(1L))
                .thenReturn(testResponse);

        // Act & Assert
        mockMvc.perform(get("/api/packages/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("3-Day Package"));

        verify(packageService, times(1)).getPackageById(1L);
    }

    @Test
    @DisplayName("Get package by ID returns 404 when not found")
    void testGetPackageByIdNotFound() throws Exception {
        // Arrange
        when(packageService.getPackageById(anyLong()))
                .thenThrow(new IllegalArgumentException("Package not found"));

        // Act & Assert
        mockMvc.perform(get("/api/packages/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get packages by experience ID successfully")
    void testGetPackagesByExperienceIdSuccess() throws Exception {
        // Arrange
        List<ExperiencePackageResponse> packages = Arrays.asList(testResponse);
        when(packageService.getPackagesByExperienceId(1L))
                .thenReturn(packages);

        // Act & Assert
        mockMvc.perform(get("/api/packages/experience/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("3-Day Package"));

        verify(packageService, times(1)).getPackagesByExperienceId(1L);
    }

    @Test
    @DisplayName("Get available packages by experience ID successfully")
    void testGetAvailablePackagesByExperienceIdSuccess() throws Exception {
        // Arrange
        List<ExperiencePackageResponse> packages = Arrays.asList(testResponse);
        when(packageService.getAvailablePackagesByExperienceId(1L))
                .thenReturn(packages);

        // Act & Assert
        mockMvc.perform(get("/api/packages/experience/1/available")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].available").value(true));

        verify(packageService, times(1)).getAvailablePackagesByExperienceId(1L);
    }

    @Test
    @DisplayName("Get featured package successfully")
    void testGetFeaturedPackageSuccess() throws Exception {
        // Arrange
        testResponse.setIsFeatured(true);
        when(packageService.getFeaturedPackageByExperienceId(1L))
                .thenReturn(testResponse);

        // Act & Assert
        mockMvc.perform(get("/api/packages/experience/1/featured")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isFeatured").value(true));

        verify(packageService, times(1)).getFeaturedPackageByExperienceId(1L);
    }

    // ==========================================
    // UPDATE TESTS
    // ==========================================

    @Test
    @DisplayName("Update package successfully")
    void testUpdatePackageSuccess() throws Exception {
        // Arrange
        when(packageService.updatePackage(anyLong(), any(ExperiencePackageRequest.class)))
                .thenReturn(testResponse);

        // Act & Assert
        mockMvc.perform(put("/api/packages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("3-Day Package"));

        verify(packageService, times(1)).updatePackage(anyLong(), any());
    }

    // ==========================================
    // DELETE TESTS
    // ==========================================

    @Test
    @DisplayName("Delete package successfully")
    void testDeletePackageSuccess() throws Exception {
        // Arrange
        doNothing().when(packageService).deletePackage(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/packages/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(packageService, times(1)).deletePackage(1L);
    }

    @Test
    @DisplayName("Delete package returns 404 when not found")
    void testDeletePackageNotFound() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Package not found"))
                .when(packageService).deletePackage(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/api/packages/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ==========================================
    // UTILITY ENDPOINT TESTS
    // ==========================================

    @Test
    @DisplayName("Validate package capacity returns true")
    void testValidatePackageCapacitySuccess() throws Exception {
        // Arrange
        when(packageService.canAccommodateGuests(1L, 5))
                .thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/packages/1/validate")
                .param("guests", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(packageService, times(1)).canAccommodateGuests(1L, 5);
    }

    @Test
    @DisplayName("Validate package capacity returns false")
    void testValidatePackageCapacityFails() throws Exception {
        // Arrange
        when(packageService.canAccommodateGuests(1L, 100))
                .thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/packages/1/validate")
                .param("guests", "100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(packageService, times(1)).canAccommodateGuests(1L, 100);
    }

    @Test
    @DisplayName("Validate package availability success")
    void testValidatePackageAvailabilitySuccess() throws Exception {
        // Arrange
        doNothing().when(packageService).validatePackageAvailability(1L);

        // Act & Assert
        mockMvc.perform(get("/api/packages/1/availability")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Package is available"));

        verify(packageService, times(1)).validatePackageAvailability(1L);
    }

    @Test
    @DisplayName("Validate package availability fails when unavailable")
    void testValidatePackageAvailabilityFails() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Package is not available"))
                .when(packageService).validatePackageAvailability(anyLong());

        // Act & Assert
        mockMvc.perform(get("/api/packages/1/availability")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("Package is not available"));
    }
}

package com.samana.service;

import com.samana.dto.ExperiencePackageRequest;
import com.samana.dto.ExperiencePackageResponse;
import com.samana.entity.Category;
import com.samana.entity.Experience;
import com.samana.entity.ExperiencePackage;
import com.samana.repository.ExperiencePackageRepository;
import com.samana.repository.ExperienceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ExperiencePackageService
 * Tests all business logic, validation, and error handling
 */
@DisplayName("ExperiencePackageService Tests")
public class ExperiencePackageServiceTest {

    @Mock
    private ExperiencePackageRepository packageRepository;

    @Mock
    private ExperienceRepository experienceRepository;

    @InjectMocks
    private ExperiencePackageService packageService;

    private Experience testExperience;
    private ExperiencePackage testPackage;
    private ExperiencePackageRequest validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        testExperience = new Experience();
        testExperience.setId(1L);
        testExperience.setTitle("Holi Festival");
        testExperience.setPrice(5000.0);
        testExperience.setDuration(3);

        testPackage = new ExperiencePackage();
        testPackage.setId(1L);
        testPackage.setName("3-Day Package");
        testPackage.setPrice(5000.0);
        testPackage.setDuration(3);
        testPackage.setMaxGuests(5);
        testPackage.setAvailable(true);
        testPackage.setExperience(testExperience);

        validRequest = new ExperiencePackageRequest();
        validRequest.setName("3-Day Package");
        validRequest.setPrice(5000.0);
        validRequest.setDuration(3);
        validRequest.setMaxGuests(5);
        validRequest.setIncluded("Accommodation, meals, activities");
        validRequest.setNotIncluded("Travel from city");
    }

    // ==========================================
    // CREATE TESTS
    // ==========================================

    @Test
    @DisplayName("Create package successfully with valid data")
    void testCreatePackageSuccess() {
        // Arrange
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(testExperience));
        when(packageRepository.save(any(ExperiencePackage.class))).thenReturn(testPackage);

        // Act
        ExperiencePackageResponse response = packageService.createPackage(1L, validRequest);

        // Assert
        assertNotNull(response);
        assertEquals("3-Day Package", response.getName());
        assertEquals(5000.0, response.getPrice());
        assertEquals(3, response.getDuration());
        verify(packageRepository, times(1)).save(any(ExperiencePackage.class));
    }

    @Test
    @DisplayName("Create package fails when experience doesn't exist")
    void testCreatePackageExperienceNotFound() {
        // Arrange
        when(experienceRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.createPackage(999L, validRequest);
        });
    }

    @Test
    @DisplayName("Create package fails with null request")
    void testCreatePackageNullRequest() {
        // Arrange
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(testExperience));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.createPackage(1L, null);
        });
    }

    @Test
    @DisplayName("Create package fails with missing name")
    void testCreatePackageMissingName() {
        // Arrange
        validRequest.setName("");
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(testExperience));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.createPackage(1L, validRequest);
        });
    }

    @Test
    @DisplayName("Create package fails with invalid price (negative)")
    void testCreatePackageInvalidPriceNegative() {
        // Arrange
        validRequest.setPrice(-1000.0);
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(testExperience));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.createPackage(1L, validRequest);
        });
    }

    @Test
    @DisplayName("Create package fails with zero price")
    void testCreatePackagePriceZero() {
        // Arrange
        validRequest.setPrice(0.0);
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(testExperience));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.createPackage(1L, validRequest);
        });
    }

    @Test
    @DisplayName("Create package fails with invalid duration (zero)")
    void testCreatePackageDurationZero() {
        // Arrange
        validRequest.setDuration(0);
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(testExperience));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.createPackage(1L, validRequest);
        });
    }

    @Test
    @DisplayName("Create package fails with duration exceeding 365 days")
    void testCreatePackageDurationTooLong() {
        // Arrange
        validRequest.setDuration(366);
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(testExperience));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.createPackage(1L, validRequest);
        });
    }

    @Test
    @DisplayName("Create package fails with invalid max guests (zero)")
    void testCreatePackageMaxGuestsZero() {
        // Arrange
        validRequest.setMaxGuests(0);
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(testExperience));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.createPackage(1L, validRequest);
        });
    }

    // ==========================================
    // READ TESTS
    // ==========================================

    @Test
    @DisplayName("Get package by ID successfully")
    void testGetPackageByIdSuccess() {
        // Arrange
        when(packageRepository.findById(1L)).thenReturn(Optional.of(testPackage));

        // Act
        ExperiencePackageResponse response = packageService.getPackageById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("3-Day Package", response.getName());
    }

    @Test
    @DisplayName("Get package by ID fails when package doesn't exist")
    void testGetPackageByIdNotFound() {
        // Arrange
        when(packageRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.getPackageById(999L);
        });
    }

    @Test
    @DisplayName("Get packages by experience ID successfully")
    void testGetPackagesByExperienceIdSuccess() {
        // Arrange
        List<ExperiencePackage> packages = Arrays.asList(testPackage);
        when(experienceRepository.existsById(1L)).thenReturn(true);
        when(packageRepository.findByExperienceId(1L)).thenReturn(packages);

        // Act
        List<ExperiencePackageResponse> responses = packageService.getPackagesByExperienceId(1L);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("3-Day Package", responses.get(0).getName());
    }

    @Test
    @DisplayName("Get packages by experience ID fails when experience doesn't exist")
    void testGetPackagesByExperienceIdNotFound() {
        // Arrange
        when(experienceRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.getPackagesByExperienceId(999L);
        });
    }

    // ==========================================
    // UPDATE TESTS
    // ==========================================

    @Test
    @DisplayName("Update package successfully")
    void testUpdatePackageSuccess() {
        // Arrange
        when(packageRepository.findById(1L)).thenReturn(Optional.of(testPackage));
        when(packageRepository.save(any(ExperiencePackage.class))).thenReturn(testPackage);

        // Act
        ExperiencePackageResponse response = packageService.updatePackage(1L, validRequest);

        // Assert
        assertNotNull(response);
        assertEquals("3-Day Package", response.getName());
        verify(packageRepository, times(1)).save(any(ExperiencePackage.class));
    }

    @Test
    @DisplayName("Update package fails with invalid price")
    void testUpdatePackageInvalidPrice() {
        // Arrange
        validRequest.setPrice(-500.0);
        when(packageRepository.findById(1L)).thenReturn(Optional.of(testPackage));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.updatePackage(1L, validRequest);
        });
    }

    // ==========================================
    // DELETE TESTS
    // ==========================================

    @Test
    @DisplayName("Delete package successfully")
    void testDeletePackageSuccess() {
        // Arrange
        when(packageRepository.existsById(1L)).thenReturn(true);

        // Act
        packageService.deletePackage(1L);

        // Assert
        verify(packageRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete package fails when package doesn't exist")
    void testDeletePackageNotFound() {
        // Arrange
        when(packageRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.deletePackage(999L);
        });
    }

    // ==========================================
    // VALIDATION TESTS
    // ==========================================

    @Test
    @DisplayName("Validate package availability successfully")
    void testValidatePackageAvailabilitySuccess() {
        // Arrange
        when(packageRepository.findById(1L)).thenReturn(Optional.of(testPackage));

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> {
            packageService.validatePackageAvailability(1L);
        });
    }

    @Test
    @DisplayName("Validate package availability fails when package unavailable")
    void testValidatePackageAvailabilityFails() {
        // Arrange
        testPackage.setAvailable(false);
        when(packageRepository.findById(1L)).thenReturn(Optional.of(testPackage));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            packageService.validatePackageAvailability(1L);
        });
    }

    @Test
    @DisplayName("Can accommodate guests successfully")
    void testCanAccommodateGuestsSuccess() {
        // Arrange
        when(packageRepository.findById(1L)).thenReturn(Optional.of(testPackage));

        // Act
        boolean result = packageService.canAccommodateGuests(1L, 5);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Cannot accommodate more guests than max")
    void testCanAccommodateGuestsFails() {
        // Arrange
        when(packageRepository.findById(1L)).thenReturn(Optional.of(testPackage));

        // Act
        boolean result = packageService.canAccommodateGuests(1L, 10);

        // Assert
        assertFalse(result);
    }
}

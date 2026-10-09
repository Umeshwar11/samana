package com.samana.service;

import com.samana.dto.ExperienceResponse;
import com.samana.entity.Category;
import com.samana.entity.Experience;
import com.samana.repository.ExperienceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ExperienceSearchService
 * Tests all search and filter functionality
 */
@DisplayName("Experience Search Service Tests")
class ExperienceSearchServiceTest {

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private ExperienceService experienceService;

    @InjectMocks
    private ExperienceSearchService searchService;

    private Experience experience1;
    private Experience experience2;
    private ExperienceResponse response1;
    private ExperienceResponse response2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        experience1 = new Experience();
        experience1.setId(1L);
        experience1.setTitle("Holi Festival in Mathura");
        experience1.setState("Uttar Pradesh");
        experience1.setLocation("Mathura");
        experience1.setPrice(5000.0);
        experience1.setDuration(3);
        experience1.setMaxGuests(8);

        experience2 = new Experience();
        experience2.setId(2L);
        experience2.setTitle("Diwali in Rajasthan");
        experience2.setState("Rajasthan");
        experience2.setLocation("Jaipur");
        experience2.setPrice(8000.0);
        experience2.setDuration(5);
        experience2.setMaxGuests(10);

        response1 = new ExperienceResponse();
        response2 = new ExperienceResponse();
    }

    @Test
    @DisplayName("Should search experiences with all criteria successfully")
    void testSearchExperiencesWithAllCriteria() {
        // Arrange
        when(experienceRepository.searchExperiences(
                "Holi", "Uttar Pradesh", "Mathura", null, 4000.0, 6000.0, 2, 5, 5))
                .thenReturn(Arrays.asList(experience1));
        when(experienceService.convertToResponse(experience1)).thenReturn(response1);

        // Act
        List<ExperienceResponse> results = searchService.searchExperiences(
                "Holi", "Uttar Pradesh", "Mathura", null, 4000.0, 6000.0, 2, 5, 5);

        // Assert
        assertEquals(1, results.size());
        verify(experienceRepository, times(1)).searchExperiences(anyString(), anyString(), anyString(),
                isNull(), anyDouble(), anyDouble(), anyInt(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Should return all experiences when no filters applied")
    void testSearchExperiencesNoFilters() {
        // Arrange
        when(experienceRepository.searchExperiences(null, null, null, null, null, null, null, null, null))
                .thenReturn(Arrays.asList(experience1, experience2));
        when(experienceService.convertToResponse(experience1)).thenReturn(response1);
        when(experienceService.convertToResponse(experience2)).thenReturn(response2);

        // Act
        List<ExperienceResponse> results = searchService.searchExperiences(
                null, null, null, null, null, null, null, null, null);

        // Assert
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Should throw exception when minPrice > maxPrice")
    void testSearchExperiencesInvalidPriceRange() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                searchService.searchExperiences(null, null, null, null, 6000.0, 4000.0, null, null, null));
    }

    @Test
    @DisplayName("Should throw exception when minDuration > maxDuration")
    void testSearchExperiencesInvalidDurationRange() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                searchService.searchExperiences(null, null, null, null, null, null, 7, 3, null));
    }

    @Test
    @DisplayName("Should perform quick search by title")
    void testQuickSearch() {
        // Arrange
        when(experienceRepository.findByTitleContainingIgnoreCase("Holi"))
                .thenReturn(Arrays.asList(experience1));
        when(experienceService.convertToResponse(experience1)).thenReturn(response1);

        // Act
        List<ExperienceResponse> results = searchService.quickSearch("Holi");

        // Assert
        assertEquals(1, results.size());
        verify(experienceRepository, times(1)).findByTitleContainingIgnoreCase("Holi");
    }

    @Test
    @DisplayName("Should filter experiences by price range")
    void testFilterByPriceRange() {
        // Arrange
        when(experienceRepository.findByPriceBetween(4000.0, 6000.0))
                .thenReturn(Arrays.asList(experience1));
        when(experienceService.convertToResponse(experience1)).thenReturn(response1);

        // Act
        List<ExperienceResponse> results = searchService.filterByPriceRange(4000.0, 6000.0);

        // Assert
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("Should throw exception for invalid price range")
    void testFilterByPriceRangeInvalid() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                searchService.filterByPriceRange(6000.0, 4000.0));
    }

    @Test
    @DisplayName("Should filter experiences by duration range")
    void testFilterByDurationRange() {
        // Arrange
        when(experienceRepository.findByDurationBetween(2, 5))
                .thenReturn(Arrays.asList(experience1, experience2));
        when(experienceService.convertToResponse(experience1)).thenReturn(response1);
        when(experienceService.convertToResponse(experience2)).thenReturn(response2);

        // Act
        List<ExperienceResponse> results = searchService.filterByDurationRange(2, 5);

        // Assert
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Should filter experiences by state")
    void testFilterByState() {
        // Arrange
        when(experienceRepository.findByStateContainingIgnoreCase("Rajasthan"))
                .thenReturn(Arrays.asList(experience2));
        when(experienceService.convertToResponse(experience2)).thenReturn(response2);

        // Act
        List<ExperienceResponse> results = searchService.filterByState("Rajasthan");

        // Assert
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("Should filter experiences by location")
    void testFilterByLocation() {
        // Arrange
        when(experienceRepository.findByLocationContainingIgnoreCase("Mathura"))
                .thenReturn(Arrays.asList(experience1));
        when(experienceService.convertToResponse(experience1)).thenReturn(response1);

        // Act
        List<ExperienceResponse> results = searchService.filterByLocation("Mathura");

        // Assert
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("Should filter experiences by categories")
    void testFilterByCategories() {
        // Arrange
        List<Long> categoryIds = Arrays.asList(1L, 2L);
        when(experienceRepository.findByCategoryIdIn(categoryIds))
                .thenReturn(Arrays.asList(experience1, experience2));
        when(experienceService.convertToResponse(experience1)).thenReturn(response1);
        when(experienceService.convertToResponse(experience2)).thenReturn(response2);

        // Act
        List<ExperienceResponse> results = searchService.filterByCategories(categoryIds);

        // Assert
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Should throw exception for empty category list")
    void testFilterByCategoriesEmpty() {
        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                searchService.filterByCategories(Arrays.asList()));
    }

    @Test
    @DisplayName("Should filter experiences by guest capacity")
    void testFilterByGuestCapacity() {
        // Arrange
        when(experienceRepository.findByMaxGuestsGreaterThanOrEqual(8))
                .thenReturn(Arrays.asList(experience1, experience2));
        when(experienceService.convertToResponse(experience1)).thenReturn(response1);
        when(experienceService.convertToResponse(experience2)).thenReturn(response2);

        // Act
        List<ExperienceResponse> results = searchService.filterByGuestCapacity(8);

        // Assert
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Should get all states for filter dropdown")
    void testGetAllStates() {
        // Arrange
        List<String> states = Arrays.asList("Rajasthan", "Uttar Pradesh", "Tamil Nadu");
        when(experienceRepository.findAllStates()).thenReturn(states);

        // Act
        List<String> results = searchService.getAllStates();

        // Assert
        assertEquals(3, results.size());
        assertTrue(results.contains("Rajasthan"));
    }

    @Test
    @DisplayName("Should get all locations for filter dropdown")
    void testGetAllLocations() {
        // Arrange
        List<String> locations = Arrays.asList("Jaipur", "Mathura", "Varanasi");
        when(experienceRepository.findAllLocations()).thenReturn(locations);

        // Act
        List<String> results = searchService.getAllLocations();

        // Assert
        assertEquals(3, results.size());
        assertTrue(results.contains("Jaipur"));
    }
}

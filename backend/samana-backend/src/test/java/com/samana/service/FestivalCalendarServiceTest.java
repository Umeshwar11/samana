package com.samana.service;

import com.samana.dto.AvailableDateRequest;
import com.samana.dto.AvailableDateResponse;
import com.samana.entity.AvailableDate;
import com.samana.entity.Experience;
import com.samana.repository.AvailableDateRepository;
import com.samana.repository.ExperienceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FestivalCalendarService
 * Tests all calendar and festival date functionality
 */
@DisplayName("Festival Calendar Service Tests")
class FestivalCalendarServiceTest {

    @Mock
    private AvailableDateRepository availableDateRepository;

    @Mock
    private ExperienceRepository experienceRepository;

    @InjectMocks
    private FestivalCalendarService calendarService;

    private Experience experience;
    private AvailableDate availableDate;
    private AvailableDateRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        experience = new Experience();
        experience.setId(1L);
        experience.setTitle("Holi Festival in Mathura");
        experience.setState("Uttar Pradesh");

        availableDate = new AvailableDate(
                experience,
                LocalDate.of(2026, 3, 25),
                10,
                "Holi",
                true
        );
        availableDate.setId(1L);

        request = new AvailableDateRequest();
        request.setExperienceId(1L);
        request.setDate(LocalDate.of(2026, 3, 25));
        request.setTotalSlots(10);
        request.setAvailableSlots(10);
        request.setIsFestivalDate(true);
        request.setFestivalName("Holi");
    }

    @Test
    @DisplayName("Should create available date successfully")
    void testCreateAvailableDate() {
        // Arrange
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));
        when(availableDateRepository.findByExperienceIdAndDate(1L, request.getDate()))
                .thenReturn(Optional.empty());
        when(availableDateRepository.save(any(AvailableDate.class))).thenReturn(availableDate);

        // Act
        AvailableDateResponse response = calendarService.createAvailableDate(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Holi", response.getFestivalName());
        verify(availableDateRepository, times(1)).save(any(AvailableDate.class));
    }

    @Test
    @DisplayName("Should throw exception when experience not found")
    void testCreateAvailableDateExperienceNotFound() {
        // Arrange
        when(experienceRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                calendarService.createAvailableDate(request));
    }

    @Test
    @DisplayName("Should throw exception when date already exists")
    void testCreateAvailableDateAlreadyExists() {
        // Arrange
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));
        when(availableDateRepository.findByExperienceIdAndDate(1L, request.getDate()))
                .thenReturn(Optional.of(availableDate));

        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                calendarService.createAvailableDate(request));
    }

    @Test
    @DisplayName("Should get all available dates for experience")
    void testGetAvailableDatesByExperience() {
        // Arrange
        List<AvailableDate> dates = Arrays.asList(availableDate);
        when(availableDateRepository.findByExperienceIdOrderByDateAsc(1L))
                .thenReturn(dates);

        // Act
        List<AvailableDateResponse> results = calendarService.getAvailableDatesByExperience(1L);

        // Assert
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("Should get festivals by month")
    void testGetFestivalsByMonth() {
        // Arrange
        List<AvailableDate> festivals = Arrays.asList(availableDate);
        when(availableDateRepository.findFestivalsByMonth(3))
                .thenReturn(festivals);

        // Act
        List<AvailableDateResponse> results = calendarService.getFestivalsByMonth(3);

        // Assert
        assertEquals(1, results.size());
        assertTrue(results.get(0).getIsFestivalDate());
    }

    @Test
    @DisplayName("Should get festivals by month and year")
    void testGetFestivalsByMonthAndYear() {
        // Arrange
        List<AvailableDate> festivals = Arrays.asList(availableDate);
        when(availableDateRepository.findFestivalsByMonthAndYear(3, 2026))
                .thenReturn(festivals);

        // Act
        List<AvailableDateResponse> results = calendarService.getFestivalsByMonthAndYear(3, 2026);

        // Assert
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("Should get festivals by state and month")
    void testGetFestivalsByStateAndMonth() {
        // Arrange
        List<AvailableDate> festivals = Arrays.asList(availableDate);
        when(availableDateRepository.findFestivalsByStateAndMonth("Uttar Pradesh", 3))
                .thenReturn(festivals);

        // Act
        List<AvailableDateResponse> results = calendarService.getFestivalsByStateAndMonth("Uttar Pradesh", 3);

        // Assert
        assertEquals(1, results.size());
        assertEquals("Uttar Pradesh", results.get(0).getExperienceTitle().substring(results.get(0).getExperienceTitle().lastIndexOf(" ") + 1));
    }

    @Test
    @DisplayName("Should get festivals by state")
    void testGetFestivalsByState() {
        // Arrange
        List<AvailableDate> festivals = Arrays.asList(availableDate);
        when(availableDateRepository.findFestivalsByState("Uttar Pradesh"))
                .thenReturn(festivals);

        // Act
        List<AvailableDateResponse> results = calendarService.getFestivalsByState("Uttar Pradesh");

        // Assert
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("Should get upcoming festivals")
    void testGetUpcomingFestivals() {
        // Arrange
        List<AvailableDate> upcoming = Arrays.asList(availableDate);
        when(availableDateRepository.findUpcomingFestivals(any(LocalDate.class)))
                .thenReturn(upcoming);

        // Act
        List<AvailableDateResponse> results = calendarService.getUpcomingFestivals();

        // Assert
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("Should update available date successfully")
    void testUpdateAvailableDate() {
        // Arrange
        when(availableDateRepository.findById(1L)).thenReturn(Optional.of(availableDate));
        when(availableDateRepository.save(any(AvailableDate.class))).thenReturn(availableDate);

        // Act
        AvailableDateResponse response = calendarService.updateAvailableDate(1L, request);

        // Assert
        assertNotNull(response);
        verify(availableDateRepository, times(1)).save(any(AvailableDate.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent date")
    void testUpdateAvailableDateNotFound() {
        // Arrange
        when(availableDateRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                calendarService.updateAvailableDate(1L, request));
    }

    @Test
    @DisplayName("Should delete available date successfully")
    void testDeleteAvailableDate() {
        // Arrange
        when(availableDateRepository.existsById(1L)).thenReturn(true);

        // Act
        calendarService.deleteAvailableDate(1L);

        // Assert
        verify(availableDateRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent date")
    void testDeleteAvailableDateNotFound() {
        // Arrange
        when(availableDateRepository.existsById(1L)).thenReturn(false);

        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                calendarService.deleteAvailableDate(1L));
    }

    @Test
    @DisplayName("Should book a slot successfully")
    void testBookSlot() {
        // Arrange
        when(availableDateRepository.findById(1L)).thenReturn(Optional.of(availableDate));
        when(availableDateRepository.save(any(AvailableDate.class))).thenReturn(availableDate);

        // Act
        AvailableDateResponse response = calendarService.bookSlot(1L);

        // Assert
        assertNotNull(response);
        verify(availableDateRepository, times(1)).save(any(AvailableDate.class));
    }

    @Test
    @DisplayName("Should throw exception when booking with no available slots")
    void testBookSlotNoAvailability() {
        // Arrange
        AvailableDate fullDate = new AvailableDate(experience, LocalDate.now(), 1);
        fullDate.setAvailableSlots(0);
        when(availableDateRepository.findById(1L)).thenReturn(Optional.of(fullDate));

        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                calendarService.bookSlot(1L));
    }

    @Test
    @DisplayName("Should cancel slot successfully")
    void testCancelSlot() {
        // Arrange
        availableDate.setAvailableSlots(5);
        when(availableDateRepository.findById(1L)).thenReturn(Optional.of(availableDate));
        when(availableDateRepository.save(any(AvailableDate.class))).thenReturn(availableDate);

        // Act
        AvailableDateResponse response = calendarService.cancelSlot(1L);

        // Assert
        assertNotNull(response);
        verify(availableDateRepository, times(1)).save(any(AvailableDate.class));
    }

    @Test
    @DisplayName("Should get all festival states")
    void testGetAllFestivalStates() {
        // Arrange
        List<String> states = Arrays.asList("Uttar Pradesh", "Rajasthan", "Tamil Nadu");
        when(availableDateRepository.findAllFestivalStates()).thenReturn(states);

        // Act
        List<String> results = calendarService.getAllFestivalStates();

        // Assert
        assertEquals(3, results.size());
    }

    @Test
    @DisplayName("Should get festival statistics")
    void testGetFestivalStatistics() {
        // Arrange
        List<String> states = Arrays.asList("Uttar Pradesh", "Rajasthan");
        when(availableDateRepository.findAllFestivalStates()).thenReturn(states);
        when(availableDateRepository.findUpcomingFestivals(any(LocalDate.class)))
                .thenReturn(Arrays.asList(availableDate));

        // Act
        java.util.Map<String, Object> stats = calendarService.getFestivalStatistics();

        // Assert
        assertNotNull(stats);
        assertEquals(2, stats.get("total_festival_states"));
        assertTrue(stats.containsKey("states"));
    }
}

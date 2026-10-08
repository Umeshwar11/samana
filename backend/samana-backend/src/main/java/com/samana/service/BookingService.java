package com.samana.service;

import com.samana.dto.BookingRequest;
import com.samana.dto.BookingResponse;
import com.samana.entity.Booking;
import com.samana.entity.Experience;
import com.samana.entity.User;
import com.samana.enums.BookingStatus;
import com.samana.repository.BookingRepository;
import com.samana.repository.ExperienceRepository;
import com.samana.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExperienceRepository experienceRepository;


    // =====================================================
    // CREATE BOOKING
    // =====================================================

    public BookingResponse createBooking(
            BookingRequest request,
            String email) {

        // Find logged-in user
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );


        // Find experience
        Experience experience = experienceRepository
                .findById(request.getExperienceId())
                .orElseThrow(() ->
                        new RuntimeException("Experience not found")
                );


        // Check experience availability
        if (experience.getAvailable() == null
                || !experience.getAvailable()) {

            throw new RuntimeException(
                    "Experience is not available"
            );
        }


        // Calculate total price
        Double totalPrice =
                experience.getPrice()
                        * request.getNumberOfPeople();


        // Create booking
        Booking booking = new Booking();

        booking.setUser(user);

        booking.setExperience(experience);

        booking.setNumberOfPeople(
                request.getNumberOfPeople()
        );

        booking.setBookingDate(
                request.getBookingDate()
        );

        booking.setTotalPrice(
                totalPrice
        );

        booking.setStatus(
                BookingStatus.PENDING
        );

        booking.setCreatedAt(
                LocalDateTime.now()
        );


        // Save booking
        Booking savedBooking =
                bookingRepository.save(booking);


        // Return response
        return convertToResponse(savedBooking);
    }


    // =====================================================
    // GET MY BOOKINGS
    // =====================================================

    public List<BookingResponse> getMyBookings(
            String email) {

        // Find logged-in user
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );


        // Find bookings belonging to user
        List<Booking> bookings =
                bookingRepository.findByUser(user);


        // Convert Entity -> DTO
        return bookings
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // =====================================================
    // CANCEL MY BOOKING
    // =====================================================

    public BookingResponse cancelBooking(
            Long bookingId,
            String email) {

        // Find logged-in user
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );


        // Find booking belonging to this user
        Booking booking =
                bookingRepository
                        .findByIdAndUser(
                                bookingId,
                                user
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found"
                                )
                        );


        // Check if already cancelled
        if (booking.getStatus()
                == BookingStatus.CANCELLED) {

            throw new RuntimeException(
                    "Booking is already cancelled"
            );
        }


        // Cancel booking
        booking.setStatus(
                BookingStatus.CANCELLED
        );


        // Save
        Booking updatedBooking =
                bookingRepository.save(booking);


        return convertToResponse(updatedBooking);
    }


    // =====================================================
    // ADMIN - GET ALL BOOKINGS
    // =====================================================

    public List<BookingResponse> getAllBookings() {

        List<Booking> bookings =
                bookingRepository.findAll();


        return bookings
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // =====================================================
    // ADMIN - GET BOOKING BY ID
    // =====================================================

    public BookingResponse getBookingById(
            Long bookingId) {

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found"
                                )
                        );


        return convertToResponse(booking);
    }


    // =====================================================
    // ADMIN - CONFIRM BOOKING
    // =====================================================

    public BookingResponse confirmBooking(
            Long bookingId) {

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found"
                                )
                        );


        // Cannot confirm cancelled booking
        if (booking.getStatus()
                == BookingStatus.CANCELLED) {

            throw new RuntimeException(
                    "Cancelled booking cannot be confirmed"
            );
        }


        // Set confirmed
        booking.setStatus(
                BookingStatus.CONFIRMED
        );


        // Save
        Booking updatedBooking =
                bookingRepository.save(booking);


        return convertToResponse(updatedBooking);
    }


    // =====================================================
    // ADMIN - CANCEL BOOKING
    // =====================================================

    public BookingResponse adminCancelBooking(
            Long bookingId) {

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found"
                                )
                        );


        // Cancel booking
        booking.setStatus(
                BookingStatus.CANCELLED
        );


        // Save
        Booking updatedBooking =
                bookingRepository.save(booking);


        return convertToResponse(updatedBooking);
    }


    // =====================================================
    // ADMIN - DELETE BOOKING
    // =====================================================

    public String deleteBooking(
            Long bookingId) {

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found"
                                )
                        );


        bookingRepository.delete(booking);


        return "Booking deleted successfully";
    }


    // =====================================================
    // ENTITY -> RESPONSE DTO
    // =====================================================

    private BookingResponse convertToResponse(
            Booking booking) {

        return new BookingResponse(

                booking.getId(),

                booking.getUser().getId(),

                booking.getExperience().getId(),

                booking.getExperience().getTitle(),

                booking.getNumberOfPeople(),

                booking.getBookingDate(),

                booking.getTotalPrice(),

                booking.getStatus()
        );
    }
}
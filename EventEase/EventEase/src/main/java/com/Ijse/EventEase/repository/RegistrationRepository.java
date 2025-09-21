package com.Ijse.EventEase.repository;

import com.Ijse.EventEase.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    // Check duplicate registration
    Registration findByEvent_IdAndAttendee_Id(Long eventId, Long attendeeId);

    // Find all registrations by event
    List<Registration> findByEvent_Id(Long eventId);

    // Find all registrations by attendee
    List<Registration> findByAttendee_Id(Long attendeeId);

    // Optional: by event and ticket type
    List<Registration> findByEvent_IdAndTicketType(Long eventId, String ticketType);

    // Optional: by event and payment method
    List<Registration> findByEvent_IdAndPaymentMethode(Long eventId, String paymentMethode);
    // Count total attendees for a given organizer
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.event.organizer.id = :organizerId")
    long countByOrganizerId(@Param("organizerId") Long organizerId);

        // Count new registrations for today for a given organizer

    @Query("SELECT COUNT(r) FROM Registration r WHERE r.event.organizer.id = :organizerId AND DATE(r.registrationTime) = :today")
    long countByOrganizerIdAndRegistrationDate(@Param("organizerId") Long organizerId, @Param("today") LocalDate today);
    }



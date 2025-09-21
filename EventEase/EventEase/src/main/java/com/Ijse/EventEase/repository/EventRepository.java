package com.Ijse.EventEase.repository;

import com.Ijse.EventEase.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventById(Long id);

    @Query("SELECT e FROM Event e WHERE e.organizer.email = :email")
    List<Event> findEventsByOrganizerEmail(@Param("email") String email);

    Optional<Event> findByTitle(String title);

    @Query("SELECT e FROM Event e WHERE e.eventDate = :eventDate AND e.organizer.email = :email")
    List<Event> findEventsByEventDateAndOrganizerEmail(@Param("eventDate") LocalDate eventDate,
                                                       @Param("email") String email);

    @Query("SELECT e FROM Event e WHERE e.eventDate = :eventDate")
    List<Event> findEventsByEventDate(@Param("eventDate") LocalDate eventDate);

    Long countEventsByOrganizerEmail(@Param("email") String email);

    // ✅ Correct derived query
    List<Event> findByOrganizer_Id(Long userId);

    Long countByOrganizer_IdAndEventDate(Long userId, LocalDate eventDate);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.event.organizer.id = :organizerId")
    Double findAverageRatingByOrganizerId(@Param("organizerId") Long organizerId);



}


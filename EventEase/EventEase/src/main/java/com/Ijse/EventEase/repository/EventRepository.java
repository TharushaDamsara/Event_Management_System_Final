package com.Ijse.EventEase.repository;

import com.Ijse.EventEase.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventById(Long id);

    @Query("SELECT e FROM Event e WHERE e.organizer.email = :email")
    List<Event> findEventsByOrganizerEmail(@Param("email") String email);

    Optional<Event> findByTitle(String title);

}

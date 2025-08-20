package com.Ijse.EventEase.repository;

import com.Ijse.EventEase.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventById(Long id);
    Optional<Event> findByOrganizerEmail(String email);

    Optional<Event> findByTitle(String title);
}

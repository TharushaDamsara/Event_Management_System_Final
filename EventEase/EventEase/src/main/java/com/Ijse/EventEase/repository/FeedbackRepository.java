package com.Ijse.EventEase.repository;


import com.Ijse.EventEase.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // navigate into the Event entity's id
    List<Feedback> findAllByEvent_Id(Long eventId);

    // navigate into Attendee entity's email
    List<Feedback> findAllByAttendee_Email(String email);
}


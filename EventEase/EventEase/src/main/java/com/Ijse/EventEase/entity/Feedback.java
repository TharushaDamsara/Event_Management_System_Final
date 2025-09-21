package com.Ijse.EventEase.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("feedbacks")
    @JoinColumn(name = "attendee_id", nullable = false)
    private User attendee;

    @ManyToOne
    @JsonIgnoreProperties("feedbacks")
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(length = 1000)
    private String comment;

    private int rating; // 1 to 5
    private LocalDateTime submittedAt;

}


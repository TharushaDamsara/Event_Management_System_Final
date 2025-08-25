package com.Ijse.EventEase.entity;

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
    private User attendee;

    @ManyToOne
    private Event event;

    @Column(length = 1000)
    private String comment;

    private int rating; // 1 to 5
    private LocalDateTime submittedAt;

}


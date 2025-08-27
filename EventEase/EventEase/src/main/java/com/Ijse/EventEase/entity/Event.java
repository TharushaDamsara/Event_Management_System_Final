package com.Ijse.EventEase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name="event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String bannerImageUrl;
    private int maxAttendees;
    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @OneToMany(mappedBy = "event")
    private List<Registration> registrations;

    @OneToMany(mappedBy = "event")
    private List<Feedback> feedbacks;
}


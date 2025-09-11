package com.Ijse.EventEase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Event{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private int maxAttendees;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets; // one-to-many tickets

    @OneToMany(mappedBy = "event")
    private List<Registration> registrations;

    @OneToMany(mappedBy = "event")
    private List<Feedback> feedbacks;

}


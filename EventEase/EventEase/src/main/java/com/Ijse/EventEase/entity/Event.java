package com.Ijse.EventEase.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private String category;
    private String location;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private int maxAttendees;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    @JsonIgnore // prevents sending the entire organizer object (break circular reference)
    private User organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "event")
    @JsonIgnore // optionally ignore registrations in this API
    private List<Registration> registrations;

    @OneToMany(mappedBy = "event")
    @JsonIgnore // optionally ignore feedbacks in this API
    private List<Feedback> feedbacks;


}


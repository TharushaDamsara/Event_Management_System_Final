package com.Ijse.EventEase.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tickets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private int quantity;

    @Column(length = 500)
    private String description;

    @ElementCollection
    private List<String> benefits;
    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonBackReference
    private Event event;


}

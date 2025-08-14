package com.Ijse.EventEase.entity;

import com.Ijse.EventEase.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;


    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // ORGANIZER / ATTENDEE
}


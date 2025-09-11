package com.Ijse.EventEase.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketDto {

    private Long id; // optional, DB-generated

    @NotBlank(message = "Ticket name is required")
    private String name;

    @PositiveOrZero(message = "Price must be 0 or greater")
    private double price;

    @Positive(message = "Quantity must be greater than 0")
    private int quantity;

    private String description;

    private List<String> benefits; // e.g., "Food", "Networking", etc.
}

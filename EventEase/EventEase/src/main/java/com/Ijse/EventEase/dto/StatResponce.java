package com.Ijse.EventEase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class StatResponce {
    private Long todayEvents;
    private Long todayRegistrations;
    private Long totalRegistrations;
    private Double averageRating;

}

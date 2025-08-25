package com.Ijse.EventEase.dto;

import com.Ijse.EventEase.entity.Event;
import com.Ijse.EventEase.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class FeedbackDto {
    private Long id;
    private String feedback;
    private int rating;
    private User attendee;
    private Event event;


}

package com.Ijse.EventEase.service;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.EventDto;
import com.Ijse.EventEase.exception.DuplicateEventException;
import com.Ijse.EventEase.exception.EventNotFoundException;

public interface EventService {
    ApiResponce createEvent(EventDto eventDto) throws Exception, DuplicateEventException;
    ApiResponce updateEvent(EventDto eventDto) throws Exception, EventNotFoundException;
    ApiResponce deleteEvent(Long eventId) throws Exception, EventNotFoundException;
    ApiResponce getEventBYEmail(String email) throws Exception, EventNotFoundException;
}

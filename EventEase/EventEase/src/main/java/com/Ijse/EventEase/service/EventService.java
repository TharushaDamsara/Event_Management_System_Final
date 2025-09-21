package com.Ijse.EventEase.service;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.EventDto;
import com.Ijse.EventEase.exception.DuplicateEventException;
import com.Ijse.EventEase.exception.EventNotFoundException;

import java.time.LocalDate;

public interface EventService {
    ApiResponce createEvent(EventDto eventDto) throws Exception, DuplicateEventException;
    ApiResponce updateEvent(EventDto eventDto) throws Exception, EventNotFoundException;
    ApiResponce deleteEvent(Long eventId) throws Exception, EventNotFoundException;
    ApiResponce getEventBYEmail(String email) throws Exception, EventNotFoundException;
    ApiResponce getOrganizerEventBYDate(LocalDate date,String email) throws Exception, EventNotFoundException;
    ApiResponce getAllEventBYDate(LocalDate date) throws Exception, EventNotFoundException;
    ApiResponce countEventsByOrganizerEmail(String email) throws Exception, EventNotFoundException;
    ApiResponce getAllEvents() throws Exception, EventNotFoundException;
    ApiResponce findEventById(Long eventId) throws Exception, EventNotFoundException;
    ApiResponce findEventsByOrganizerId(Long userId) throws Exception, EventNotFoundException;
    ApiResponce getStatisticsByOrganizerId(Long userId) throws Exception, EventNotFoundException;
}

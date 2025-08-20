package com.Ijse.EventEase.service.impl;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.EventDto;
import com.Ijse.EventEase.entity.Event;
import com.Ijse.EventEase.exception.DuplicateEventException;
import com.Ijse.EventEase.exception.EventNotFoundException;
import com.Ijse.EventEase.repository.EventRepository;
import com.Ijse.EventEase.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public ApiResponce createEvent(EventDto eventDto) throws DuplicateEventException {
        // Optional: check for duplicate title or organizer + date
        Optional<Event> existingEvent = eventRepository.findByTitle(eventDto.getTitle());
        if (existingEvent.isPresent()) {
            throw new DuplicateEventException("Event with this title already exists");
        }

        Event newEvent = mapToNewEvent(eventDto); // ID not set for new events
        eventRepository.save(newEvent);
        return new ApiResponce(201, "Event created successfully", true);
    }

    private Event mapToNewEvent(EventDto eventDto) {
        return Event.builder()
                .title(eventDto.getTitle())
                .description(eventDto.getDescription())
                .location(eventDto.getLocation())
                .eventDate(eventDto.getEventDate())
                .eventTime(eventDto.getEventTime())
                .bannerImageUrl(eventDto.getBannerImageUrl())
                .maxAttendees(eventDto.getMaxAttendees())
                .build();
    }

    private Event mapToExistingEvent(EventDto eventDto) {
        return Event.builder()
                .id(eventDto.getId()) // only for update
                .title(eventDto.getTitle())
                .description(eventDto.getDescription())
                .location(eventDto.getLocation())
                .eventDate(eventDto.getEventDate())
                .eventTime(eventDto.getEventTime())
                .bannerImageUrl(eventDto.getBannerImageUrl())
                .maxAttendees(eventDto.getMaxAttendees())
                .build();
    }

    @Override
    public ApiResponce updateEvent(EventDto eventDto) throws EventNotFoundException {
        if (!eventRepository.findEventById(eventDto.getId()).isPresent()) {
            throw new EventNotFoundException("Event not found");
        }
        eventRepository.save(mapToExistingEvent(eventDto));
        return new ApiResponce(200, "Event updated successfully", true);
    }

    @Override
    public ApiResponce deleteEvent(Long eventId) throws EventNotFoundException {
        if (!eventRepository.findEventById(eventId).isPresent()) {
            throw new EventNotFoundException("Event not found");
        }
        eventRepository.deleteById(eventId);
        return new ApiResponce(200, "Event deleted successfully", true);
    }

    @Override
    public ApiResponce getEventBYEmail(String email) throws EventNotFoundException {
        Optional<Event> eventOpt = eventRepository.findByOrganizerEmail(email);
        if (!eventOpt.isPresent()) {
            throw new EventNotFoundException("No events found for this email");
        }
        return new ApiResponce(200, "Event found successfully", true);
    }
}

package com.Ijse.EventEase.service.impl;

import com.Ijse.EventEase.dto.*;
import com.Ijse.EventEase.entity.Event;
import com.Ijse.EventEase.exception.DuplicateEventException;
import com.Ijse.EventEase.exception.EventNotFoundException;
import com.Ijse.EventEase.repository.EventRepository;
import com.Ijse.EventEase.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .organizer(eventDto.getOrganizer())
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
                .organizer(eventDto.getOrganizer())
                .build();
    }

    @Override
    public ApiResponce updateEvent(EventDto eventDto) throws EventNotFoundException {
        if (eventRepository.findEventById(eventDto.getId()).isEmpty()) {
            throw new EventNotFoundException("Event not found");
        }
        eventRepository.save(mapToExistingEvent(eventDto));
        return new ApiResponce(200, "Event updated successfully", true);
    }

    @Override
    public ApiResponce deleteEvent(Long eventId) throws EventNotFoundException {
        if (eventRepository.findEventById(eventId).isEmpty()) {
            throw new EventNotFoundException("Event not found");
        }
        eventRepository.deleteById(eventId);
        return new ApiResponce(200, "Event deleted successfully", true);
    }

    @Override
    public ApiResponce getEventBYEmail(String email) throws EventNotFoundException {
        List<Event> events = eventRepository.findEventsByOrganizerEmail(email);

        if (events.isEmpty()) {
            throw new EventNotFoundException("No events found for this email");
        }

        // Map to DTOs if you don’t want to expose entities directly
        List<EventResponseDto> eventDtos = events.stream()
                .map(e -> new EventResponseDto(
                        e.getId(),
                        e.getTitle(),
                        e.getDescription(),
                        e.getLocation(),
                        e.getEventDate(),
                        e.getEventTime(),
                        e.getBannerImageUrl(),
                        e.getMaxAttendees(),
                        new OrganizerDto(
                                e.getOrganizer().getId(),
                                e.getOrganizer().getName(),
                                e.getOrganizer().getEmail(),
                                e.getOrganizer().getRole()
                        )
                ))
                .toList();

        return new ApiResponce(200, "Events found successfully", eventDtos);
    }

}

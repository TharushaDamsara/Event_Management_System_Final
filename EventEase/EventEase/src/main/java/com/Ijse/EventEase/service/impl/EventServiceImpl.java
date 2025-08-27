package com.Ijse.EventEase.service.impl;

import com.Ijse.EventEase.dto.*;
import com.Ijse.EventEase.entity.Event;
import com.Ijse.EventEase.entity.User;
import com.Ijse.EventEase.exception.DuplicateEventException;
import com.Ijse.EventEase.exception.EventNotFoundException;
import com.Ijse.EventEase.repository.EventRepository;
import com.Ijse.EventEase.repository.UserRepository;
import com.Ijse.EventEase.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponce createEvent(EventDto eventDto) throws DuplicateEventException {
        // Check for duplicate by title
        if (eventRepository.findByTitle(eventDto.getTitle()).isPresent()) {
            throw new DuplicateEventException("Event with this title already exists");
        }

        // ✅ fetch organizer
        User organizer = userRepository.findById(eventDto.getOrganizerId())
                .orElseThrow(() -> new RuntimeException("Organizer not found"));

        Event newEvent = mapToNewEvent(eventDto, organizer);
        eventRepository.save(newEvent);

        return new ApiResponce(201, "Event created successfully", true);
    }

    private Event mapToNewEvent(EventDto eventDto, User organizer) {
        return Event.builder()
                .title(eventDto.getTitle())
                .description(eventDto.getDescription())
                .location(eventDto.getLocation())
                .eventDate(eventDto.getEventDate())
                .eventTime(eventDto.getEventTime())
                .bannerImageUrl(eventDto.getBannerImageUrl())
                .maxAttendees(eventDto.getMaxAttendees())
                .organizer(organizer) // ✅ use actual User
                .build();
    }

    private Event mapToExistingEvent(EventDto eventDto, User organizer) {
        return Event.builder()
                .id(eventDto.getId()) // keep ID for update
                .title(eventDto.getTitle())
                .description(eventDto.getDescription())
                .location(eventDto.getLocation())
                .eventDate(eventDto.getEventDate())
                .eventTime(eventDto.getEventTime())
                .bannerImageUrl(eventDto.getBannerImageUrl())
                .maxAttendees(eventDto.getMaxAttendees())
                .organizer(organizer) // ✅ use actual User
                .build();
    }

    @Override
    public ApiResponce updateEvent(EventDto eventDto) throws EventNotFoundException {
        Event existing = eventRepository.findEventById(eventDto.getId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        // ✅ fetch organizer
        User organizer = userRepository.findById(eventDto.getOrganizerId())
                .orElseThrow(() -> new RuntimeException("Organizer not found"));

        Event updatedEvent = mapToExistingEvent(eventDto, organizer);
        eventRepository.save(updatedEvent);

        return new ApiResponce(200, "Event updated successfully", true);
    }

    @Override
    public ApiResponce deleteEvent(Long eventId) throws EventNotFoundException {
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        eventRepository.delete(event);
        return new ApiResponce(200, "Event deleted successfully", true);
    }

    @Override
    public ApiResponce getEventBYEmail(String email) throws EventNotFoundException {
        List<Event> events = eventRepository.findEventsByOrganizerEmail(email);

        if (events.isEmpty()) {
            throw new EventNotFoundException("No events found for this email");
        }

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

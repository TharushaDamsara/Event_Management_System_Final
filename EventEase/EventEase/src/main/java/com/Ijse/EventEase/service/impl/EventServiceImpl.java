package com.Ijse.EventEase.service.impl;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.EventDto;
import com.Ijse.EventEase.entity.Event;
import com.Ijse.EventEase.entity.Ticket;
import com.Ijse.EventEase.entity.User;
import com.Ijse.EventEase.exception.DuplicateEventException;
import com.Ijse.EventEase.exception.EventNotFoundException;
import com.Ijse.EventEase.repository.EventRepository;
import com.Ijse.EventEase.repository.UserRepository;
import com.Ijse.EventEase.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ApiResponce createEvent(EventDto eventDto) throws DuplicateEventException {
        if (eventRepository.findByTitle(eventDto.getTitle()).isPresent()) {
            throw new DuplicateEventException("Event with this title already exists");
        }

        User organizer = userRepository.findById(eventDto.getOrganizerId())
                .orElseThrow(() -> new RuntimeException("Organizer not found"));

        Event event = Event.builder()
                .title(eventDto.getTitle())
                .description(eventDto.getDescription())
                .location(eventDto.getLocation())
                .eventDate(eventDto.getEventDate())
                .eventTime(eventDto.getEventTime())
                .maxAttendees(eventDto.getMaxAttendees())
                .organizer(organizer)
                .build();

        // map tickets
        List<Ticket> tickets = eventDto.getTickets().stream().map(t -> {
            Ticket ticket = Ticket.builder()
                    .name(t.getName())
                    .price(t.getPrice())
                    .quantity(t.getQuantity())
                    .description(t.getDescription())
                    .benefits(t.getBenefits())
                    .event(event)
                    .build();
            return ticket;
        }).collect(Collectors.toList());

        event.setTickets(tickets);

        eventRepository.save(event); // saves both event + tickets

        return new ApiResponce(201, "Event with tickets created successfully", true);
    }

    @Override
    @Transactional
    public ApiResponce updateEvent(EventDto eventDto) throws  EventNotFoundException {
        Event existing = eventRepository.findEventById(eventDto.getId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        User organizer = userRepository.findById(eventDto.getOrganizerId())
                .orElseThrow(() -> new RuntimeException("Organizer not found"));

        existing.setTitle(eventDto.getTitle());
        existing.setDescription(eventDto.getDescription());
        existing.setLocation(eventDto.getLocation());
        existing.setEventDate(eventDto.getEventDate());
        existing.setEventTime(eventDto.getEventTime());
        existing.setMaxAttendees(eventDto.getMaxAttendees());
        existing.setOrganizer(organizer);

        // update tickets
        existing.getTickets().clear();
        List<Ticket> tickets = eventDto.getTickets().stream().map(t -> {
            Ticket ticket = Ticket.builder()
                    .name(t.getName())
                    .price(t.getPrice())
                    .quantity(t.getQuantity())
                    .description(t.getDescription())
                    .benefits(t.getBenefits())
                    .event(existing)
                    .build();
            return ticket;
        }).collect(Collectors.toList());

        existing.setTickets(tickets);

        eventRepository.save(existing);

        return new ApiResponce(200, "Event with tickets updated successfully", true);
    }

    @Override
    @Transactional
    public ApiResponce deleteEvent(Long eventId) throws  EventNotFoundException {
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        eventRepository.delete(event);
        return new ApiResponce(200, "Event deleted successfully", true);
    }

    @Override
    public ApiResponce getEventBYEmail(String email) throws  EventNotFoundException {
        List<Event> events = eventRepository.findEventsByOrganizerEmail(email);
        if (events.isEmpty()) {
            throw new EventNotFoundException("No events found for this email");
        }
        return new ApiResponce(200, "Events found", events);
    }

    @Override
    public ApiResponce getOrganizerEventBYDate(LocalDate date, String email) throws Exception, EventNotFoundException {
        List<Event> eventsByEventDateAndOrganizerEmail = eventRepository.findEventsByEventDateAndOrganizerEmail(date, email);
        if (eventsByEventDateAndOrganizerEmail.isEmpty()) {
            throw new EventNotFoundException("No your events found for this date");
        }
        return new ApiResponce(200, "Events found", eventsByEventDateAndOrganizerEmail);
    }

    @Override
    public ApiResponce getAllEventBYDate(LocalDate date) throws Exception, EventNotFoundException {
        List<Event> eventsByEventDate = eventRepository.findEventsByEventDate(date);
        if (eventsByEventDate.isEmpty()) {
            throw new EventNotFoundException("No events found for this date");
        }
        return new ApiResponce(200, "Events found", eventsByEventDate);
    }

    @Override
    public ApiResponce countEventsByOrganizerEmail(String email) throws Exception, EventNotFoundException {
        Long l = eventRepository.countEventsByOrganizerEmail(email);
        return new ApiResponce(200, "Events count", l);
    }


}

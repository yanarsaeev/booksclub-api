package com.booksclub.api.service;

import com.booksclub.api.dto.EventDto;
import com.booksclub.api.entities.Event;
import com.booksclub.api.entities.Person;
import com.booksclub.api.exception.NotFoundException;
import com.booksclub.api.repository.EventRepository;
import com.booksclub.api.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final PersonRepository personRepository;

    public List<Event> findAll() {
        return this.eventRepository.findAll();
    }

    public Event findById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id %s not found".formatted(eventId)));
    }

    @Transactional
    public Event save(EventDto eventDto) {
        Event event = new Event();
        event.setName(eventDto.getName());
        event.setDescription(event.getDescription());
        event.setPlannedAt(eventDto.getPlannedAt());
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.findPersonByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        event.setManager(person);
        person.getEvents().add(event);

        return eventRepository.save(event);
    }

    @Transactional
    public void delete(Long eventId) {
        if (this.eventRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event with id %s not found".formatted(eventId));
        }

        this.eventRepository.deleteById(eventId);
    }

    @Transactional
    public void update(Long eventId, String eventName, String description, LocalDateTime plannedAt) {
        this.eventRepository.findById(eventId)
                .ifPresentOrElse(event -> {
                    event.setName(eventName);
                    event.setDescription(description);
                    event.setPlannedAt(plannedAt);
                }, () -> {
                    throw new NotFoundException("Event with id %s not found".formatted(eventId));
                });
    }
}
package com.booksclub.api.service;

import com.booksclub.api.entities.Event;
import com.booksclub.api.exception.NotFoundException;
import com.booksclub.api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> findAll() {
        return this.eventRepository.findAll();
    }

    public Event findById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id %s not found".formatted(eventId)));
    }

    @Transactional
    public void save(Event event) {
        if (event.getPlannedAt() == null) {
            event.setPlannedAt(LocalDateTime.now());
        }

        eventRepository.saveAndFlush(enrichEvent(event));
    }

    @Transactional
    public void delete(Long eventId) {
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

    private Event enrichEvent(Event event) {
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        return event;
    }
}
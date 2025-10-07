package com.booksclub.api.controller;

import com.booksclub.api.dto.EventDto;
import com.booksclub.api.entities.Event;
import com.booksclub.api.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventRestController {

    private final EventService eventService;

    private final ModelMapper modelMapper;

    @GetMapping
    public List<EventDto> getAll() {
        return eventService.findAll().stream().map(this::convertToEventDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventDto get(@PathVariable("eventId") Long eventId) {
        return convertToEventDto(this.eventService.findById(eventId));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid EventDto eventDto, BindingResult bindingResult,
                                    UriComponentsBuilder uriComponentsBuilder)
            throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Event savedEvent = convertToEvent(eventDto);
            eventService.save(savedEvent);
            return ResponseEntity
                    .created(uriComponentsBuilder
                        .replacePath("/api/events/{eventId}")
                        .build(Map.of("eventId", savedEvent.getId())))
                    .body(savedEvent);
        }
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<?> update(@PathVariable("eventId") Long eventId,
                                    @RequestBody @Valid EventDto eventDto,
                                    BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.eventService.update(eventId, eventDto.getName(), eventDto.getDescription(), eventDto.getPlannedAt());
            return ResponseEntity.noContent()
                    .build();
        }
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable("eventId") Long eventId) {
        this.eventService.delete(eventId);
        return ResponseEntity.noContent().build();
    }

    private EventDto convertToEventDto(Event event) {
        return modelMapper.map(event, EventDto.class);
    }

    private Event convertToEvent(EventDto eventDto) {
        return modelMapper.map(eventDto, Event.class);
    }
}

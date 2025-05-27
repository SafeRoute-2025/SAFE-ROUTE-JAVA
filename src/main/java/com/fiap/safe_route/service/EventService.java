package com.fiap.safe_route.service;

import com.fiap.safe_route.dto.event.EventRequest;
import com.fiap.safe_route.dto.event.EventResponse;
import com.fiap.safe_route.model.Event;
import com.fiap.safe_route.repository.EventRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public EventResponse create(EventRequest request) {
        Event event = Event.builder()
                .eventType(request.eventType())
                .description(request.description())
                .eventDate(request.eventDate())
                .riskLevel(request.riskLevel())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .build();
        return toResponse(repository.save(event));
    }

    public EventResponse findById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public Page<EventResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        repository.deleteById(id);
    }

    private EventResponse toResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getEventType(),
                event.getDescription(),
                event.getEventDate(),
                event.getRiskLevel(),
                event.getLatitude(),
                event.getLongitude()
        );
    }

    @Transactional
    public EventResponse update(Long id, EventRequest request) {
        Event event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setEventType(request.eventType());
        event.setDescription(request.description());
        event.setEventDate(request.eventDate());
        event.setRiskLevel(request.riskLevel());
        event.setLatitude(request.latitude());
        event.setLongitude(request.longitude());
        return toResponse(repository.save(event));
    }

}
package com.fiap.safe_route.service;

import com.fiap.safe_route.dto.event.EventRequest;
import com.fiap.safe_route.dto.event.EventResponse;
import com.fiap.safe_route.exception.NotFoundException;
import com.fiap.safe_route.model.Event;
import com.fiap.safe_route.model.EventType;
import com.fiap.safe_route.model.RiskLevel;
import com.fiap.safe_route.repository.AlertRepository;
import com.fiap.safe_route.repository.EventRepository;
import com.fiap.safe_route.repository.EventTypeRepository;
import com.fiap.safe_route.repository.RiskLevelRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository repository;
    private final EventTypeRepository eventTypeRepository;
    private final RiskLevelRepository riskLevelRepository;
    private final AlertRepository alertRepository;
    public EventService(EventRepository repository,
                        EventTypeRepository eventTypeRepository,
                        RiskLevelRepository riskLevelRepository,
                        AlertRepository alertRepository) {
        this.repository = repository;
        this.eventTypeRepository = eventTypeRepository;
        this.riskLevelRepository = riskLevelRepository;
        this.alertRepository = alertRepository;
    }

    public EventResponse create(EventRequest request) {
        EventType eventType = eventTypeRepository.findByName(request.eventType())
                .orElseThrow(() -> new NotFoundException("EventType not found: " + request.eventType()));

        RiskLevel riskLevel = riskLevelRepository.findByName(request.riskLevel())
                .orElseThrow(() -> new NotFoundException("RiskLevel not found: " + request.riskLevel()));

        Date eventDate = request.eventDate() != null ? request.eventDate() : Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant());
        double latitude = request.latitude() != null ? request.latitude() : 0;
        double longitude = request.longitude() != null ? request.longitude() : 0;
        Event event = Event.builder()
                .eventType(eventType)
                .description(request.description())
                .eventDate(eventDate)
                .riskLevel(riskLevel)
                .latitude(latitude)
                .longitude(longitude)
                .build();
        return toResponse(repository.save(event));
    }

    public EventResponse findById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    public Page<EventResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Event not found");
        }
        alertRepository.deleteAllByEventId(id);
        repository.deleteById(id);
    }

    private EventResponse toResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getEventType().getName(),
                event.getDescription(),
                event.getEventDate(),
                event.getRiskLevel().getName(),
                event.getLatitude(),
                event.getLongitude()
        );
    }

    @Transactional
    public EventResponse update(Long id, EventRequest request) {
        Event event = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        EventType eventType = eventTypeRepository.findByName(request.eventType())
                .orElseThrow(() -> new NotFoundException("EventType not found: " + request.eventType()));

        RiskLevel riskLevel = riskLevelRepository.findByName(request.riskLevel())
                .orElseThrow(() -> new NotFoundException("RiskLevel not found: " + request.riskLevel()));

        event.setEventType(eventType);
        event.setDescription(request.description());
        event.setEventDate(request.eventDate());
        event.setRiskLevel(riskLevel);
        event.setLatitude(request.latitude());
        event.setLongitude(request.longitude());

        return toResponse(repository.save(event));
    }


    public List<String> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "eventDate"))
                .stream()
                .map(event -> String.format("%s - %s (%s)", event.getEventType().getName(), event.getDescription(), event.getEventDate()))
                .toList();
    }

    public List<EventResponse> findAllFlat() {
        return repository.findAll(Sort.by("eventDate")).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

}
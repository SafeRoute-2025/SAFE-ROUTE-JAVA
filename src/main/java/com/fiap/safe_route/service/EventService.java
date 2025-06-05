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
import com.fiap.safe_route.service.messaging.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private final EventRepository repository;
    @Autowired
    private final EventTypeRepository eventTypeRepository;
    @Autowired
    private final RiskLevelRepository riskLevelRepository;
    @Autowired
    private final AlertRepository alertRepository;
    @Autowired
    private final EventProducer eventProducer;
    public EventService(EventRepository repository,
                        EventTypeRepository eventTypeRepository,
                        RiskLevelRepository riskLevelRepository,
                        AlertRepository alertRepository,
                        EventProducer eventProducer) {
        this.repository = repository;
        this.eventTypeRepository = eventTypeRepository;
        this.riskLevelRepository = riskLevelRepository;
        this.alertRepository = alertRepository;
        this.eventProducer = eventProducer;
    }

    public EventResponse create(EventRequest request) {
        EventType eventType = eventTypeRepository.findByName(request.eventType())
                .orElseThrow(() -> new NotFoundException("EventType not found: " + request.eventType()));

        RiskLevel riskLevel = riskLevelRepository.findByName(request.riskLevel())
                .orElseThrow(() -> new NotFoundException("RiskLevel not found: " + request.riskLevel()));

        Event event = Event.builder()
                .eventType(eventType)
                .description(request.description())
                .eventDate(request.eventDate() != null ? request.eventDate() : new Date())
                .riskLevel(riskLevel)
                .latitude(request.latitude())
                .longitude(request.longitude())
                .build();
        repository.save(event);
        EventResponse response = toResponse(event);
        eventProducer.sendEvent(response);
        return response;
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
        EventType eventType = eventTypeRepository.findByName(request.eventType())
                .orElseThrow(() -> new NotFoundException("EventType not found: " + request.eventType()));

        RiskLevel riskLevel = riskLevelRepository.findByName(request.riskLevel())
                .orElseThrow(() -> new NotFoundException("RiskLevel not found: " + request.riskLevel()));

        if (!repository.existsById(id)) {
            throw new NotFoundException("Event not found");
        }

        Event event = Event.builder()
                .id(id)
                .eventType(eventType)
                .description(request.description())
                .eventDate(request.eventDate() != null ? request.eventDate() : new Date())
                .riskLevel(riskLevel)
                .latitude(request.latitude())
                .longitude(request.longitude())
                .build();

        repository.save(event);
        EventResponse response = toResponse(event);
        eventProducer.sendEvent(response);
        return response;
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

    public void saveOrUpdateFromMessage(EventResponse response) {
        EventType eventType = eventTypeRepository.findByName(response.getEventType())
                .orElseThrow(() -> new NotFoundException("EventType not found: " + response.getEventType()));

        RiskLevel riskLevel = riskLevelRepository.findByName(response.getRiskLevel())
                .orElseThrow(() -> new NotFoundException("RiskLevel not found: " + response.getRiskLevel()));

        Event event = null;

        if (response.getId() != null) {
            event = repository.findById(response.getId()).orElse(null);
            if (event != null) {
                // Atualizar
                event.setEventType(eventType);
                event.setDescription(response.getDescription());
                event.setEventDate(response.getEventDate());
                event.setRiskLevel(riskLevel);
                event.setLatitude(response.getLatitude());
                event.setLongitude(response.getLongitude());
            }
        } else{
            // Criar novo
            event = Event.builder()
                    .eventType(eventType)
                    .description(response.getDescription())
                    .eventDate(response.getEventDate())
                    .riskLevel(riskLevel)
                    .latitude(response.getLatitude())
                    .longitude(response.getLongitude())
                    .build();
        }


        repository.save(event);
    }




}
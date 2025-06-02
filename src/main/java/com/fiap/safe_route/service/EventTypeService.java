package com.fiap.safe_route.service;

import com.fiap.safe_route.dto.eventtype.EventTypeResponse;
import com.fiap.safe_route.model.EventType;
import com.fiap.safe_route.repository.EventTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventTypeService {

    private final EventTypeRepository eventTypeRepository;

    public EventTypeService(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    private EventTypeResponse toResponse(EventType eventType) {
        return new EventTypeResponse(
                eventType.getId(),
                eventType.getName()
        );
    }

    public EventTypeResponse findById(Long id) {
        return eventTypeRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Event Type not found"));
    }

    public EventTypeResponse findByName(String name) {
        return eventTypeRepository.findByName(name)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Event Type not found"));
    }

    public List<EventTypeResponse> findAll() {
        return eventTypeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Page<EventTypeResponse> findAllPaginated(Pageable pageable) {
        return eventTypeRepository.findAll(pageable).map(this::toResponse);
    }


}

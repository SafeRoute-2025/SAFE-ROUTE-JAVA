package com.fiap.safe_route.controller.api;

import com.fiap.safe_route.dto.eventtype.EventTypeResponse;
import com.fiap.safe_route.service.EventTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/event-types")
@Tag(name = "Event Type", description = "Event type lookup")
public class EventTypeController {

    private final EventTypeService service;

    public EventTypeController(EventTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EventTypeResponse>> getAll() {
        try{
            List<EventTypeResponse> eventTypes = service.findAll();
            if (eventTypes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(eventTypes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/page")
    public ResponseEntity<Page<EventTypeResponse>> getAllPaginated(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        try {
            Page<EventTypeResponse> eventTypes = service.findAllPaginated(pageable);
            if (eventTypes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(eventTypes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventTypeResponse> getById(@PathVariable Long id) {
        try {
            EventTypeResponse eventType = service.findById(id);
            return ResponseEntity.ok(eventType);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<EventTypeResponse>> getAllForSelect() {
        return ResponseEntity.ok(service.findAll());
    }

}

package com.fiap.safe_route.controller.api;

import com.fiap.safe_route.dto.event.EventRequest;
import com.fiap.safe_route.dto.event.EventResponse;
import com.fiap.safe_route.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Event", description = "Event API")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event found"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<EventResponse> getById(@PathVariable Long id) {
        try {
            EventResponse event = service.findById(id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "List all events (paginated)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No events found")
    })
    public ResponseEntity<Page<EventResponse>> getAll(@PageableDefault(size = 10, sort = "eventDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<EventResponse> events = service.findAll(pageable);
        if (events.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(events);
    }

    @PostMapping
    @Operation(summary = "Create a new event")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Event created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<EventResponse> create(@RequestBody @Valid EventRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Update an event by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event updated"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(@PathVariable Long id, @RequestBody @Valid EventRequest request) {
        try {
            EventResponse updated = service.update(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete event by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllResourceTypeNames() {
        List<String> allNames = service.getAll();
        return ResponseEntity.ok(allNames);
    }

    @GetMapping("/list")
    @Operation(summary = "List all events (flat list)")
    public ResponseEntity<List<EventResponse>> listAll() {
        List<EventResponse> events = service.findAllFlat();
        if (events.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(events);
    }

}
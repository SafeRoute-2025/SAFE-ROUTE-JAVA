package com.fiap.safe_route.controller;

import com.fiap.safe_route.controller.api.EventController;
import com.fiap.safe_route.dto.event.EventRequest;
import com.fiap.safe_route.dto.event.EventResponse;
import com.fiap.safe_route.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnEventById() {
        EventResponse event = new EventResponse(1L, "Flood", "Description", new Date(), "High", -23.55, -46.63);
        when(eventService.findById(1L)).thenReturn(event);

        ResponseEntity<EventResponse> response = eventController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    @Test
    void shouldReturnNotFoundForMissingEvent() {
        when(eventService.findById(999L)).thenThrow(new RuntimeException());

        ResponseEntity<EventResponse> response = eventController.getById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturnPaginatedEvents() {
        List<EventResponse> page = List.of(new EventResponse(1L, "Flood", "Description", new Date(), "High", -23.55, -46.63));
        when(eventService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(page));

        ResponseEntity<Page<EventResponse>> response = eventController.getAll(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void shouldReturnNoContentForEmptyPage() {
        when(eventService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        ResponseEntity<Page<EventResponse>> response = eventController.getAll(PageRequest.of(0, 10));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldCreateEvent() {
        EventRequest request = new EventRequest("Flood", "Description", new Date(), "High", -23.55, -46.63);
        EventResponse created = new EventResponse(1L, "Flood", "Description", new Date(), "High", -23.55, -46.63);

        when(eventService.create(request)).thenReturn(created);

        ResponseEntity<EventResponse> response = eventController.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(created, response.getBody());
    }

    @Test
    void shouldUpdateEvent() {
        EventRequest request = new EventRequest("Flood", "Updated Description", new Date(), "Medium", -23.56, -46.64);
        EventResponse updated = new EventResponse(1L, "Flood", "Updated Description", new Date(), "Medium", -23.56, -46.64);

        when(eventService.update(eq(1L), eq(request))).thenReturn(updated);

        ResponseEntity<EventResponse> response = eventController.update(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    void shouldDeleteEvent() {
        doNothing().when(eventService).delete(1L);

        ResponseEntity<Void> response = eventController.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(eventService).delete(1L);
    }

    @Test
    void shouldReturnAllEventNames() {
        List<String> names = List.of("Flood - Description");
        when(eventService.getAll()).thenReturn(names);

        ResponseEntity<List<String>> response = eventController.getAllResourceTypeNames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(names, response.getBody());
    }

    @Test
    void shouldReturnFlatEventList() {
        List<EventResponse> list = List.of(new EventResponse(1L, "Flood", "Desc", new Date(), "High", -23.55, -46.63));
        when(eventService.findAllFlat()).thenReturn(list);

        ResponseEntity<List<EventResponse>> response = eventController.listAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnNoContentForEmptyFlatList() {
        when(eventService.findAllFlat()).thenReturn(Collections.emptyList());

        ResponseEntity<List<EventResponse>> response = eventController.listAll();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

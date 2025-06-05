package com.fiap.safe_route.controller;

import com.fiap.safe_route.controller.api.AlertController;
import com.fiap.safe_route.dto.alert.AlertRequest;
import com.fiap.safe_route.dto.alert.AlertResponse;
import com.fiap.safe_route.service.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlertControllerTest {

    @Mock
    private AlertService alertService;

    @InjectMocks
    private AlertController alertController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAlertById() {
        AlertResponse alert = new AlertResponse(1L, "Mensagem", LocalDateTime.now(), 1L, "Evento");
        when(alertService.findById(1L)).thenReturn(alert);

        ResponseEntity<AlertResponse> response = alertController.getAlertById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(alert, response.getBody());
    }

    @Test
    void shouldReturnNotFoundForMissingAlert() {
        when(alertService.findById(999L)).thenThrow(new RuntimeException());

        ResponseEntity<AlertResponse> response = alertController.getAlertById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturnAllAlerts() {
        List<AlertResponse> list = List.of(new AlertResponse(1L, "Mensagem", LocalDateTime.now(), 1L, "Evento"));
        when(alertService.findAll()).thenReturn(list);

        ResponseEntity<List<AlertResponse>> response = alertController.getAllAlerts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnNoContentWhenAlertListEmpty() {
        when(alertService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<AlertResponse>> response = alertController.getAllAlerts();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldReturnPaginatedAlerts() {
        List<AlertResponse> page = List.of(new AlertResponse(1L, "Mensagem", LocalDateTime.now(), 1L, "Evento"));
        when(alertService.findAllPaginated(any(Pageable.class))).thenReturn(new PageImpl<>(page));

        ResponseEntity<List<AlertResponse>> response = alertController.getPaginatedAlerts(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnNoContentForEmptyPaginatedAlerts() {
        when(alertService.findAllPaginated(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        ResponseEntity<List<AlertResponse>> response = alertController.getPaginatedAlerts(PageRequest.of(0, 10));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldCreateAlert() {
        AlertRequest request = new AlertRequest(1L, "Mensagem", LocalDateTime.now());
        AlertResponse created = new AlertResponse(1L, "Mensagem", LocalDateTime.now(), 1L, "Evento");

        when(alertService.create(request)).thenReturn(created);

        ResponseEntity<AlertResponse> response = alertController.createAlert(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(created, response.getBody());
    }

    @Test
    void shouldUpdateAlert() {
        AlertRequest request = new AlertRequest(1L, "Mensagem", LocalDateTime.now());
        AlertResponse updated = new AlertResponse(1L, "Mensagem", LocalDateTime.now(), 1L, "Evento");

        when(alertService.update(eq(1L), eq(request))).thenReturn(updated);

        ResponseEntity<AlertResponse> response = alertController.updateAlert(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    void shouldDeleteAlert() {
        doNothing().when(alertService).delete(1L);

        ResponseEntity<Void> response = alertController.deleteAlert(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldDeleteOldAlerts() {
        doNothing().when(alertService).deleteOlderThanDays(7);

        ResponseEntity<Void> response = alertController.deleteOldAlerts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(alertService).deleteOlderThanDays(7);
    }
}

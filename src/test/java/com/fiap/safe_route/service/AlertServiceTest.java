package com.fiap.safe_route.service;

import com.fiap.safe_route.dto.alert.AlertRequest;
import com.fiap.safe_route.dto.alert.AlertResponse;
import com.fiap.safe_route.exception.NotFoundException;
import com.fiap.safe_route.model.Alert;
import com.fiap.safe_route.model.Event;
import com.fiap.safe_route.model.EventType;
import com.fiap.safe_route.repository.AlertRepository;
import com.fiap.safe_route.repository.EventRepository;
import com.fiap.safe_route.service.messaging.AlertProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private AlertProducer alertProducer;

    @InjectMocks
    private AlertService alertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAlert() {
        Event event = Event.builder()
                .id(1L)
                .eventType(EventType.builder().name("Flood").build())
                .description("Heavy rain")
                .build();

        AlertRequest request = new AlertRequest(1L, "Test alert", LocalDateTime.now());

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(alertRepository.save(any(Alert.class))).thenAnswer(invocation -> {
            Alert alert = invocation.getArgument(0);
            alert.setId(1L);
            return alert;
        });

        AlertResponse response = alertService.create(request);

        assertNotNull(response);
        assertEquals("Test alert", response.getMessage());
        verify(alertProducer).sendAlert(any(AlertResponse.class));
    }

    @Test
    void shouldThrowWhenEventNotFoundOnCreate() {
        AlertRequest request = new AlertRequest(99L, "Mensagem", LocalDateTime.now());
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> alertService.create(request));
    }

    @Test
    void shouldFindAlertById() {
        Alert alert = Alert.builder()
                .id(1L)
                .message("Mensagem")
                .sentAt(LocalDateTime.now())
                .event(Event.builder()
                        .id(1L)
                        .eventType(EventType.builder().name("Flood").build())
                        .description("Alagamento")
                        .build())
                .build();

        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));

        AlertResponse response = alertService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Mensagem", response.getMessage());
    }

    @Test
    void shouldDeleteAlert() {
        when(alertRepository.existsById(1L)).thenReturn(true);

        alertService.delete(1L);

        verify(alertRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentAlert() {
        when(alertRepository.existsById(999L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> alertService.delete(999L));
    }

    @Test
    void shouldReturnPaginatedAlerts() {
        Pageable pageable = PageRequest.of(0, 10);
        Alert alert = Alert.builder()
                .id(1L)
                .message("Mensagem")
                .sentAt(LocalDateTime.now())
                .event(Event.builder().id(1L).eventType(EventType.builder().name("Flood").build()).description("Desc").build())
                .build();
        Page<Alert> page = new PageImpl<>(List.of(alert));

        when(alertRepository.findAll(pageable)).thenReturn(page);

        Page<AlertResponse> result = alertService.findAllPaginated(pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldDeleteOlderAlerts() {
        doNothing().when(alertRepository).deleteBySentAtBefore(any(LocalDateTime.class));
        alertService.deleteOlderThanDays(7);
        verify(alertRepository).deleteBySentAtBefore(any(LocalDateTime.class));
    }
}

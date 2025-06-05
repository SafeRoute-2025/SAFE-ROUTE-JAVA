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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventTypeRepository eventTypeRepository;

    @Mock
    private RiskLevelRepository riskLevelRepository;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private EventProducer eventProducer;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateEventSuccessfully() {
        // Arrange
        EventType eventType = EventType.builder()
                .id(1L)
                .name("Flood")
                .description("Flooding due to rain")
                .build();

        RiskLevel riskLevel = RiskLevel.builder()
                .id(3L)
                .name("High")
                .build();

        EventRequest request = new EventRequest(
                "Flood",
                "Streets flooded after storm",
                new Date(),
                "High",
                -23.5505,
                -46.6333
        );

        when(eventTypeRepository.findByName("Flood")).thenReturn(Optional.of(eventType));
        when(riskLevelRepository.findByName("High")).thenReturn(Optional.of(riskLevel));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
            Event saved = invocation.getArgument(0);
            saved.setId(100L);
            return saved;
        });

        // Act
        EventResponse response = eventService.create(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getEventType()).isEqualTo("Flood");
        assertThat(response.getRiskLevel()).isEqualTo("High");
        assertThat(response.getDescription()).isEqualTo("Streets flooded after storm");

        verify(eventProducer, times(1)).sendEvent(any(EventResponse.class));
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void shouldThrowWhenEventTypeNotFound() {
        // Arrange
        EventRequest request = new EventRequest(
                "UnknownType",
                "Some description",
                new Date(),
                "High",
                0.0,
                0.0
        );

        when(eventTypeRepository.findByName("UnknownType")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> eventService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("EventType not found");
    }

    @Test
    void shouldThrowWhenRiskLevelNotFound() {
        // Arrange
        EventType eventType = EventType.builder()
                .id(1L)
                .name("Flood")
                .build();

        EventRequest request = new EventRequest(
                "Flood",
                "Some description",
                new Date(),
                "UnknownLevel",
                0.0,
                0.0
        );

        when(eventTypeRepository.findByName("Flood")).thenReturn(Optional.of(eventType));
        when(riskLevelRepository.findByName("UnknownLevel")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> eventService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("RiskLevel not found");
    }

    @Test
    void shouldFindEventById() {
        // Arrange
        Event event = Event.builder()
                .id(10L)
                .eventType(EventType.builder().id(1L).name("Flood").build())
                .riskLevel(RiskLevel.builder().id(3L).name("High").build())
                .description("Flooded streets")
                .eventDate(new Date())
                .latitude(-23.0)
                .longitude(-46.0)
                .build();

        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));

        // Act
        EventResponse response = eventService.findById(10L);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getEventType()).isEqualTo("Flood");
    }

    @Test
    void shouldThrowWhenEventNotFoundById() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.findById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Event not found");
    }

    @Test
    void shouldDeleteEventAndAlerts() {
        Long eventId = 1L;
        when(eventRepository.existsById(eventId)).thenReturn(true);

        eventService.delete(eventId);

        verify(alertRepository, times(1)).deleteAllByEventId(eventId);
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentEvent() {
        when(eventRepository.existsById(42L)).thenReturn(false);

        assertThatThrownBy(() -> eventService.delete(42L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Event not found");
    }
}

package com.fiap.safe_route.service;

import com.fiap.safe_route.dto.alert.AlertRequest;
import com.fiap.safe_route.dto.alert.AlertResponse;
import com.fiap.safe_route.exception.NotFoundException;
import com.fiap.safe_route.model.Alert;
import com.fiap.safe_route.model.Event;
import com.fiap.safe_route.repository.AlertRepository;
import com.fiap.safe_route.repository.EventRepository;
import com.fiap.safe_route.service.messaging.AlertProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    private final AlertRepository alertRepository;
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final AlertProducer alertProducer;
    public AlertService(AlertRepository alertRepository,
                        EventRepository eventRepository,
                        AlertProducer alertProducer) {
        this.alertRepository = alertRepository;
        this.eventRepository = eventRepository;
        this.alertProducer = alertProducer;
    }

    private AlertResponse toResponse(Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getMessage(),
                alert.getSentAt(),
                alert.getEvent().getId(),
                alert.getEvent().getEventType().getName() + " - " + alert.getEvent().getDescription()
        );
    }

    public AlertResponse findById(Long id) {
        return alertRepository.findById(id)
                .map(this::toResponse)
                .orElse(null);
    }

    public List<AlertResponse> findAll() {
        return alertRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public AlertResponse create(AlertRequest request) {
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new NotFoundException("Evento não encontrado"));

        Alert alert = Alert.builder()
                .message(request.message())
                .sentAt(request.sentAt() != null ? request.sentAt() : LocalDateTime.now())
                .event(event)
                .build();

        alertRepository.save(alert);

        AlertResponse response = toResponse(alert);
        alertProducer.sendAlert(response);
        return response;
    }


    public AlertResponse update(Long id, AlertRequest request) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Alerta não encontrado"));

        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new NotFoundException("Evento não encontrado"));

        alert.setMessage(request.message());
        alert.setSentAt(request.sentAt() != null ? request.sentAt() : LocalDateTime.now());
        alert.setEvent(event);

        alertRepository.save(alert);
        AlertResponse response = toResponse(alert);
        alertProducer.sendAlert(response);
        return response;
    }



    public void delete(Long id) {
        if (!alertRepository.existsById(id)) {
            throw new NotFoundException("Alerta não encontrado");
        }
        alertRepository.deleteById(id);
    }


    public Page<AlertResponse> findAllPaginated(Pageable pageable) {
        return alertRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional
    public void deleteOlderThanDays(int days) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(days);
        alertRepository.deleteBySentAtBefore(threshold);
    }

    public void saveOrUpdateFromMessage(AlertResponse response) {
        Event event = eventRepository.findById(response.getEventId())
                .orElseThrow(() -> new NotFoundException("Evento não encontrado para o alerta"));

        Alert alert;

        if (response.getId() != null && alertRepository.existsById(response.getId())) {
            alert = alertRepository.findById(response.getId())
                    .orElseThrow(() -> new NotFoundException("Alerta não encontrado"));
            alert.setMessage(response.getMessage());
            alert.setSentAt(response.getSentAt());
            alert.setEvent(event);
        } else {
            alert = Alert.builder()
                    .message(response.getMessage())
                    .sentAt(response.getSentAt())
                    .event(event)
                    .build();
        }
        alertRepository.save(alert);
    }

}

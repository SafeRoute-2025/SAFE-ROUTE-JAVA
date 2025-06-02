package com.fiap.safe_route.service;

import com.fiap.safe_route.dto.alert.AlertRequest;
import com.fiap.safe_route.dto.alert.AlertResponse;
import com.fiap.safe_route.exception.NotFoundException;
import com.fiap.safe_route.model.Alert;
import com.fiap.safe_route.model.Event;
import com.fiap.safe_route.repository.AlertRepository;
import com.fiap.safe_route.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final EventRepository eventRepository;

    public AlertService(AlertRepository alertRepository,
                        EventRepository eventRepository) {
        this.alertRepository = alertRepository;
        this.eventRepository = eventRepository;
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
        Alert alert = new Alert();
        alert.setMessage(request.message());

        LocalDateTime sentAt = request.sentAt() != null ? request.sentAt() : LocalDateTime.now();
        alert.setSentAt(sentAt);

        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new NotFoundException("Evento não encontrado"));
        alert.setEvent(event);

        return toResponse(alertRepository.save(alert));
    }


    public AlertResponse update(Long id, AlertRequest request) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Alerta não encontrado"));

        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new NotFoundException("Evento não encontrado"));

        alert.setMessage(request.message());
        alert.setEvent(event);
        alert.setSentAt(request.sentAt() != null ? request.sentAt() : LocalDateTime.now());

        return toResponse(alertRepository.save(alert));
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

}

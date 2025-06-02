package com.fiap.safe_route.repository;

import com.fiap.safe_route.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    @Transactional
    void deleteAllByEventId(Long eventId);
    void deleteBySentAtBefore(LocalDateTime threshold);

}

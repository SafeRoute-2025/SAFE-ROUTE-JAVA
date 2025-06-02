package com.fiap.safe_route.repository;

import com.fiap.safe_route.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {
}

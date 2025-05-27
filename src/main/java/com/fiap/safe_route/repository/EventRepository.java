package com.fiap.safe_route.repository;

import com.fiap.safe_route.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}

package com.fiap.safe_route.repository;

import com.fiap.safe_route.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    boolean existsByName(String name);

    Optional<EventType> findByName(String name);
}

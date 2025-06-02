package com.fiap.safe_route.repository;

import com.fiap.safe_route.model.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RiskLevelRepository extends JpaRepository<RiskLevel, Long> {
    Optional<RiskLevel> findByName(String name);

}

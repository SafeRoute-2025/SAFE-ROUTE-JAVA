package com.fiap.safe_route.repository;

import com.fiap.safe_route.model.SafePlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SafePlaceRepository extends JpaRepository<SafePlace, Long> {
}
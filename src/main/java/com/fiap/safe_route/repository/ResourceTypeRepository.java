package com.fiap.safe_route.repository;

import com.fiap.safe_route.model.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ResourceTypeRepository extends JpaRepository<ResourceType, Long> {
     Optional<ResourceType> findByName(String name);

    List<ResourceType> findByNameContainingIgnoreCase(String query);
}

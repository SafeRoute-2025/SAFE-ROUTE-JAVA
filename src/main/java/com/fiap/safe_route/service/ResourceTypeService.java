package com.fiap.safe_route.service;


import com.fiap.safe_route.dto.resourcetype.ResourceTypeResponse;
import com.fiap.safe_route.dto.risklevel.RiskLevelResponse;
import com.fiap.safe_route.model.ResourceType;
import com.fiap.safe_route.repository.ResourceTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceTypeService {

    private final ResourceTypeRepository resourceTypeRepository;

    public ResourceTypeService(ResourceTypeRepository resourceTypeRepository) {
        this.resourceTypeRepository = resourceTypeRepository;
    }

    private ResourceTypeResponse toResponse(ResourceType resourceType) {
        return new ResourceTypeResponse(
                resourceType.getId(),
                resourceType.getName(),
                resourceType.getDescription()
        );
    }

    public ResourceTypeResponse findById(Long id) {
        return resourceTypeRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Resource Type not found"));
    }

    public ResourceTypeResponse findByName(String name) {
        return resourceTypeRepository.findByName(name)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Resource Type not found"));
    }

    public List<ResourceTypeResponse> findAll() {
        return resourceTypeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Page<ResourceTypeResponse> findAllPaginated (Pageable pageable) {
        return resourceTypeRepository.findAll(pageable).map(this::toResponse);
    }

    public List<String> searchByName(String query) {
        if (query == null || query.isEmpty()) {
            return List.of();
        }
        return resourceTypeRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(ResourceType::getName)
                .collect(Collectors.toList());
    }

    public List<String> getAllNames() {
        return resourceTypeRepository.findAll().stream()
                .map(ResourceType::getName)
                .collect(Collectors.toList());
    }

}

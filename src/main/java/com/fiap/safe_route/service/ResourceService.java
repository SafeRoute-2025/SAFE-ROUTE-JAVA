package com.fiap.safe_route.service;


import com.fiap.safe_route.dto.resource.ResourceRequest;
import com.fiap.safe_route.dto.resource.ResourceResponse;
import com.fiap.safe_route.model.Resource;
import com.fiap.safe_route.model.SafePlace;
import com.fiap.safe_route.repository.ResourceRepository;
import com.fiap.safe_route.repository.ResourceTypeRepository;
import com.fiap.safe_route.repository.SafePlaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceTypeRepository resourceTypeRepository;
    private final SafePlaceRepository safePlaceRepository;

    public ResourceService(ResourceRepository resourceRepository,
                           ResourceTypeRepository resourceTypeRepository,
                           SafePlaceRepository safePlaceRepository) {
        this.resourceRepository = resourceRepository;
        this.resourceTypeRepository = resourceTypeRepository;
        this.safePlaceRepository = safePlaceRepository;
    }

    private ResourceResponse toResponse(Resource resource) {
        return new ResourceResponse(
                resource.getId(),
                resource.getResourceType().getName(),
                resource.getAvailableQuantity(),
                resource.getSafePlace().getId(),
                resource.getSafePlace().getName()
        );
    }


    public List<ResourceResponse> findAll() {
        return resourceRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ResourceResponse findById(Long id) {
        return resourceRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
    }


    public Page<ResourceResponse> findAllPaginated(Pageable pageable) {
        return resourceRepository.findAll(pageable).map(this::toResponse);
    }


    public Page<ResourceResponse> findBySafePlaceId(Long idSafePlace, Pageable pageable) {
        SafePlace safePlace = safePlaceRepository.findById(idSafePlace)
                .orElseThrow(() -> new RuntimeException("Safe Place not found"));

        return resourceRepository.findBySafePlaceId(idSafePlace, pageable)
                .map(this::toResponse);
    }

    public void create(ResourceRequest request) {
        Resource resource = new Resource();
        resource.setResourceType(resourceTypeRepository.findById(request.resourceTypeId())
                .orElseThrow(() -> new RuntimeException("Resource Type not found")));
        resource.setAvailableQuantity(request.availableQuantity());
        resource.setSafePlace(safePlaceRepository.findById(request.safePlaceId())
                .orElseThrow(() -> new RuntimeException("Safe Place not found")));

        resourceRepository.save(resource);
    }

    public void update(Long id, ResourceRequest request) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        resource.setResourceType(resourceTypeRepository.findById(request.resourceTypeId())
                .orElseThrow(() -> new RuntimeException("Resource Type not found")));
        resource.setAvailableQuantity(request.availableQuantity());
        resource.setSafePlace(safePlaceRepository.findById(request.safePlaceId())
                .orElseThrow(() -> new RuntimeException("Safe Place not found")));

        resourceRepository.save(resource);
    }

    public void delete(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
        resourceRepository.delete(resource);
    }
}

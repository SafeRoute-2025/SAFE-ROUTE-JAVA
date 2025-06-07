package com.fiap.safe_route.service;

import com.fiap.safe_route.dto.safeplace.SafePlaceRequest;
import com.fiap.safe_route.dto.safeplace.SafePlaceResponse;
import com.fiap.safe_route.model.SafePlace;
import com.fiap.safe_route.repository.ResourceRepository;
import com.fiap.safe_route.repository.SafePlaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SafePlaceService {

    private final SafePlaceRepository repository;
    private final ResourceRepository resourceRepository;

    public SafePlaceService(SafePlaceRepository repository, ResourceRepository resourceRepository) {
        this.repository = repository;
        this.resourceRepository = resourceRepository;
    }

    public SafePlaceResponse create(SafePlaceRequest request) {
        SafePlace place = SafePlace.builder()
                .name(request.name())
                .address(request.address())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .capacity(request.capacity())
                .build();
        return toResponse(repository.save(place));
    }

    public SafePlaceResponse findById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Safe Place not found"));
    }

    public Page<SafePlaceResponse> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public List<SafePlaceResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Safe Place not found");
        }

        repository.deleteById(id);
    }

    @Transactional
    public SafePlaceResponse update(Long id, SafePlaceRequest request) {
        SafePlace place = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Safe Place not found"));

        place.setName(request.name());
        place.setAddress(request.address());
        place.setLatitude(request.latitude());
        place.setLongitude(request.longitude());
        place.setCapacity(request.capacity());

        return toResponse(repository.save(place));
    }

    private SafePlaceResponse toResponse(SafePlace place) {
        return new SafePlaceResponse(
                place.getId(),
                place.getName(),
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude(),
                place.getCapacity()
        );
    }
}

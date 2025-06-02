package com.fiap.safe_route.controller.api;

import com.fiap.safe_route.dto.resource.ResourceRequest;
import com.fiap.safe_route.dto.resource.ResourceResponse;
import com.fiap.safe_route.service.ResourceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@Tag(name = "Resource", description = "Resource management and lookup")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ResourceResponse>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ResourceResponse>> getAllPaginated(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.findAllPaginated(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponse> getById(@PathVariable Long id) {
        try {
            ResourceResponse resource = service.findById(id);
            return ResponseEntity.ok(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/by-safe-place/{idSafePlace}")
    public ResponseEntity<List<ResourceResponse>> getBySafePlace(
            @PathVariable Long idSafePlace,
            @PageableDefault(size = 10, sort = "resourceType") Pageable pageable) {
        Page<ResourceResponse> resources = service.findBySafePlaceId(idSafePlace, pageable);
        return ResponseEntity.ok(resources.getContent());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ResourceRequest request) {
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody ResourceRequest request) {
        service.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

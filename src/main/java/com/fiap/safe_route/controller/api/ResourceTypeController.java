package com.fiap.safe_route.controller.api;

import com.fiap.safe_route.dto.resourcetype.ResourceTypeResponse;
import com.fiap.safe_route.service.ResourceTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resource-types")
@Tag(name = "Resource Type", description = "Resource type lookup")
public class ResourceTypeController {

    private final ResourceTypeService service;

    public ResourceTypeController(ResourceTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ResourceTypeResponse>> getAll() {
        try {
            List<ResourceTypeResponse> resourceTypes = service.findAll();
            if (resourceTypes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(resourceTypes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ResourceTypeResponse>> getAllPaginated(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        try {
            Page<ResourceTypeResponse> resourceTypes =
                    service.findAllPaginated(pageable);
            if (resourceTypes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(resourceTypes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceTypeResponse> getById(@PathVariable Long id) {
        try {
            ResourceTypeResponse resourceType = service.findById(id);
            return ResponseEntity.ok(resourceType);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchResourceTypes(@RequestParam String query) {
        List<String> suggestions = service.searchByName(query);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllResourceTypeNames() {
        List<String> allNames = service.getAllNames();
        return ResponseEntity.ok(allNames);
    }


}

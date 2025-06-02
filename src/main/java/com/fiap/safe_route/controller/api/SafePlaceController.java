package com.fiap.safe_route.controller.api;

import com.fiap.safe_route.dto.safeplace.SafePlaceRequest;
import com.fiap.safe_route.dto.safeplace.SafePlaceResponse;
import com.fiap.safe_route.service.SafePlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/safe-places")
@Tag(name = "Safe Place", description = "Safe Place API")
public class SafePlaceController {

    private final SafePlaceService service;

    public SafePlaceController(SafePlaceService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new safe place")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Safe place created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<SafePlaceResponse> create(@RequestBody @Valid SafePlaceRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get all safe places (paginated)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Safe places retrieved"),
            @ApiResponse(responseCode = "204", description = "No content")
    })
    @GetMapping
    public ResponseEntity<Page<SafePlaceResponse>> getAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<SafePlaceResponse> safePlaces = service.findAllPaged(pageable);
        if (safePlaces.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(safePlaces);
    }

    @Operation(summary = "Get safe place by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Safe place found"),
            @ApiResponse(responseCode = "404", description = "Safe place not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SafePlaceResponse> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update safe place by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Safe place updated"),
            @ApiResponse(responseCode = "404", description = "Safe place not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SafePlaceResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid SafePlaceRequest request) {
        try {
            SafePlaceResponse updated = service.update(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete safe place by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Safe place deleted"),
            @ApiResponse(responseCode = "404", description = "Safe place not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

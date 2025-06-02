package com.fiap.safe_route.controller.api;

import com.fiap.safe_route.dto.alert.AlertRequest;
import com.fiap.safe_route.dto.alert.AlertResponse;
import com.fiap.safe_route.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@Tag(name = "Alert", description = "Alert management and retrieval")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get alert by ID", description = "Retrieve an alert by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alert found"),
            @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    public ResponseEntity<AlertResponse> getAlertById(@PathVariable Long id) {
        try {
            AlertResponse alert = alertService.findById(id);
            return ResponseEntity.ok(alert);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all alerts", description = "Retrieve a list of all alerts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alerts retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No alerts found")
    })
    public ResponseEntity<List<AlertResponse>> getAllAlerts() {
        try {
            List<AlertResponse> alerts = alertService.findAll();
            if (alerts.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(alerts);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/page")
    @Operation(summary = "Get paginated alerts", description = "Retrieve a paginated list of alerts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated alerts retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No alerts found")
    })
    public ResponseEntity<List<AlertResponse>> getPaginatedAlerts(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<AlertResponse> alertsPage = alertService.findAllPaginated(pageable);
        if (alertsPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(alertsPage.getContent());
    }

    @PostMapping
    @Operation(summary = "Create a new alert", description = "Create a new alert with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Alert created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid alert data")
    })
    public ResponseEntity<AlertResponse> createAlert(@RequestBody @Valid AlertRequest alertRequest) {
        try {
            AlertResponse createdAlert = alertService.create(alertRequest);
            return ResponseEntity.status(201).body(createdAlert);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an alert by ID", description = "Update an existing alert with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alert updated successfully"),
            @ApiResponse(responseCode = "404", description = "Alert not found"),
            @ApiResponse(responseCode = "400", description = "Invalid alert data")
    })
    public ResponseEntity<AlertResponse> updateAlert(
            @PathVariable Long id, AlertRequest alertRequest) {
        try {
            AlertResponse updatedAlert = alertService.update(id, alertRequest);
            return ResponseEntity.ok(updatedAlert);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an alert by ID", description = "Delete an existing alert by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Alert deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {
        try {
            alertService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/older-than-7-days")
    @Operation(summary = "Delete alerts older than 7 days", description = "Remove all alerts sent more than 7 days ago")
    @ApiResponse(responseCode = "200", description = "Old alerts deleted successfully")
    public ResponseEntity<Void> deleteOldAlerts() {
        alertService.deleteOlderThanDays(7);
        return ResponseEntity.ok().build();
    }


}

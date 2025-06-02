package com.fiap.safe_route.controller.api;

import com.fiap.safe_route.dto.risklevel.RiskLevelResponse;
import com.fiap.safe_route.dto.user.UserResponse;
import com.fiap.safe_route.service.RiskLevelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-levels")
@Tag(name = "Risk Level", description = "Risk level lookup")
public class RiskLevelController {

    private final RiskLevelService service;

    public RiskLevelController(RiskLevelService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RiskLevelResponse>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<RiskLevelResponse>> getAllPaginated(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<RiskLevelResponse> riskLevels = service.findAllPaginated(pageable);
        if (riskLevels.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(riskLevels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RiskLevelResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}

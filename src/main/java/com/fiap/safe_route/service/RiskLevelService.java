package com.fiap.safe_route.service;

import com.fiap.safe_route.dto.risklevel.RiskLevelResponse;
import com.fiap.safe_route.model.RiskLevel;
import com.fiap.safe_route.repository.RiskLevelRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiskLevelService {

    private final RiskLevelRepository repository;

    public RiskLevelService(RiskLevelRepository repository) {
        this.repository = repository;
    }

    private RiskLevelResponse toResponse(RiskLevel riskLevel) {
        return new RiskLevelResponse(
                riskLevel.getId(),
                riskLevel.getName()
        );
    }

    public RiskLevelResponse findById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Risk Level not found"));
    }

    public List<RiskLevelResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Page<RiskLevelResponse> findAllPaginated(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }




}

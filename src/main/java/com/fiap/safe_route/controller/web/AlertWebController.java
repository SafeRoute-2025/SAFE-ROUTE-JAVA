package com.fiap.safe_route.controller.web;

import com.fiap.safe_route.dto.alert.AlertResponse;
import com.fiap.safe_route.service.AlertService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/alerts")
public class AlertWebController {

    private final AlertService alertService;

    public AlertWebController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public String listAlerts(@PageableDefault(size = 15, sort = "sentAt", direction = Sort.Direction.DESC)
                             Pageable pageable,
                             Model model) {
        Page<AlertResponse> page = alertService.findAllPaginated(pageable);
        model.addAttribute("alertsPage", page);
        return "alerts";
    }
}

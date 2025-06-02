package com.fiap.safe_route.controller.web;

import com.fiap.safe_route.service.SafePlaceService;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/safe-places")
public class SafePlaceWebController {

    private final SafePlaceService service;

    public SafePlaceWebController(SafePlaceService service) {
        this.service = service;
    }

    @GetMapping
    public String listSafePlaces(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            Model model) {

        Page<?> safePlacesPage = service.findAllPaged(pageable);
        model.addAttribute("safePlacesPage", safePlacesPage);

        return "safeplacelist";
    }
}

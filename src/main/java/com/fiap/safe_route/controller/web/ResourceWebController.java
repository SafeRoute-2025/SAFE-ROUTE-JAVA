package com.fiap.safe_route.controller.web;

import com.fiap.safe_route.dto.resource.ResourceResponse;
import com.fiap.safe_route.dto.safeplace.SafePlaceResponse;
import com.fiap.safe_route.service.ResourceService;
import com.fiap.safe_route.service.SafePlaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/resources")
public class ResourceWebController {

    private final ResourceService resourceService;
    private final SafePlaceService safePlaceService;

    public ResourceWebController(ResourceService resourceService,
                                 SafePlaceService safePlaceService) {
        this.resourceService = resourceService;
        this.safePlaceService = safePlaceService;
    }

    @GetMapping
    public String listResources(@RequestParam(required = false) Long safePlaceId,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {
        model.addAttribute("safePlaceId", safePlaceId);
        model.addAttribute("safePlaces", safePlaceService.findAll());

        if (safePlaceId != null) {
            Page<ResourceResponse> resourcesPage = resourceService.findBySafePlaceId(safePlaceId, PageRequest.of(page, 10));
            model.addAttribute("resourcesPage", resourcesPage);
        }

        return "resourceList";
    }





    // Você pode adicionar POST, PUT e DELETE aqui se quiser manipular direto via formulário

}

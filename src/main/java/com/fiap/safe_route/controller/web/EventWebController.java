package com.fiap.safe_route.controller.web;

import com.fiap.safe_route.dto.event.EventRequest;
import com.fiap.safe_route.dto.event.EventResponse;
import com.fiap.safe_route.dto.eventtype.EventTypeResponse;
import com.fiap.safe_route.dto.risklevel.RiskLevelResponse;
import com.fiap.safe_route.service.EventService;
import com.fiap.safe_route.service.EventTypeService;
import com.fiap.safe_route.service.RiskLevelService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
@RequestMapping("/events")
public class EventWebController {

    private final EventService eventService;
    private final EventTypeService eventTypeService;
    private final RiskLevelService riskLevelService;

    public EventWebController(EventService eventService,
                              EventTypeService eventTypeService,
                              RiskLevelService riskLevelService) {
        this.eventService = eventService;
        this.eventTypeService = eventTypeService;
        this.riskLevelService = riskLevelService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping
    public String listEvents(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").descending());
        Page<EventResponse> eventsPage = eventService.findAll(pageable);

        model.addAttribute("eventsPage", eventsPage);
        model.addAttribute("event", new EventRequest("", "", new Date(), "", null, null));
        model.addAttribute("eventTypes", eventTypeService.findAll().stream().map(EventTypeResponse::getName).toList());
        model.addAttribute("riskLevels", riskLevelService.findAll().stream().map(RiskLevelResponse::getName).toList());
        return "eventList";
    }

    @GetMapping("/{id}")
    public String eventDetails(@PathVariable Long id, Model model) {
        EventResponse event = eventService.findById(id);
        if (event == null) return "redirect:/events";
        model.addAttribute("event", event);
        return "event/details";
    }

    @PostMapping("/{id}/edit")
    public String updateEvent(@PathVariable Long id, @ModelAttribute EventRequest event) {
        eventService.update(id, event);
        return "redirect:/events";
    }

    @PostMapping("/{id}/delete")
    public String deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
        return "redirect:/events";
    }

    @PostMapping
    public String createEvent(@ModelAttribute @Valid EventRequest event,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("eventTypes", eventTypeService.findAll().stream().map(EventTypeResponse::getName).toList());
            model.addAttribute("riskLevels", riskLevelService.findAll().stream().map(RiskLevelResponse::getName).toList());
            return "event/form"; // fallback (caso precise reaproveitar)
        }

        eventService.create(event);
        redirectAttributes.addFlashAttribute("success", "Evento criado com sucesso!");
        return "redirect:/events";
    }
}

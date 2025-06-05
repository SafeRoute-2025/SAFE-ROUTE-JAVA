package com.fiap.safe_route.controller.web;

import com.fiap.safe_route.dto.alert.AlertResponse;
import com.fiap.safe_route.service.AiService;
import com.fiap.safe_route.service.AlertService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Controller
public class HomeController {
    private final AlertService alertService;
    private final AiService aiService;

    public HomeController(AlertService alertService, AiService aiService) {
        this.alertService = alertService;
        this.aiService = aiService;
    }

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal Object principal) {
        List<AlertResponse> latestAlerts = alertService.findAll()
                .stream()
                .sorted(Comparator.comparing(AlertResponse::getSentAt).reversed())
                .limit(5)
                .toList();

        model.addAttribute("alerts", latestAlerts);

        Map<String, Object> stats = new HashMap<>();
        stats.put("alertCount", alertService.findAll().size());
        stats.put("safePlaceCount", 0);
        stats.put("realTime", true);
        model.addAttribute("stats", stats);

        String userName = "Usuário";
        if (principal instanceof OAuth2User oauthUser) {
            userName = oauthUser.getAttribute("name");
        } else if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            userName = userDetails.getUsername(); // geralmente é o email
        }
        model.addAttribute("userName", userName);

        return "index";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/api/dica")
    public ResponseEntity<String> novaDica(HttpServletRequest request) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        Locale locale = (localeResolver != null) ? localeResolver.resolveLocale(request) : request.getLocale();
        String dica = aiService.gerarDicaSeguranca(locale);
        return ResponseEntity.ok(dica);
    }

    @GetMapping("/change-language")
    public String changeLanguage(@RequestParam("lang") String lang,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver != null) {
            localeResolver.setLocale(request, response, new Locale(lang));
        }
        // Redireciona para a página anterior (referer)
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

}

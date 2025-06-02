package com.fiap.safe_route.controller.web;

import com.fiap.safe_route.dto.user.UserRequest;
import com.fiap.safe_route.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRequest("", "", "", ""));
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") @Valid
                                  UserRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.create(request);
            return "redirect:/login?registered=true";
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.user", "Email já está em uso.");
            return "register";
        }
    }
}

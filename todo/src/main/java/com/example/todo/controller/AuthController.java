package com.example.todo.controller;

import com.example.todo.entity.User;
import com.example.todo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username đã tồn tại!");
            return "register";
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "register";
        }
        userService.register(user);
        return "redirect:/login?registerSuccess";
    }
}

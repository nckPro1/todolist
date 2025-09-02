package com.example.todo.controller;

import com.example.todo.entity.User;
import com.example.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Form login
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    // Form register
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register"; // register.html
    }

    // Submit register
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("user") User user,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp");
            return "register";
        }

        try {
            userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
            return "redirect:/auth/login?registerSuccess";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}

package com.example.todo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        String username = authentication.getName(); // lấy username từ SecurityContext
        model.addAttribute("username", username);

        // sau này bạn có thể load danh sách công việc của user từ DB
        // ví dụ: model.addAttribute("todos", todoService.findByUser(username));

        return "home"; // home.html
    }
}

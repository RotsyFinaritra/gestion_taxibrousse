package com.itu.taxibrousse.controller.backoffice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @GetMapping("/admin/login")
    public String showLoginForm(Model model) {
        return "backoffice/login";
    }

    @PostMapping("/admin/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {
        // Simple hardcoded admin check (replace with DB check if needed)
        if ("admin".equals(username) && "admin123".equals(password)) {
            session.setAttribute("adminUser", username);
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Identifiants invalides");
            return "backoffice/login";
        }
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}

package com.itu.taxibrousse.controller.backoffice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    
    @GetMapping
    public String dashboard() {
        return "backoffice/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard_page() {
        return "backoffice/dashboard";
    }
}

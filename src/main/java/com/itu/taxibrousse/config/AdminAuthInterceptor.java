package com.itu.taxibrousse.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.*;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();
        // Autoriser l'accès à la page de login et aux ressources statiques
        if (uri.startsWith("/admin/login") || uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images")) {
            return true;
        }
        if (session != null && session.getAttribute("adminUser") != null) {
            return true;
        }
        response.sendRedirect("/admin/login");
        return false;
    }
}

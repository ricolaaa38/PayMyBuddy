package com.openclassrooms.paymybuddy.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        if (statusCode == null) {
            statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        }

       
            if (statusCode!= null && (statusCode == 404 || statusCode == 403)) {
                model.addAttribute("errorMessage", "Page non trouvée (404).");
                return "error/404";
            }
        
        model.addAttribute("errorMessage", "Une erreur est survenue.");
        return "error/default";
    }
}
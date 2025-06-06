package com.openclassrooms.paymybuddy.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller for handling the Error page (404)
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Handles errors and returns the appropriate error page.
     *
     * @param request the HttpServletRequest containing error information
     * @param model   the Model to pass attributes to the view
     * @return the name of the error view
     */
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
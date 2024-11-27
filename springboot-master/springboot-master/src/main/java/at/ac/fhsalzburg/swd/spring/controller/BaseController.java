package at.ac.fhsalzburg.swd.spring.controller;

import org.springframework.ui.Model;

public class BaseController {

    protected void addErrorMessage(String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
    }
}

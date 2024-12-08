package at.ac.fhsalzburg.swd.spring.controller;

import org.springframework.ui.Model;

public class BaseController {

    protected void addErrorMessage(String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
    }

	protected void addSuccessMessage(String successMessage, Model model) {
		model.addAttribute("successMessage", successMessage);
	}
}

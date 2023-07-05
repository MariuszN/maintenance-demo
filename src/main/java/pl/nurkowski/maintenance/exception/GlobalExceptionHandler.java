package pl.nurkowski.maintenance.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCarBrandException.class)
    public String handleInvalidCarBrandException(InvalidCarBrandException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error-page";
    }

}

//package com.finalYearProject.queueManagement.exception;
//
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    public String handleAllExceptions(Exception ex, WebRequest request, Model model) {
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "error";
//    }
//}
//

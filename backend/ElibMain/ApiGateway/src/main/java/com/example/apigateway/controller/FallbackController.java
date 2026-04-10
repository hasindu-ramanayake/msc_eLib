package com.example.apigateway.controller;
 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
@RequestMapping("/fallback")
public class FallbackController {
 
    @GetMapping("/user-service")
    public String userServiceFallback() {
        return "The User Service is currently unavailable. Please try again later.";
    }
}

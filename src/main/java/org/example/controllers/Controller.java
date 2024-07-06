package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import org.example.services.*;

@RequiredArgsConstructor
public class Controller {
    private final Service service;

    @GetMapping("/")
    public String main_page() {
        return "main-page";
    }


}

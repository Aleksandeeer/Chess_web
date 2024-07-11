package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.example.services.*;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class Controller {
    private final Service service = new Service();

    @GetMapping("/")
    public String main_page(Model model) {
        model.addAttribute(service.getGame()); // ! TODO: Поменять передачу аттрибута (или чекнуть как работает эта)
        return "main-page";
    }


}

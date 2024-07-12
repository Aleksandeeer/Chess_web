package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.models.FigurePositionLetterEnum;
import org.example.models.FigurePositionNumberEnum;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import org.example.services.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class Controller {
    private final Service service = new Service();

    private final ResourceLoader resourceLoader;

    @GetMapping("/")
    public String main_page(Model model) {
        model.addAttribute("BlackFiguresList", service.getGame().getBlackFiguresList()); // ! TODO: Поменять передачу аттрибута (или чекнуть как работает эта)
        model.addAttribute("WhiteFiguresList", service.getGame().getWhiteFiguresList());
        model.addAttribute("FigurePositionLetterEnum", FigurePositionLetterEnum.values());
        model.addAttribute("FigurePositionNumberEnum", FigurePositionNumberEnum.values());
        return "main-page";
    }

    // * МЕТОД ДЛЯ ОТДАЧИ ИЗОБРАЖЕНИЙ
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        try {
            Resource resource = resourceLoader.getResource("classpath:/images/" + filename);
            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Paths.get(resource.getURI())));
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

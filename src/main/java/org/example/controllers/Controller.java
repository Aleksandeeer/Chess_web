package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.models.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import org.example.services.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class Controller {
    private final GameState game = new GameState();

    private final ResourceLoader resourceLoader;

    // * ИГРА
    @GetMapping("/")
    public String main_page(Model model) {
        model.addAttribute("BlackFiguresList", game.getBlackFiguresList()); // ! TODO: Поменять передачу аттрибута (или чекнуть как работает эта)
        model.addAttribute("WhiteFiguresList", game.getWhiteFiguresList());
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

    // * получения возможных ходов
    @GetMapping("/possible-moves/{position}")
    public ResponseEntity<List<String>> getPossibleMoves(@PathVariable("position") String position) {
        char letter = position.charAt(0);
        int number = Character.getNumericValue(position.charAt(1));

        FigurePositionLetterEnum col = FigurePositionLetterEnum.valueOf(Character.toString(letter).toUpperCase());
        FigurePositionNumberEnum row = FigurePositionNumberEnum.values()[number - 1];

        Figure figure = game.getFigureAtPosition(col, row);

        if (figure != null) {
            List<Move> possibleMoves = game.getPossibleMoves(figure);
            List<String> movePositions = possibleMoves.stream()
                    .map(move -> move.getDestination().toLowerCase())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(movePositions);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/move-figure")
    public ResponseEntity<GameState> moveFigure(@RequestParam("source") String source, @RequestParam("target") String target) {
        try {
            char sourceLetter = source.charAt(0);
            int sourceNumber = Character.getNumericValue(source.charAt(1));
            char targetLetter = target.charAt(0);
            int targetNumber = Character.getNumericValue(target.charAt(1));

            FigurePositionLetterEnum sourceCol = FigurePositionLetterEnum.valueOf(Character.toString(sourceLetter).toUpperCase());
            FigurePositionNumberEnum sourceRow = FigurePositionNumberEnum.values()[sourceNumber - 1];
            FigurePositionLetterEnum targetCol = FigurePositionLetterEnum.valueOf(Character.toString(targetLetter).toUpperCase());
            FigurePositionNumberEnum targetRow = FigurePositionNumberEnum.values()[targetNumber - 1];

            Figure figure = game.getFigureAtPosition(sourceCol, sourceRow);

            if (figure != null) {
                List<Move> possibleMoves = game.getPossibleMoves(figure);
                boolean isValidMove = possibleMoves.stream().anyMatch(move ->
                        move.getNewHorizontalPos() == targetCol && move.getNewVerticalPos() == targetRow);
                // ? Если ход возможен
                if (isValidMove) {
                    // ? Удаление фигуры противника
                    Figure enemyFigure = game.getFigureAtPosition(targetCol, targetRow);
                    if (enemyFigure != null && enemyFigure.getColorFigure() != figure.getColorFigure()) {
                        game.removeFigureAtPosition(targetCol, targetRow, game.getFigureAtPosition(targetCol, targetRow).getColorFigure());
                    }

                    // ? Удаление фигуры с текущей позиции
                    game.removeFigureAtPosition(sourceCol, sourceRow, figure.getColorFigure());

                    // ? Установка на новые координаты
                    figure.setHorizontalPos(targetCol);
                    figure.setVerticalPos(targetRow);

                    // ? Добавление фигуры обратно на поле
                    game.addFigureAtPosition(figure);

                    return ResponseEntity.ok(game);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

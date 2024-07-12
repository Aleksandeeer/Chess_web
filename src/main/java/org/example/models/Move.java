package org.example.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Move {
    private Figure figure;
    private FigurePositionLetterEnum newHorizontalPos;
    private FigurePositionNumberEnum newVerticalPos;

    public Move(Figure figure, FigurePositionLetterEnum newHorizontalPos, FigurePositionNumberEnum newVerticalPos) {
        this.figure = figure;
        this.newHorizontalPos = newHorizontalPos;
        this.newVerticalPos = newVerticalPos;
    }

    // * Метод для получения позиции назначения
    public String getDestination() {
        return newHorizontalPos.toString().toLowerCase() + (newVerticalPos.ordinal() + 1);
    }
}


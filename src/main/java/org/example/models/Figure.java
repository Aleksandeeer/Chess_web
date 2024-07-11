package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Figure {
    FigureColorEnum colorFigure;
    FigureTypeEnum typeFigure;
    FigurePositionLetterEnum horizontalPos;
    FigurePositionNumberEnum verticalPos;

    @Setter
    Boolean isAlive;
}

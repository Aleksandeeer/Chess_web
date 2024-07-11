package org.example.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameState {
    List<Figure> WhiteFiguresList;
    List<Figure> BlackFiguresList;

    Boolean turnOfTheMove;

    public GameState() {
        this.WhiteFiguresList = new ArrayList<>();
        this.BlackFiguresList = new ArrayList<>();
        initializeFigures();
    }

    private void initializeFigures() {
        // * Инициализация белых фигур
        // ? Пешки
        for (FigurePositionLetterEnum letter : FigurePositionLetterEnum.values()) {
            WhiteFiguresList.add(new Figure(FigureColorEnum.WHITE, FigureTypeEnum.PAWN, letter, FigurePositionNumberEnum.TWO, true));
        }

        // ? Ладья
        WhiteFiguresList.add(new Figure(FigureColorEnum.WHITE, FigureTypeEnum.ROCK, FigurePositionLetterEnum.A, FigurePositionNumberEnum.ONE, true));
        WhiteFiguresList.add(new Figure(FigureColorEnum.WHITE, FigureTypeEnum.ROCK, FigurePositionLetterEnum.H, FigurePositionNumberEnum.ONE, true));

        // ? Конь
        WhiteFiguresList.add(new Figure(FigureColorEnum.WHITE, FigureTypeEnum.KNIGHT, FigurePositionLetterEnum.B, FigurePositionNumberEnum.ONE, true));
        WhiteFiguresList.add(new Figure(FigureColorEnum.WHITE, FigureTypeEnum.KNIGHT, FigurePositionLetterEnum.G, FigurePositionNumberEnum.ONE, true));

        // ? Слон
        WhiteFiguresList.add(new Figure(FigureColorEnum.WHITE, FigureTypeEnum.BISHOP, FigurePositionLetterEnum.C, FigurePositionNumberEnum.ONE, true));
        WhiteFiguresList.add(new Figure(FigureColorEnum.WHITE, FigureTypeEnum.BISHOP, FigurePositionLetterEnum.F, FigurePositionNumberEnum.ONE, true));

        // ? Королева
        WhiteFiguresList.add(new Figure(FigureColorEnum.WHITE, FigureTypeEnum.QUEEN, FigurePositionLetterEnum.D, FigurePositionNumberEnum.ONE, true));
        // ? Король
        WhiteFiguresList.add(new Figure(FigureColorEnum.WHITE, FigureTypeEnum.KING, FigurePositionLetterEnum.E, FigurePositionNumberEnum.ONE, true));


        // * Инициализация черных фигур
        // ? Пешки
        for (FigurePositionLetterEnum letter : FigurePositionLetterEnum.values()) {
            BlackFiguresList.add(new Figure(FigureColorEnum.BLACK, FigureTypeEnum.PAWN, letter, FigurePositionNumberEnum.SEVEN, true));
        }

        // ? Ладья
        BlackFiguresList.add(new Figure(FigureColorEnum.BLACK, FigureTypeEnum.ROCK, FigurePositionLetterEnum.A, FigurePositionNumberEnum.EIGHT, true));
        BlackFiguresList.add(new Figure(FigureColorEnum.BLACK, FigureTypeEnum.ROCK, FigurePositionLetterEnum.H, FigurePositionNumberEnum.EIGHT, true));

        // ? Конь
        BlackFiguresList.add(new Figure(FigureColorEnum.BLACK, FigureTypeEnum.KNIGHT, FigurePositionLetterEnum.B, FigurePositionNumberEnum.EIGHT, true));
        BlackFiguresList.add(new Figure(FigureColorEnum.BLACK, FigureTypeEnum.KNIGHT, FigurePositionLetterEnum.G, FigurePositionNumberEnum.EIGHT, true));

        // ? Слон
        BlackFiguresList.add(new Figure(FigureColorEnum.BLACK, FigureTypeEnum.BISHOP, FigurePositionLetterEnum.C, FigurePositionNumberEnum.EIGHT, true));
        BlackFiguresList.add(new Figure(FigureColorEnum.BLACK, FigureTypeEnum.BISHOP, FigurePositionLetterEnum.F, FigurePositionNumberEnum.EIGHT, true));

        // ? Королева
        BlackFiguresList.add(new Figure(FigureColorEnum.BLACK, FigureTypeEnum.QUEEN, FigurePositionLetterEnum.D, FigurePositionNumberEnum.EIGHT, true));
        // ? Король
        BlackFiguresList.add(new Figure(FigureColorEnum.BLACK, FigureTypeEnum.KING, FigurePositionLetterEnum.E, FigurePositionNumberEnum.EIGHT, true));
    }

}

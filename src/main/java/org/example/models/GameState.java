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

    // * ПОЛУЧЕНИЕ ВСЕХ ВОЗМОЖНЫХ ХОДОВ ДЛЯ ЛЮБОЙ ФИГУРЫ
    public List<Move> getPossibleMoves(Figure figure) {
        return switch (figure.getTypeFigure()) {
            case BISHOP -> getBishopMoves(figure);
            case KING -> getKingMoves(figure);
            case KNIGHT -> getKnightMoves(figure);
            case PAWN -> getPawnMoves(figure);
            case QUEEN -> getQueenMoves(figure);
            case ROCK -> getRockMoves(figure);
            default -> new ArrayList<>();
        };
    }

    // * Логика для слона
    private List<Move> getBishopMoves(Figure figure) {
        return getDiagonalMoves(figure);
    }

    // * Логика для короля
    private List<Move> getKingMoves(Figure figure) {
        List<Move> possibleMoves = new ArrayList<>();
        FigurePositionLetterEnum[] horizontalMoves = FigurePositionLetterEnum.values();
        FigurePositionNumberEnum[] verticalMoves = FigurePositionNumberEnum.values();

        int[][] kingMoves = {
                {1, 0}, {1, 1}, {0, 1}, {-1, 1},
                {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
        };

        int currentHorizontalIndex = figure.getHorizontalPos().ordinal();
        int currentVerticalIndex = figure.getVerticalPos().ordinal();

        for (int[] move : kingMoves) {
            int newHorizontalIndex = currentHorizontalIndex + move[0];
            int newVerticalIndex = currentVerticalIndex + move[1];

            if (newHorizontalIndex >= 0 && newHorizontalIndex < horizontalMoves.length &&
                    newVerticalIndex >= 0 && newVerticalIndex < verticalMoves.length) {
                FigurePositionLetterEnum newHorizontal = horizontalMoves[newHorizontalIndex];
                FigurePositionNumberEnum newVertical = verticalMoves[newVerticalIndex];

                if (!isOccupied(newHorizontal, newVertical) || isOccupiedByOpponent(figure, newHorizontal, newVertical)) {
                    possibleMoves.add(new Move(figure, newHorizontal, newVertical));
                }
            }
        }

        return possibleMoves;
    }

    // * Логика для коня
    private List<Move> getKnightMoves(Figure figure) {
        List<Move> possibleMoves = new ArrayList<>();
        FigurePositionLetterEnum[] horizontalMoves = FigurePositionLetterEnum.values();
        FigurePositionNumberEnum[] verticalMoves = FigurePositionNumberEnum.values();

        int[][] knightMoves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        int currentHorizontalIndex = figure.getHorizontalPos().ordinal();
        int currentVerticalIndex = figure.getVerticalPos().ordinal();

        for (int[] move : knightMoves) {
            int newHorizontalIndex = currentHorizontalIndex + move[0];
            int newVerticalIndex = currentVerticalIndex + move[1];

            if (newHorizontalIndex >= 0 && newHorizontalIndex < horizontalMoves.length &&
                    newVerticalIndex >= 0 && newVerticalIndex < verticalMoves.length) {
                FigurePositionLetterEnum newHorizontal = horizontalMoves[newHorizontalIndex];
                FigurePositionNumberEnum newVertical = verticalMoves[newVerticalIndex];

                if (!isOccupied(newHorizontal, newVertical) || isOccupiedByOpponent(figure, newHorizontal, newVertical)) {
                    possibleMoves.add(new Move(figure, newHorizontal, newVertical));
                }
            }
        }

        return possibleMoves;
    }

    // * Логика для пешки
    private List<Move> getPawnMoves(Figure figure) {
        List<Move> possibleMoves = new ArrayList<>();
        FigurePositionLetterEnum[] horizontalMoves = FigurePositionLetterEnum.values();
        FigurePositionNumberEnum[] verticalMoves = FigurePositionNumberEnum.values();

        int direction = figure.getColorFigure() == FigureColorEnum.WHITE ? 1 : -1;
        int currentHorizontalIndex = figure.getHorizontalPos().ordinal();
        int currentVerticalIndex = figure.getVerticalPos().ordinal();

        // Один шаг вперёд
        int newVerticalIndex = currentVerticalIndex + direction;
        if (newVerticalIndex >= 0 && newVerticalIndex < verticalMoves.length) {
            FigurePositionNumberEnum newVertical = verticalMoves[newVerticalIndex];
            if (!isOccupied(figure.getHorizontalPos(), newVertical)) {
                possibleMoves.add(new Move(figure, figure.getHorizontalPos(), newVertical));
            }
        }

        // Два шага вперёд (если пешка не двигалась)
        if ((figure.getColorFigure() == FigureColorEnum.WHITE && figure.getVerticalPos() == FigurePositionNumberEnum.TWO) ||
                (figure.getColorFigure() == FigureColorEnum.BLACK && figure.getVerticalPos() == FigurePositionNumberEnum.SEVEN)) {
            newVerticalIndex = currentVerticalIndex + 2 * direction;
            FigurePositionNumberEnum midVertical = verticalMoves[currentVerticalIndex + direction];
            if (!isOccupied(figure.getHorizontalPos(), verticalMoves[newVerticalIndex]) &&
                    !isOccupied(figure.getHorizontalPos(), midVertical)) {
                possibleMoves.add(new Move(figure, figure.getHorizontalPos(), verticalMoves[newVerticalIndex]));
            }
        }

        // Взятие фигуры противника
        int[] captureOffsets = {-1, 1};
        for (int offset : captureOffsets) {
            int newHorizontalIndex = currentHorizontalIndex + offset;
            if (newHorizontalIndex >= 0 && newHorizontalIndex < horizontalMoves.length) {
                FigurePositionLetterEnum newHorizontal = horizontalMoves[newHorizontalIndex];
                if (isOccupiedByOpponent(figure, newHorizontal, verticalMoves[newVerticalIndex])) {
                    possibleMoves.add(new Move(figure, newHorizontal, verticalMoves[newVerticalIndex]));
                }
            }
        }

        return possibleMoves;
    }

    // * Логика для королевы
    private List<Move> getQueenMoves(Figure figure) {
        List<Move> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(getDiagonalMoves(figure));
        possibleMoves.addAll(getStraightMoves(figure));
        return possibleMoves;
    }

    // * Логика для ладьи
    private List<Move> getRockMoves(Figure figure) {
        return getStraightMoves(figure);
    }

    // * Проверка возможных ходов по диагонале
    private List<Move> getDiagonalMoves(Figure figure) {
        List<Move> possibleMoves = new ArrayList<>();
        FigurePositionLetterEnum[] horizontalMoves = FigurePositionLetterEnum.values();
        FigurePositionNumberEnum[] verticalMoves = FigurePositionNumberEnum.values();

        int currentHorizontalIndex = figure.getHorizontalPos().ordinal();
        int currentVerticalIndex = figure.getVerticalPos().ordinal();

        int[][] directions = {
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        for (int[] direction : directions) {
            int newHorizontalIndex = currentHorizontalIndex;
            int newVerticalIndex = currentVerticalIndex;
            while (true) {
                newHorizontalIndex += direction[0];
                newVerticalIndex += direction[1];

                if (newHorizontalIndex >= 0 && newHorizontalIndex < horizontalMoves.length &&
                        newVerticalIndex >= 0 && newVerticalIndex < verticalMoves.length) {
                    FigurePositionLetterEnum newHorizontal = horizontalMoves[newHorizontalIndex];
                    FigurePositionNumberEnum newVertical = verticalMoves[newVerticalIndex];

                    if (isOccupied(newHorizontal, newVertical)) {
                        if (isOccupiedByOpponent(figure, newHorizontal, newVertical)) {
                            possibleMoves.add(new Move(figure, newHorizontal, newVertical));
                        }
                        break;
                    } else {
                        possibleMoves.add(new Move(figure, newHorizontal, newVertical));
                    }
                } else {
                    break;
                }
            }
        }

        return possibleMoves;
    }

    // * Проверка возможных ходов по горизонтали
    private List<Move> getStraightMoves(Figure figure) {
        List<Move> possibleMoves = new ArrayList<>();
        FigurePositionLetterEnum[] horizontalMoves = FigurePositionLetterEnum.values();
        FigurePositionNumberEnum[] verticalMoves = FigurePositionNumberEnum.values();

        int currentHorizontalIndex = figure.getHorizontalPos().ordinal();
        int currentVerticalIndex = figure.getVerticalPos().ordinal();

        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        for (int[] direction : directions) {
            int newHorizontalIndex = currentHorizontalIndex;
            int newVerticalIndex = currentVerticalIndex;
            while (true) {
                newHorizontalIndex += direction[0];
                newVerticalIndex += direction[1];

                if (newHorizontalIndex >= 0 && newHorizontalIndex < horizontalMoves.length &&
                        newVerticalIndex >= 0 && newVerticalIndex < verticalMoves.length) {
                    FigurePositionLetterEnum newHorizontal = horizontalMoves[newHorizontalIndex];
                    FigurePositionNumberEnum newVertical = verticalMoves[newVerticalIndex];

                    if (isOccupied(newHorizontal, newVertical)) {
                        if (isOccupiedByOpponent(figure, newHorizontal, newVertical)) {
                            possibleMoves.add(new Move(figure, newHorizontal, newVertical));
                        }
                        break;
                    } else {
                        possibleMoves.add(new Move(figure, newHorizontal, newVertical));
                    }
                } else {
                    break;
                }
            }
        }

        return possibleMoves;
    }

    // * Логика для проверки занятости клетки на доске
    private boolean isOccupied(FigurePositionLetterEnum horizontal, FigurePositionNumberEnum vertical) {
        return WhiteFiguresList.stream().anyMatch(f -> f.getHorizontalPos() == horizontal && f.getVerticalPos() == vertical) ||
                BlackFiguresList.stream().anyMatch(f -> f.getHorizontalPos() == horizontal && f.getVerticalPos() == vertical);
    }

    // * Логика для проверки занятости клетки фигурой противника
    private boolean isOccupiedByOpponent(Figure figure, FigurePositionLetterEnum horizontal, FigurePositionNumberEnum vertical) {
        List<Figure> opponentFigures = figure.getColorFigure() == FigureColorEnum.WHITE ? BlackFiguresList : WhiteFiguresList;
        return opponentFigures.stream().anyMatch(f -> f.getHorizontalPos() == horizontal && f.getVerticalPos() == vertical);
    }

}

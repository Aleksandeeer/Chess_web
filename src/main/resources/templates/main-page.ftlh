<!DOCTYPE html>
<html lang="RU">
<head>
    <title>Chess Board</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="../css/board.css">

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <script>
        var selectedFigure = null;
        var possibleMoves = [];

        // ? Подсветка возможных ходов при нажатии на фигуру
        $(document).ready(function() {
            $(".chess-board td img").click(function() { // todo: проверить без img (написать отдельный скрипт
                                                        // todo: $(".chess-board td").click(function()
                                                        // todo: возможно трабл в том что он проверяет полное совпадение на все атрибуты
                var imgElement = $(this).get(0); // или $(this)[0], чтобы получить первый элемент из выборки
                var isHighlighted = false;
                possibleMoves.forEach(function(posMove) {
                    if(posMove === imgElement) {
                        isHighlighted = true;
                    }
                });

                console.log("isHighlighted:" + isHighlighted);

                if (isHighlighted) { // todo: НЕ РАБОТАЕТ НИЧЕГО ПРОСТО ААААААААААААААААААААА
                    var targetCellId = $(this).attr("id");
                    var sourceCellId = $('.selected-figure').parent().attr("id"); // ID клетки с фигурой, которая двигается

                    console.log("Moving to cell: " + targetCellId);
                    $.ajax({
                        url: "/move-figure",
                        method: "POST",
                        data: {
                            source: sourceCellId,
                            target: targetCellId
                        },
                        success: function(response) {
                            console.log("Move successful: ", response);
                            clearHighlights(); // Очистка предыдущих подсветок
                            // Обновление отображения фигур
                            updateBoard(response);
                        },
                        error: function(xhr, status, error) {
                            console.error("Error moving figure: ", error);
                            alert("Error moving figure. Error: " + error);
                        }
                    });
                } else {
                    var cellId = $(this).parent().attr("id");
                    selectedFigure = cellId;
                    console.log("Clicked on cell: " + cellId);
                    $.ajax({
                        url: "/possible-moves/" + cellId,
                        method: "GET",
                        success: function(response) {
                            console.log("Received moves: ", response);
                            clearHighlights(); // Очистка предыдущих подсветок
                            highlightMoves(response);
                            possibleMoves = response;
                        },
                        error: function(xhr, status, error) {
                            console.error("Error fetching possible moves: ", error);
                            alert("Error fetching possible moves. Error: " + error);
                        }
                    });
                }
            });
        });

        // ? Обновление доски после хода
        function updateBoard(gameState) {
            $(".chess-board td").empty();

            // Отображение белых фигур
            gameState.whiteFiguresList.forEach(function(figure) {
                var cellId = figure.horizontalPos.toLowerCase() + (figure.verticalPos.ordinal() + 1);
                var figureImg = "<img src='/images/" + figure.typeFigure.toLowerCase() + "_" + figure.colorFigure.toLowerCase() + ".png' />";
                $("#" + cellId).html(figureImg);
            });

            // Отображение черных фигур
            gameState.blackFiguresList.forEach(function(figure) {
                var cellId = figure.horizontalPos.toLowerCase() + (figure.verticalPos.ordinal() + 1);
                var figureImg = "<img src='/images/" + figure.typeFigure.toLowerCase() + "_" + figure.colorFigure.toLowerCase() + ".png' />";
                $("#" + cellId).html(figureImg);
            });
        }

        // ? Функция для очистки предыдущих подсветок
        function clearHighlights() {
            const highlightedCells = document.querySelectorAll('.highlight');
            highlightedCells.forEach(function(cell) {
                cell.classList.remove('highlight');
            });
        }


        // ? Выделение возможных шагов
        function highlightMoves(moves) {
            moves.forEach(function (move) {
                const cell = document.getElementById(move);
                if (cell) {
                    cell.classList.add('highlight');
                }
            });
        }
    </script>
    <style>
        .highlight {
            background-color: yellow !important; /* Используем !important для приоритета */
        }
    </style>
</head>
<body>

<div class="box">
    <div class="centered">
        <table class="chess-board">
            <#list FigurePositionNumberEnum?reverse as row>
                <tr>
                    <#list FigurePositionLetterEnum as col>
                        <#assign isWhiteCell = (col?index + row?index) % 2 == 0>
                        <td class="${isWhiteCell?string('white', 'black')}" id="${col?lower_case}${row?index + 1}">
                            <#assign figureFound = false>
                            <#list WhiteFiguresList + BlackFiguresList as figure>
                                <#if figure.horizontalPos == col && figure.verticalPos == row>
                                    <#assign imagePath = "/images/${figure.colorFigure?upper_case}_${figure.typeFigure?upper_case}.png">
                                    <img src="${imagePath}" alt="${figure.colorFigure} ${figure.typeFigure}">
                                    <#assign figureFound = true>
                                    <#break>
                                </#if>
                            </#list>
                            <#if !figureFound>
                                &nbsp;
                            </#if>
                        </td>
                    </#list>
                </tr>
            </#list>
        </table>
    </div>
</div>


</body>
</html>
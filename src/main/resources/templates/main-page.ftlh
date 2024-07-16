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
            $(".chess-board td").click(function() {
                var parentCell = $(this);
                var imgElement = parentCell.find("img").get(0);

                if (selectedFigure) {
                    var targetCellId = parentCell.attr("id");

                    // Проверяем, является ли targetCellId допустимым ходом
                    var isValidMove = possibleMoves.includes(targetCellId);

                    if (isValidMove) {
                        var sourceCellId = selectedFigure;
                        console.log("Selected cell for move. Target: " + targetCellId + ", Source: " + sourceCellId);

                        $.ajax({
                            url: "/move-figure",
                            method: "POST",
                            data: {
                                source: sourceCellId,
                                target: targetCellId
                            },
                            success: function(response) {
                                clearHighlights(); // Очистка предыдущих подсветок
                                updateBoard(response);
                                selectedFigure = null; // Сбрасываем выбранную фигуру после успешного хода
                            },
                            error: function(xhr, status, error) {
                                alert("Error moving figure. Error: " + error);
                            }
                        });
                    } else {
                        console.log("Invalid move to cell: " + targetCellId);
                    }
                } else {
                    if (imgElement) { // Проверка, что клик был по фигуре
                        var cellId = parentCell.attr("id");
                        selectedFigure = cellId;
                        $('.selected-figure').removeClass('selected-figure'); // Удаление предыдущей подсветки
                        $(imgElement).addClass('selected-figure'); // Подсветка новой фигуры
                        console.log("Selected figure at cell: " + cellId);

                        $.ajax({
                            url: "/possible-moves/" + cellId,
                            method: "GET",
                            success: function(response) {
                                clearHighlights(); // Очистка предыдущих подсветок
                                highlightMoves(response);
                                possibleMoves = response;
                            },
                            error: function(xhr, status, error) {
                                alert("Error fetching possible moves. Error: " + error);
                            }
                        });
                    }
                }
            });
        });


        // ? Обновление доски после хода
        function updateBoard(gameState) {
            // $(".chess-board td").each(function() {
            //     $(this).html("<img src='/images/Empty.png' alt='Empty' />");
            // });

            // Отображение белых фигур
            gameState.whiteFiguresList.forEach(function(figure) {
                var cellId = figure.horizontalPos.toLowerCase() + figure.verticalPos; // Используем просто figure.verticalPos
                var figureImg = "<img src='/images/" + figure.typeFigure.toLowerCase() + "_" + figure.colorFigure.toLowerCase() + ".png' />";
                $("#" + cellId).html(figureImg);
            });

            // Отображение черных фигур
            gameState.blackFiguresList.forEach(function(figure) {
                var cellId = figure.horizontalPos.toLowerCase() + figure.verticalPos; // Используем просто figure.verticalPos
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
<#--                    TODO: поменялись местами фигуры (вверх ногами)   -->
                        <td class="${isWhiteCell?string('white', 'black')}" id="${col?lower_case}${row?index + 1}">
                            <#assign figureFound = false>
                            <#list WhiteFiguresList as whiteFigure>
                                <#if whiteFigure.horizontalPos == col && whiteFigure.verticalPos == row>
                                    <#assign imagePath = "/images/${whiteFigure.colorFigure?upper_case}_${whiteFigure.typeFigure?upper_case}.png">
                                    <img src="${imagePath}" alt="${whiteFigure.colorFigure} ${whiteFigure.typeFigure}">
                                    <#assign figureFound = true>
                                    <#break>
                                </#if>
                            </#list>
                            <#list BlackFiguresList as blackFigure>
                                <#if blackFigure.horizontalPos == col && blackFigure.verticalPos == row>
                                    <#assign imagePath = "/images/${blackFigure.colorFigure?upper_case}_${blackFigure.typeFigure?upper_case}.png">
                                    <img src="${imagePath}" alt="${blackFigure.colorFigure} ${blackFigure.typeFigure}">
                                    <#assign figureFound = true>
                                    <#break>
                                </#if>
                            </#list>
                            <#if !figureFound>
                                <img src="/images/Empty.png" alt="Empty">
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
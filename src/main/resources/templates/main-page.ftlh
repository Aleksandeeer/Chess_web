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

        // ? Ход фигуры
        $(document).ready(function() {
            $(".chess-board td").click(function() {
                var parentCell = $(this);
                var imgElement = parentCell.find("img").get(0);

                if (selectedFigure) {
                    // todo: здесь багулина со сменой хода
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
                                clearHighlights();
                                updateBoard(response);
                                selectedFigure = null;
                            },
                            error: function(xhr, status, error) {
                                alert("Error moving figure. Error: " + error);
                            }
                        });
                    } else {
                        console.log("Invalid move to cell: " + targetCellId);
                    }

                } else { // ? Подсветка возможных ходов при нажатии на фигуру
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


        // ? Апдейт доски после хода
        function updateBoard(gameState) {
            var whiteFigures = gameState.whiteFiguresList;
            var blackFigures = gameState.blackFiguresList;

            // Очистка доски
            $(".chess-board td").each(function() {
                $(this).empty().append('<img src="/images/Empty.png" alt="Empty">');
            });

            // Размещение белых фигур
            whiteFigures.forEach(function(figure) {
                var cellId = figure.horizontalPos.toLowerCase() + (8 - figure.verticalPos);
                var imagePath = "/images/" + figure.colorFigure.toUpperCase() + "_" + figure.typeFigure.toUpperCase() + ".png";
                // console.log("image_path: " + imagePath);
                $("#" + cellId).empty().append('<img src="' + imagePath + '" alt="' + figure.colorFigure + ' ' + figure.typeFigure + '">');
            });

            // Размещение черных фигур
            blackFigures.forEach(function(figure) {
                var cellId = figure.horizontalPos.toLowerCase() + (8 - figure.verticalPos);
                var imagePath = "/images/" + figure.colorFigure.toUpperCase() + "_" + figure.typeFigure.toUpperCase() + ".png";
                // console.log("image_path: " + imagePath);
                $("#" + cellId).empty().append('<img src="' + imagePath + '" alt="' + figure.colorFigure + ' ' + figure.typeFigure + '">');
            });

            location.reload();
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
            background-color: yellow !important; /* Используем !important для приоритета над .white и .black */
        }
    </style>
</head
<body>

<div class="box">
    <div class="centered">
        <table class="chess-board">
            <#list FigurePositionNumberEnum as row>
                <tr>
                    <#list FigurePositionLetterEnum as col>
                        <#assign isWhiteCell = (col?index + row?index) % 2 == 0>
                        <!-- Зеркальная нумерация строк -->
                        <td class="${isWhiteCell?string('white', 'black')}" id="${col?lower_case}${(8 - row?index)}">
                            <#assign figureFound = false>

                            <#list WhiteFiguresList as whiteFigure>
                                <#if whiteFigure.getHorizontalPos() == col && whiteFigure.getVerticalPos().ordinal() == (7 - row?index)>
                                    <#assign imagePath = "/images/${whiteFigure.getColorFigure()?upper_case}_${whiteFigure.getTypeFigure()?upper_case}.png">
                                    <img src="${imagePath}" alt="${whiteFigure.getColorFigure()} ${whiteFigure.getTypeFigure()}">
                                    <#assign figureFound = true>
                                    <#break>
                                </#if>
                            </#list>

                            <#list BlackFiguresList as blackFigure>
                                <#if blackFigure.getHorizontalPos() == col && blackFigure.getVerticalPos().ordinal() == (7 - row?index)>
                                    <#assign imagePath = "/images/${blackFigure.getColorFigure()?upper_case}_${blackFigure.getTypeFigure()?upper_case}.png">
                                    <img src="${imagePath}" alt="${blackFigure.getColorFigure()} ${blackFigure.getTypeFigure()}">
                                    <#assign figureFound = true>
                                    <#break>
                                </#if>
                            </#list>

                            <!-- Пустые клетки -->
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
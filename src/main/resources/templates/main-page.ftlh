<!DOCTYPE html>
<html lang="RU">
<head>
    <title>Chess Board</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="../css/board.css">

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<#--TODO : выкинуть скрипт в файл -->
    <script>
        $(document).ready(function() {
            $(".chess-board td img").click(function() {
                var cellId = $(this).parent().attr("id");
                console.log("Clicked on cell: " + cellId);
                $.ajax({
                    url: "/possible-moves/" + cellId,
                    method: "GET",
                    success: function(response) {
                        console.log("Received moves: ", response);
                        clearHighlights(); // Очистка предыдущих подсветок
                        highlightMoves(response);
                    },
                    error: function(xhr, status, error) {
                        console.error("Error fetching possible moves: ", error);
                        alert("Error fetching possible moves.");
                    }
                });
            });
        });

        // Функция для очистки предыдущих подсветок
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


        // ? Вывод возможных шагов
        function printMoves(moves) {
            moves.forEach(function(move) {
               document.writeln(move);
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
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
                console.log("Clicked on cell: " + cellId); // Debug output
                $.ajax({
                    url: "/possible-moves/" + cellId,
                    method: "GET",
                    success: function(response) {
                        console.log("Received moves: ", response); // Debug output
                        // $("td").removeClass("possible-move");
                        highlightMoves(response);
                        // response.forEach(function(move) {
                        //     $("#" + move).addClass("possible-move");
                        // });
                        // response.forEach(function(move) {
                        //     $("#" + move).addClass("possible-move");
                        //     // Вывод каждого возможного хода с помощью document.write
                        //     document.write(move + "<br>");
                        // });
                    },
                    error: function(xhr, status, error) {
                        console.error("Error fetching possible moves: ", error); // Debug output
                        alert("Error fetching possible moves.");
                    }
                });
            });
        });

        function highlightMoves(moves) {
            // Снимаем выделение со всех клеток перед выделением новых
            $(".chess-board td").removeClass("highlighted");

            // Выделяем клетки, соответствующие возможным ходам
            moves.forEach(function(move) {
                var cellId = move.cellId; // Предположим, что в ответе есть список cellId для выделения
                $("#" + cellId).addClass("possible-move");
            });
        }
    </script>
    <style>
        .possible-move {
            background-color: yellow;
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
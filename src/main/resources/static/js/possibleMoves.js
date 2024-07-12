function getPossibleMoves(horizontalPos, verticalPos) {
    fetch(`/moves?horizontalPos=${horizontalPos}&verticalPos=${verticalPos}`)
        .then(response => response.json())
        .then(data => {
            // Обработка данных и отображение возможных ходов
            highlightMoves(data);
        })
        .catch(error => console.error('Error:', error));
}

function highlightMoves(moves) {
    // Удаление предыдущих подсветок
    document.querySelectorAll('.highlight').forEach(cell => cell.classList.remove('highlight'));

    // Добавление новой подсветки
    moves.forEach(move => {
        const cellId = move.horizontalPos.toLowerCase() + move.verticalPos;
        const cell = document.getElementById(cellId);
        if (cell) {
            cell.classList.add('highlight');
        }
    });
}
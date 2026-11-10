package com.example.chess.model

import kotlin.math.abs
import kotlin.math.sign

data class Cell(
    val row: Int,
    val col: Int,
    var piece: ChessPiece? = null
)

class ChessBoard {
    val grid: Array<Array<Cell>> = Array(8) { row ->
        Array(8) { col ->
            Cell(row, col)
        }
    }

    val gameState = GameState()

    init {
        resetBoard()
    }

    fun resetBoard() {
        grid.forEach { row -> row.forEach { cell -> cell.piece = null } }
        gameState.currentTurn = PieceColor.WHITE
        gameState.status = GameStatus.IN_PROGRESS
        gameState.enPassantTarget = null

        val mainPieces = arrayOf(
            PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN,
            PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK
        )

        for (col in 0..7) {
            grid[0][col].piece = ChessPiece(mainPieces[col], PieceColor.BLACK)
            grid[1][col].piece = ChessPiece(PieceType.PAWN, PieceColor.BLACK)
            grid[7][col].piece = ChessPiece(mainPieces[col], PieceColor.WHITE)
            grid[6][col].piece = ChessPiece(PieceType.PAWN, PieceColor.WHITE)
        }
    }

    fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val movingPiece = grid[fromRow][fromCol].piece

        if (movingPiece != null && movingPiece.color == gameState.currentTurn && gameState.status == GameStatus.IN_PROGRESS) {
            if (isValidMove(fromRow, fromCol, toRow, toCol) && isMoveSafe(fromRow, fromCol, toRow, toCol)) {
                val targetPiece = grid[toRow][toCol].piece
                if(targetPiece != null) {
                    gameState.addCountPiece(targetPiece)
                }

                // Взятие на проходе пешки
                if (movingPiece.type == PieceType.PAWN && fromCol != toCol && targetPiece == null) {
                    val capturedRow = fromRow
                    val capturedPawn = grid[capturedRow][toCol].piece
                    if (capturedPawn != null) {
                        gameState.addCountPiece(capturedPawn)
                        grid[capturedRow][toCol].piece = null
                    }
                }

                if (movingPiece.type == PieceType.PAWN && abs(toRow - fromRow) == 2) {
                    val skippedRow = (fromRow + toRow) / 2
                    gameState.enPassantTarget = Pair(skippedRow, toCol)
                } else {
                    gameState.enPassantTarget = null
                }

                // Рокировка
                if (movingPiece.type == PieceType.KING && abs(toCol - fromCol) == 2) {
                    val rookFromCol = if (toCol > fromCol) 7 else 0
                    val rookToCol = if (toCol > fromCol) toCol - 1 else toCol + 1

                    val rook = grid[fromRow][rookFromCol].piece
                    grid[fromRow][rookToCol].piece = rook
                    grid[fromRow][rookFromCol].piece = null
                    rook?.hasMoved = true
                }

                grid[toRow][toCol].piece = movingPiece
                grid[fromRow][fromCol].piece = null

                // Превращение пешки
                if (movingPiece.type == PieceType.PAWN) {
                    val isWhitePromotion = movingPiece.color == PieceColor.WHITE && toRow == 0
                    val isBlackPromotion = movingPiece.color == PieceColor.BLACK && toRow == 7

                    if (isWhitePromotion || isBlackPromotion) {
                        grid[toRow][toCol].piece = ChessPiece(PieceType.QUEEN, movingPiece.color, true)
                    }
                }

                movingPiece.hasMoved = true
                gameState.switchTurn()

                updateGameStatus()

                return true
            }
        }
        return false
    }
    private fun isValidMove(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val movingPiece = grid[fromRow][fromCol].piece ?: return false
        val targetPiece = grid[toRow][toCol].piece

        if (targetPiece != null && targetPiece.color == movingPiece.color) {
            return false
        }

        val rowDiff = abs(toRow - fromRow)
        val colDiff = abs(toCol - fromCol)

        return when (movingPiece.type) {
            PieceType.KNIGHT -> {
                (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)
            }
            PieceType.KING -> {
                val isNormalMove = rowDiff <= 1 && colDiff <= 1

                val isCastling = rowDiff == 0 && colDiff == 2 && !movingPiece.hasMoved
                if (isCastling) {
                    val rookCol = if (toCol > fromCol) 7 else 0
                    val rookPiece = grid[fromRow][rookCol].piece

                    if (rookPiece != null && rookPiece.type == PieceType.ROOK && !rookPiece.hasMoved) {
                        return isPathClear(fromRow, fromCol, fromRow, rookCol)
                    }
                }

                isNormalMove || isCastling
            }
            PieceType.ROOK -> {
                (rowDiff == 0 || colDiff == 0) && isPathClear(fromRow, fromCol, toRow, toCol)
            }
            PieceType.BISHOP -> {
                (rowDiff == colDiff) && isPathClear(fromRow, fromCol, toRow, toCol)
            }
            PieceType.QUEEN -> {
                (rowDiff == 0 || colDiff == 0 || rowDiff == colDiff) && isPathClear(fromRow, fromCol, toRow, toCol)
            }
            PieceType.PAWN -> {
                val direction = if (movingPiece.color == PieceColor.WHITE) -1 else 1
                val startRow = if (movingPiece.color == PieceColor.WHITE) 6 else 1

                val isOneStep = colDiff == 0 && (toRow - fromRow) == direction && targetPiece == null

                val isTwoSteps = colDiff == 0 && (toRow - fromRow) == direction * 2 && fromRow == startRow &&
                        targetPiece == null && grid[fromRow + direction][fromCol].piece == null

                val isCapture = colDiff == 1 && (toRow - fromRow) == direction && targetPiece != null

                // Взятие на проходе
                val isEnPassant = colDiff == 1 && (toRow - fromRow) == direction && targetPiece == null &&
                        gameState.enPassantTarget?.first == toRow && gameState.enPassantTarget?.second == toCol

                isOneStep || isTwoSteps || isCapture || isEnPassant
            }
        }
    }

    // Проверка на то, что на пути фигуры нет препятствий
    private fun isPathClear(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val rowStep = sign((toRow - fromRow).toDouble()).toInt()
        val colStep = sign((toCol - fromCol).toDouble()).toInt()

        var currentRow = fromRow + rowStep
        var currentCol = fromCol + colStep

        // Двигаемся шаг за шагом до конечной клетки (не включая её саму)
        while (currentRow != toRow || currentCol != toCol) {
            if (grid[currentRow][currentCol].piece != null) {
                return false // На пути найдена фигура
            }
            currentRow += rowStep
            currentCol += colStep
        }
        return true
    }

    //Шах
    fun isKingInCheck(color: PieceColor): Boolean {
        var kingRow = -1
        var kingCol = -1

        for (row in 0..7) {
            for (col in 0..7) {
                val piece = grid[row][col].piece
                if (piece != null && piece.type == PieceType.KING && piece.color == color) {
                    kingRow = row
                    kingCol = col
                    break
                }
            }
        }

        if (kingRow == -1) return false

        for (row in 0..7) {
            for (col in 0..7) {
                val piece = grid[row][col].piece
                if (piece != null && piece.color != color) {
                    if (isValidMove(row, col, kingRow, kingCol)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    // Симулируем ход: проверяем, не окажется ли наш король под шахом после него
    private fun isMoveSafe(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val movingPiece = grid[fromRow][fromCol].piece
        val targetPiece = grid[toRow][toCol].piece

        // Временно перемещаем фигуру
        grid[toRow][toCol].piece = movingPiece
        grid[fromRow][fromCol].piece = null

        val inCheck = isKingInCheck(movingPiece!!.color)

        // Откатываем ход назад
        grid[fromRow][fromCol].piece = movingPiece
        grid[toRow][toCol].piece = targetPiece

        return !inCheck
    }

    // Проверяем, есть ли у текущего игрока хотя бы один допустимый ход
    private fun hasLegalMoves(color: PieceColor): Boolean {
        for (fromRow in 0..7) {
            for (fromCol in 0..7) {
                val piece = grid[fromRow][fromCol].piece
                if (piece != null && piece.color == color) {
                    for (toRow in 0..7) {
                        for (toCol in 0..7) {
                            if (isValidMove(fromRow, fromCol, toRow, toCol) && isMoveSafe(fromRow, fromCol, toRow, toCol)) {
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    // Обновляем статус игры (Мат или Пат)
    private fun updateGameStatus() {
        val hasMoves = hasLegalMoves(gameState.currentTurn)
        val inCheck = isKingInCheck(gameState.currentTurn)

        if (!hasMoves) {
            gameState.status = if (inCheck) {
                // Если ходов нет и король под ударом — это Мат (победил предыдущий игрок)
                if (gameState.currentTurn == PieceColor.WHITE) GameStatus.BLACK_WINS else GameStatus.WHITE_WINS
            } else {
                // Если ходов нет, но шаха нет — это Пат (Ничья)
                GameStatus.DRAW
            }
        }
    }
}
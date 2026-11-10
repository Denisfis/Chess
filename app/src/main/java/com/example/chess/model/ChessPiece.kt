package com.example.chess.model

enum class PieceColor {
    WHITE, BLACK
}

enum class PieceType {
    PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
}

data class ChessPiece(
    val type: PieceType,
    val color: PieceColor,
    var hasMoved: Boolean = false
)
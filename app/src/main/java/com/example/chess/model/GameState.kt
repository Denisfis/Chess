package com.example.chess.model

enum class GameStatus {
    IN_PROGRESS, WHITE_WINS, BLACK_WINS, DRAW
}
class GameState {
    var status: GameStatus = GameStatus.IN_PROGRESS
    var currentTurn: PieceColor = PieceColor.WHITE
    var countWhitePiece = mutableListOf<ChessPiece>()
    var countBlackPiece = mutableListOf<ChessPiece>()
    var enPassantTarget: Pair<Int, Int>? = null
    fun switchTurn() {
        currentTurn = if (currentTurn == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
    }

    fun addCountPiece (piece: ChessPiece){
        if(piece.color == PieceColor.WHITE)
            countWhitePiece.add(piece)
        else
            countBlackPiece.add(piece)
    }

}
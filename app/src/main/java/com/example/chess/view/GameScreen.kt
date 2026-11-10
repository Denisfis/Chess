package com.example.chess.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.model.ChessBoard
import com.example.chess.model.ChessPiece
import com.example.chess.model.GameStatus
import com.example.chess.model.PieceColor

@Composable
fun GameScreen(onNavigateBack: () -> Unit) {
    val chessBoard = remember { ChessBoard() }
    var refreshTrigger by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Chess Game",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val dummy = refreshTrigger

        val statusText = when (chessBoard.gameState.status) {
            GameStatus.IN_PROGRESS -> "Current Turn: ${chessBoard.gameState.currentTurn.name}"
            GameStatus.WHITE_WINS -> "WHITE WINS! (Checkmate)"
            GameStatus.BLACK_WINS -> "BLACK WINS! (Checkmate)"
            GameStatus.DRAW -> "DRAW! (Stalemate)"
        }

        val statusColor = when (chessBoard.gameState.status) {
            GameStatus.IN_PROGRESS -> if (chessBoard.gameState.currentTurn == PieceColor.WHITE) Color.Gray else Color.Black
            else -> Color.Red
        }

        Text(
            text = statusText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = statusColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        CountPiecesView(pieces = chessBoard.gameState.countWhitePiece.toList())

        Spacer(modifier = Modifier.height(8.dp))

        BoardView(
            chessBoard = chessBoard,
            refreshTrigger = refreshTrigger,
            onMove = { refreshTrigger++ }
        )

        Spacer(modifier = Modifier.height(8.dp))

        CountPiecesView(pieces = chessBoard.gameState.countBlackPiece.toList())

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onNavigateBack) {
            Text(text = "Back to Menu")
        }
    }
}

@Composable
fun CountPiecesView(pieces: List<ChessPiece>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        pieces.forEach { piece ->
            Text(
                text = piece.type.name.take(1),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (piece.color == PieceColor.WHITE) Color.Gray else Color.Black,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }
    }
}

@Composable
fun BoardView(
    chessBoard: ChessBoard,
    refreshTrigger: Int,
    onMove: () -> Unit
) {
    var selectedCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    Column {
        for (row in 0..7) {
            Row {
                for (col in 0..7) {
                    val isLightSquare = (row + col) % 2 == 0
                    var squareColor = if (isLightSquare) Color(0xFFF0D9B5) else Color(0xFFB58863)

                    if (selectedCell?.first == row && selectedCell?.second == col) {
                        squareColor = Color(0xFFFFF59D)
                    }

                    val dummy = refreshTrigger
                    val cell = chessBoard.grid[row][col]

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(squareColor)
                            .clickable {
                                if (chessBoard.gameState.status != GameStatus.IN_PROGRESS) return@clickable

                                if (selectedCell == null) {
                                    if (cell.piece != null && cell.piece!!.color == chessBoard.gameState.currentTurn) {
                                        selectedCell = Pair(row, col)
                                    }
                                } else {
                                    val (fromRow, fromCol) = selectedCell!!
                                    val success = chessBoard.movePiece(fromRow, fromCol, row, col)

                                    selectedCell = null
                                    if (success) {
                                        onMove()
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        cell.piece?.let { piece ->
                            val textColor = if (piece.color == PieceColor.WHITE) Color.White else Color.Black
                            Text(
                                text = piece.type.name.take(1),
                                color = textColor,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
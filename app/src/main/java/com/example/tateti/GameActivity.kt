package com.example.tateti

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import com.example.tateti.storage.SharedPrefsManager
import com.example.tateti.ui.theme.TicTacTronTheme
import com.example.tateti.ui.theme.TronBlue
import com.example.tateti.ui.theme.TronGrey
import com.example.tateti.ui.theme.TronRed
import com.example.tateti.ui.theme.TronWhite
import kotlin.random.Random

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playerName = intent.getStringExtra("playerName") ?: "Extraño"
        val playerTeam = intent.getStringExtra("team") ?: "X"
        val cpuTeam = if (playerTeam == "X") "O" else "X"

        setContent {
            TicTacTronTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreen(playerName, playerTeam, cpuTeam, onBackToMain = {
                        finish()
                    }, onGoToLeaderboard = {
                        startActivity(Intent(this, LeaderboardActivity::class.java))
                    })
                }
            }
        }
    }
}

@Composable
fun GameScreen(
    playerName: String,
    playerTeam: String,
    cpuTeam: String,
    onBackToMain: () -> Unit,
    onGoToLeaderboard: () -> Unit
) {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var currentPlayer by remember { mutableStateOf(playerTeam) }
    var winner by remember { mutableStateOf<String?>(null) }
    var context = LocalContext.current

    fun checkWinner(board: List<String>): String? {
        val lines = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )
        for (line in lines) {
            val (a, b, c) = line
            if (board[a].isNotBlank() && board[a] == board[b] && board[b] == board[c]) {
                return board[a]
            }
        }
        return if (board.all { it.isNotBlank() }) "Empate" else null
    }

    fun makeCpuMove() {
        val emptyCells = board.mapIndexedNotNull { i, v -> if (v == "") i else null }

        val lines = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )

        // 1. Intenta ganar
        for (line in lines) {
            val (a, b, c) = line
            val values = listOf(board[a], board[b], board[c])
            if (values.count { it == cpuTeam } == 2 && values.count { it == "" } == 1) {
                val winIndex = listOf(a, b, c)[values.indexOf("")]
                board = board.toMutableList().also { it[winIndex] = cpuTeam }
                currentPlayer = playerTeam
                return
            }
        }

        // 2. Intenta bloquear victoria al jugador
        for (line in lines) {
            val (a, b, c) = line
            val values = listOf(board[a], board[b], board[c])
            if (values.count { it == playerTeam } == 2 && values.count { it == "" } == 1) {
                val blockIndex = listOf(a, b, c)[values.indexOf("")]
                board = board.toMutableList().also { it[blockIndex] = cpuTeam }
                currentPlayer = playerTeam
                return
            }
        }

        // 3. Sino, movimiento random
        if (emptyCells.isNotEmpty()) {
            val randomIndex = emptyCells[Random.nextInt(emptyCells.size)]
            board = board.toMutableList().also { it[randomIndex] = cpuTeam }
            currentPlayer = playerTeam
        }
    }

    fun handleMove(index: Int) {
        if (board[index] == "" && winner == null && currentPlayer == playerTeam) {
            board = board.toMutableList().also { it[index] = playerTeam }
            currentPlayer = cpuTeam
        }
    }

    LaunchedEffect(board) {
        val result = checkWinner(board)
        if (result != null) {
            winner = result
            if (result == playerTeam) {
                SharedPrefsManager.saveWin(context, playerName)
            }
        } else if (currentPlayer == cpuTeam) {
            makeCpuMove()
        }
    }

    AnimatedBackground()

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("$playerName juega con $playerTeam", style = MaterialTheme.typography.headlineMedium)
        Text("Turno de: ${if (currentPlayer == playerTeam) playerName else "CPU"}", color = Color.Gray)

        Board(board, onCellClicked = { if (currentPlayer == playerTeam) handleMove(it) })

        winner?.let {
            Text(
                when (it) {
                    "Empate" -> "¡Es un empate!"
                    else -> "El ganador es: ${if (it == playerTeam) playerName else "CPU"}"
                },
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Button(onClick = {
            board = List(9) { "" }
            winner = null
            currentPlayer = playerTeam
        },
            colors = ButtonDefaults.buttonColors(
            containerColor = TronGrey,
            contentColor = TronWhite
        )) {
            Text("Volver a Jugar")
        }

        Button(
            onClick = onBackToMain,
            colors = ButtonDefaults.buttonColors(
            containerColor = TronGrey,
            contentColor = TronWhite
        )
        ) {
            Text("Menu Principal")
        }

        Button(
            onClick = onGoToLeaderboard,
            colors = ButtonDefaults.buttonColors(
                containerColor = TronGrey,
                contentColor = TronWhite
            )
        ){
            Text("Tabla de Posiciones")
        }
    }
}

@Composable
fun Board(board: List<String>, onCellClicked: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(TronGrey.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column {
            for (i in 0..2) {
                Row {
                    for (j in 0..2) {
                        val index = i * 3 + j
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .padding(4.dp)
                                .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))
                                .border(2.dp, TronWhite, shape = RoundedCornerShape(16.dp))
                                .clickable { onCellClicked(index) }
                                .clip(RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                board[index],
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontSize = 36.sp,
                                    color = when (board[index]) {
                                        "X" -> TronRed
                                        "O" -> TronBlue
                                        else -> TronWhite
                                    }
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

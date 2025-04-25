package com.example.tateti

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tateti.ui.theme.TicTacTronTheme
import com.example.tateti.ui.theme.TronBlue
import com.example.tateti.ui.theme.TronGrey
import com.example.tateti.ui.theme.TronRed
import com.example.tateti.ui.theme.TronWhite
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import kotlin.random.Random
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacTronTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var name by remember { mutableStateOf("") }
                    var team by remember { mutableStateOf("X") }
                    val focusManager = LocalFocusManager.current

                    AnimatedBackground()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "¡Tic-Tac-Tron!",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Ingresá tu nombre") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            )
                        )

                        TeamSelector(selectedTeam = team, onTeamSelected = { team = it })

                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, GameActivity::class.java)
                                intent.putExtra(
                                    "playerName",
                                    if (name.isBlank()) "Extraño" else name
                                )
                                intent.putExtra("team", team)
                                startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TronGrey,
                                contentColor = TronWhite
                            )
                        ) {
                            Text("Comenzar")
                        }

                        Button(
                            onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        LeaderboardActivity::class.java
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TronGrey,
                                contentColor = TronWhite
                            )
                        ) {
                            Text("Tabla de Resultados")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeamSelector(selectedTeam: String, onTeamSelected: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf("X", "O").forEach { team ->
            val isSelected = selectedTeam == team
            Button(
                onClick = { onTeamSelected(team) },
                modifier = Modifier
                    .size(100.dp)
                    .border(
                        width = if (isSelected) 4.dp else 2.dp,
                        color = if (isSelected) if (team == "X") TronRed else TronBlue else Color.Gray,
                        shape = RoundedCornerShape(20)
                    ),
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) Color.Black else Color.DarkGray,
                    contentColor = if (team == "X") TronRed else TronBlue
                )
            ) {
                Text(
                    text = team,
                    fontSize = 40.sp,
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
    }
}

@Composable
fun AnimatedBackground(modifier: Modifier = Modifier) {
    val particleCount = 40
    val particles = remember {
        List(particleCount) {
            LineParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                length = Random.nextFloat() * 30 + 20,
                speedY = Random.nextFloat() * 0.0005f + 0.0002f,
                color = if (Random.nextBoolean()) TronBlue else TronRed
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "lines")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(60000, easing = LinearEasing)
        ),
        label = "progress"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Fondo con partículas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            particles.forEach { particle ->
                val newY = (particle.y + particle.speedY * animatedProgress * 60000f) % 1f
                val start = Offset(particle.x * width, newY * height)
                val end = Offset(particle.x * width, (newY * height) + particle.length)
                drawLine(
                    color = particle.color.copy(alpha = 0.5f),
                    start = start,
                    end = end,
                    strokeWidth = 2f
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(TronRed.copy(alpha = 0.2f), Color.Transparent),
                        startX = 0f,
                        endX = 600f //
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, TronBlue.copy(alpha = 0.2f)),
                        startX = 600f,
                        endX = 1200f
                    )
                )
        )
    }
}

data class LineParticle(
    val x: Float,
    val y: Float,
    val length: Float,
    val speedY: Float,
    val color: Color
)
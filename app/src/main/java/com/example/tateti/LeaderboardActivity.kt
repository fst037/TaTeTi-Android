package com.example.tateti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tateti.ui.theme.TicTacTronTheme
import com.example.tateti.storage.SharedPrefsManager
import com.example.tateti.ui.theme.TronGrey
import com.example.tateti.ui.theme.TronWhite

class LeaderboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacTronTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val winMap = remember { SharedPrefsManager.getAllWins(this) }

                    AnimatedBackground()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Tabla de Posiciones", style = MaterialTheme.typography.headlineLarge)

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(winMap.entries.toList()) { (name, wins) ->
                                LeaderboardRow(name, wins)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { finish() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TronGrey,
                                contentColor = TronWhite
                            )
                        ) {
                            Text("Atras")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardRow(name: String, wins: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("$name ganó: $wins juegos", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
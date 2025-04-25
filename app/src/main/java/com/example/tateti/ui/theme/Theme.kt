package com.example.tateti.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Define light/dark theme color schemes (mostly same for Tron style)
private val DarkColorScheme = darkColorScheme(
    primary = TronWhite,
    secondary = TronRed,
    background = TronBlack,
    surface = TronBlack,
    onPrimary = TronBlack,
    onSecondary = TronWhite,
    onBackground = TronWhite,
    onSurface = TronWhite
)

@Composable
fun TicTacTronTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(
            bodyLarge = TextStyle(
                color = TronWhite,
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif
            ),
            headlineMedium = TextStyle(
                color = TronWhite,
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif
            )
        ),
        shapes = Shapes(
            small = RoundedCornerShape(16.dp),
            medium = RoundedCornerShape(30.dp),
            large = RoundedCornerShape(50.dp)
        ),
        content = content
    )
}
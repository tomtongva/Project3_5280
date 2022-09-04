package com.group3.project2.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.android.material.color.MaterialColors

private val DarkColorPalette = darkColors(
    primary = LightGray,
    primaryVariant = Blue,
    secondary = Red
)

private val LightColorPalette = lightColors(
    primary = Black,
    primaryVariant = Blue,
    secondary = Red
)

@Composable
fun UnoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
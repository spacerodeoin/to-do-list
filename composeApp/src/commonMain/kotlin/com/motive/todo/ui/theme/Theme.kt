package com.motive.todo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    onPrimary = BackgroundWhite,
    primaryContainer = BlueLight,
    onPrimaryContainer = BlueSecondary,
    secondary = BlueSecondary,
    onSecondary = BackgroundWhite,
    background = BackgroundWhite,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = DividerColor,
    error = CriticalRed
)

@Composable
fun TodoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}

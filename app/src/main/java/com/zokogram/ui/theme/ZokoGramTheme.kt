package com.zokogram.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.shape.CircleShape
import androidx.compose.ui.graphics.Shape

enum class AvatarShapeType {
    CIRCLE, SQUARE, ROUNDED_CORNER
}

val LocalAvatarShape = staticCompositionLocalOf<Shape> { CircleShape }
val LocalAccentColor = staticCompositionLocalOf { Color(0xFF0088CC) }

@Composable
fun ZokoGramTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    accentColor: Color = Color(0xFF0088CC),
    avatarShapeType: AvatarShapeType = AvatarShapeType.CIRCLE,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = accentColor,
            background = Color(0xFF0F0F0F),
            surface = Color(0xFF1E1E1E)
        )
    } else {
        lightColorScheme(
            primary = accentColor,
            background = Color(0xFFFFFFFF),
            surface = Color(0xFFF5F5F5)
        )
    }

    val avatarShape = when (avatarShapeType) {
        AvatarShapeType.CIRCLE -> CircleShape
        AvatarShapeType.SQUARE -> androidx.compose.ui.graphics.RectangleShape
        AvatarShapeType.ROUNDED_CORNER -> androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    }

    CompositionLocalProvider(
        LocalAvatarShape provides avatarShape,
        LocalAccentColor provides accentColor
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

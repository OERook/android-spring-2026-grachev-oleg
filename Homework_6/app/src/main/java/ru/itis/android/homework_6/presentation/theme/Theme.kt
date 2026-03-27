package ru.itis.android.homework_6.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val RickAndMortyColorScheme = darkColorScheme(
    primary = RickPortalGreen,
    onPrimary = SpaceBackground,
    secondary = MortyShirtYellow,
    background = SpaceBackground,
    onBackground = TextWhite,
    surface = SurfaceCardDark,
    onSurface = TextWhite,
    surfaceVariant = SurfaceCardDark,
    onSurfaceVariant = TextGray
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RickAndMortyColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
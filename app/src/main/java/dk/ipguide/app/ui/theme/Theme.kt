package dk.ipguide.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PvsNavy,
    onPrimary = Color.White,
    primaryContainer = PvsBlueLight,
    onPrimaryContainer = PvsNavyDark,
    secondary = PvsNavyDark,
    onSecondary = Color.White,
    background = PvsBackground,
    onBackground = PvsTextPrimary,
    surface = Color.White,
    onSurface = PvsTextPrimary,
    surfaceVariant = PvsBackground,
    onSurfaceVariant = PvsTextSecondary,
    outline = PvsCardBorder
)

@Composable
fun IPGuideTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

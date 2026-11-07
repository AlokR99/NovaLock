package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = OneUIBlue,
    onPrimary = Color.White,
    primaryContainer = OneUIDarkBlue,
    onPrimaryContainer = Color.White,
    secondary = OneUIAccentPurple,
    onSecondary = Color.White,
    secondaryContainer = OneUIDarkCardElevated,
    onSecondaryContainer = OneUIDarkTextPrimary,
    tertiary = OneUIAccentCyan,
    background = OneUIDarkBackground,
    onBackground = OneUIDarkTextPrimary,
    surface = OneUIDarkSurface,
    onSurface = OneUIDarkTextPrimary,
    surfaceVariant = OneUIDarkCard,
    onSurfaceVariant = OneUIDarkTextSecondary,
    outline = OneUIDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = OneUIBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E4FF),
    onPrimaryContainer = OneUIDarkBlue,
    secondary = OneUIAccentPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE9E0FF),
    onSecondaryContainer = Color(0xFF2E1A66),
    tertiary = OneUIAccentCyan,
    background = OneUILightBackground,
    onBackground = OneUILightTextPrimary,
    surface = OneUILightSurface,
    onSurface = OneUILightTextPrimary,
    surfaceVariant = OneUILightCard,
    onSurfaceVariant = OneUILightTextSecondary,
    outline = OneUILightBorder
)

@Composable
fun NovaLockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    customPrimaryColor: Color? = null,
    customAccentColor: Color? = null,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val baseScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val finalColorScheme = if (customPrimaryColor != null) {
        if (darkTheme) {
            baseScheme.copy(
                primary = customPrimaryColor,
                secondary = customAccentColor ?: baseScheme.secondary
            )
        } else {
            baseScheme.copy(
                primary = customPrimaryColor,
                secondary = customAccentColor ?: baseScheme.secondary
            )
        }
    } else {
        baseScheme
    }

    MaterialTheme(
        colorScheme = finalColorScheme,
        typography = Typography,
        content = content
    )
}

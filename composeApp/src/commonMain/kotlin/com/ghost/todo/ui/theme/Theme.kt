package com.ghost.todo.ui.theme
// Generated using MaterialKolor Builder version 1.3.0 (103)
// https://materialkolor.com/?color_seed=FF6366F1&color_primary=FF6366F1&color_secondary=FF2DD4BF&color_tertiary=FFF472B6&color_neutral=FF121214&dark_mode=true&package_name=com.example.app



import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicMaterialThemeState

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val dynamicThemeState = rememberDynamicMaterialThemeState(
        isDark = isDarkTheme,
        style = PaletteStyle.TonalSpot,
        primary = Primary,
        secondary = Secondary,
        tertiary = Tertiary,
        neutral = Neutral,
    )
    
    DynamicMaterialTheme(
        state = dynamicThemeState,
        animate = true,
        content = content,
    )
}
package com.ghost.todo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ghost.todo.ui.navigation.AppNavigation
import com.ghost.todo.ui.theme.AppTheme


@Composable
@Preview
fun App() {
    AppTheme(
        isDarkTheme = !true
    ) {
        AppContent()
    }
}


@Composable
fun AppContent() {
    AppNavigation(
        modifier = Modifier.fillMaxSize()
    )
}
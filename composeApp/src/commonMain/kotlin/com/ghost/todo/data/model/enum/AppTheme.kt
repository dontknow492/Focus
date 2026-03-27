package com.ghost.todo.data.model.enum

import kotlinx.serialization.Serializable

@Serializable
enum class AppTheme {
    LUMINAL,  // Light
    OBSIDIAN, // Dark
    ABSOLUTE  // OLED
}
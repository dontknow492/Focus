package com.ghost.todo.data.model.enum

import kotlinx.serialization.Serializable

@Serializable
enum class PriorityLevel {
    ETHEREAL, // High/Pink
    STANDARD, // Med/Indigo
    BACKLOG   // Low/Grey
}
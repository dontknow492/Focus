package com.ghost.todo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SubTask(
    val id: String = "",
    val title: String = "",
    val isCompleted: Boolean = false,
    val isPriority: Boolean = false // Drives the pink "PRIORITY" pill
)
package com.ghost.todo.data.model.enum

import kotlinx.serialization.Serializable

@Serializable
enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    COMPLETED,
    BACKLOG
}


package com.ghost.todo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FocusSession(
    val id: String = "",
    val userId: String = "",
    val taskId: String? = null, // Can be null if it's a general focus session

    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val durationMinutes: Int = 0,
    val efficiencyScore: Int = 0, // e.g., 74%

    val wasInterrupted: Boolean = false
)
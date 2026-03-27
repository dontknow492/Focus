package com.ghost.todo.data.model

import com.ghost.todo.data.model.enum.PriorityLevel
import com.ghost.todo.data.model.enum.TaskStatus
import kotlinx.serialization.Serializable

@Serializable
data class FocusTask(
    val id: String = "", // Firestore Document ID
    val userId: String = "", // Crucial for multi-user/syncing

    // Core Details
    val title: String = "",
    val description: String = "", // Rich text or standard string
    val workspace: String = "Personal", // e.g., "WORK"
    val project: String = "General",    // e.g., "SYSTEMS"

    // Status & Classification
    val status: TaskStatus = TaskStatus.BACKLOG,
    val priority: PriorityLevel = PriorityLevel.STANDARD,
    val tags: List<String> = emptyList(), // e.g., ["System", "Design"]

    // Time & Scheduling
    val scheduledDate: Long? = null, // Unix timestamp for the Calendar
    val startTimeMs: Long? = null,   // Specific time of day (10:00 AM)
    val endTimeMs: Long? = null,     // Specific time of day (12:30 PM)
    val estimatedMinutes: Int? = null, // e.g., 45 for "45m" in Backlog

    // Advanced Features
    val location: String? = null, // e.g., "Zen Garden"
    val subtasks: List<SubTask> = emptyList(),
    val attachments: List<TaskAttachment> = emptyList(),
    val collaborators: List<String> = emptyList(), // List of User IDs

    // Tracking
    val progress: Float = 0f, // 0.0 to 1.0 (75% Complete)
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)



package com.ghost.todo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val userId: String,
    val username: String,
    val displayName: String?,
    val email: String,
    val avatarUrl: String? = null,
    val philosophyBio: String = "", // "Designing the void..."
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),

    // Settings
    val preferences: SystemPreferences = SystemPreferences()
)



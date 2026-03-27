package com.ghost.todo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val philosophyBio: String = "", // "Designing the void..."

    // Settings
    val preferences: SystemPreferences = SystemPreferences()
)
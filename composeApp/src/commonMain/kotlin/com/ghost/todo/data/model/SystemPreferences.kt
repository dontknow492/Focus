package com.ghost.todo.data.model

import com.ghost.todo.data.model.enum.AppTheme
import kotlinx.serialization.Serializable

@Serializable
data class SystemPreferences(
    // Focus Interruptions
    val hapticCompletion: Boolean = true,
    val deepFocusSuppression: Boolean = true,
    val emailDigest: Boolean = false,

    // Atmospheric Core
    val theme: AppTheme = AppTheme.OBSIDIAN,

    // Audio Sanctuary
    val defaultAmbientTrack: String = "Lo-fi Sanctuary",
    val defaultVolume: Float = 0.5f
)
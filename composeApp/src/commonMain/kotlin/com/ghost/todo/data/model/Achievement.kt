package com.ghost.todo.data.model

import kotlinx.serialization.Serializable

// A sealed class is great here because Achievements might have different properties later
@Serializable
sealed class Achievement {
    abstract val unlockedAt: Long

    @Serializable
    data class DeepDiver(override val unlockedAt: Long, val hoursFocused: Int) : Achievement()

    @Serializable
    data class OnFire(override val unlockedAt: Long, val dayStreak: Int) : Achievement()

    @Serializable
    data class TaskMaster(override val unlockedAt: Long, val tasksDone: Int) : Achievement()
}
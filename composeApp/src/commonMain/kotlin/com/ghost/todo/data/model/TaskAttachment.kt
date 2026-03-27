package com.ghost.todo.data.model

import com.ghost.todo.data.model.enum.AttachmentType
import kotlinx.serialization.Serializable

@Serializable
data class TaskAttachment(
    val id: String = "",
    val fileName: String = "",
    val fileUrl: String = "", // Firebase Storage URL
    val fileSizeMb: Double = 0.0, // e.g., 2.4
    val fileType: AttachmentType = AttachmentType.IMAGE
)
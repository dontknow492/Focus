package com.ghost.todo.data.model.enum

import kotlinx.serialization.Serializable

@Serializable
enum class AttachmentType {
    IMAGE,
    DOCUMENT,
    LINK,
    OTHER
}
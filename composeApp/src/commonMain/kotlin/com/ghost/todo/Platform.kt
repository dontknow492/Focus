package com.ghost.todo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
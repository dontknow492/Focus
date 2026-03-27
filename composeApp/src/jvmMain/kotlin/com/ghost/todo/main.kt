package com.ghost.todo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ghost.todo.di.appModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

fun main() = application {
    Napier.base(DebugAntilog())
    startKoin { modules(appModule) }
    Window(
        onCloseRequest = ::exitApplication,
        title = "todo",
    ) {
        App()
    }
}
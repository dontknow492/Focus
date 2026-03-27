package com.ghost.todo

import android.app.Application
import com.ghost.todo.di.appModule
import com.google.firebase.BuildConfig
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }

        if(BuildConfig.DEBUG){
            Napier.base(DebugAntilog())
        }
    }
}
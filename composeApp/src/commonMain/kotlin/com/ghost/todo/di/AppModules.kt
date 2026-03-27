package com.ghost.todo.di

import com.ghost.todo.data.service.AuthService
import com.ghost.todo.data.service.FirebaseAuthService
import com.ghost.todo.ui.viewmodel.LoginViewModel
import com.ghost.todo.ui.viewmodel.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val viewmodelModule = module {
    viewModel<LoginViewModel>{
        LoginViewModel(get())
    }
    viewModel<RegisterViewModel>{
        RegisterViewModel(get())
    }
}

private val serviceModule = module {
    single<AuthService> { FirebaseAuthService() }
}

val sharedModule = module {
    includes(viewmodelModule, serviceModule)
}


val appModule = module {
    includes(sharedModule)
}
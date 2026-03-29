package com.ghost.todo.di

import com.ghost.todo.data.service.AuthService
import com.ghost.todo.data.service.FirebaseAuthService
import com.ghost.todo.data.service.UserProfileService
import com.ghost.todo.ui.viewmodel.LoginViewModel
import com.ghost.todo.ui.viewmodel.RegisterViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val viewmodelModule = module {
    viewModel<LoginViewModel> {
        LoginViewModel(get())
    }
    viewModel<RegisterViewModel> {
        RegisterViewModel(get())
    }
}

val firebaseModule = module {

    // Firebase instances
    single { Firebase.firestore }
    single { Firebase.auth }

    // Services
    single<UserProfileService> { UserProfileService(get()) }
    single<AuthService> { FirebaseAuthService(get()) }
}

private val serviceModule = module {

}

val sharedModule = module {
    includes(viewmodelModule, serviceModule, firebaseModule)
}


val appModule = module {
    includes(sharedModule)
}
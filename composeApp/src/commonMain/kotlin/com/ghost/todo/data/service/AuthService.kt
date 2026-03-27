package com.ghost.todo.data.service

import dev.gitlive.firebase.auth.FirebaseUser

interface AuthService {
    val currentUser: FirebaseUser?

    suspend fun signUpWithEmail(email: String, password: String): Result<FirebaseUser?>
    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser?>
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser?>
    suspend fun signOut()
}
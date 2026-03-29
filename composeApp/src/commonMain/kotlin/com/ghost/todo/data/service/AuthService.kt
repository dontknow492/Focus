package com.ghost.todo.data.service

import com.ghost.todo.data.model.UserProfile
import dev.gitlive.firebase.auth.FirebaseUser

interface AuthService {
    val currentUser: FirebaseUser?

    suspend fun signUpWithEmail(
        email: String,
        password: String,
        username: String,
        displayName: String? = null
    ): Result<UserProfile>

    suspend fun signInWithEmail(email: String, password: String): Result<UserProfile>
    suspend fun signInWithGoogle(idToken: String): Result<UserProfile>
    suspend fun signOut()
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    suspend fun getCurrentUserProfile(): UserProfile?
}
package com.ghost.todo.data.service

import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import io.github.aakira.napier.Napier


class FirebaseAuthService : AuthService {
    private val auth: FirebaseAuth = Firebase.auth

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun signUpWithEmail(email: String, password: String): Result<FirebaseUser?> {
        return try {
            Napier.d("Attempting Sign Up for: $email", tag = "AuthService")
            val result = auth.createUserWithEmailAndPassword(email, password)
            Napier.i("Sign Up Successful: ${result.user?.uid}", tag = "AuthService")
            Result.success(result.user)
        } catch (e: Exception) {
            Napier.e("Sign Up Failed", e, tag = "AuthService")
            Result.failure(e)
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser?> {
        return try {
            Napier.d("Attempting Sign In: $email", tag = "AuthService")
            val result = auth.signInWithEmailAndPassword(email, password)
            Napier.i("Sign In Successful: ${result.user?.uid}", tag = "AuthService")
            Result.success(result.user)
        } catch (e: Exception) {
            Napier.e("Sign In Failed", e, tag = "AuthService")
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser?> {
        return try {
            Napier.d("Attempting Google Sign In", tag = "AuthService")
            val credential = GoogleAuthProvider.credential(idToken, null)
            val result = auth.signInWithCredential(credential)
            Napier.i("Google Sign In Successful: ${result.user?.uid}", tag = "AuthService")
            Result.success(result.user)
        } catch (e: Exception) {
            Napier.e("Google Sign In Failed", e, tag = "AuthService")
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        Napier.w("User logging out: ${auth.currentUser?.uid}", tag = "AuthService")
        auth.signOut()
    }
}
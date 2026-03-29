package com.ghost.todo.data.service

import com.ghost.todo.data.model.SystemPreferences
import com.ghost.todo.data.model.UserProfile
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import io.github.aakira.napier.Napier

class FirebaseAuthService(
    private val userProfileService: UserProfileService
) : AuthService {
    private val auth: FirebaseAuth = Firebase.auth

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        username: String,
        displayName: String?
    ): Result<UserProfile> {
        return try {
            Napier.d("Attempting Sign Up for: $email", tag = "AuthService")

            // 1. Create auth user
            val result = auth.createUserWithEmailAndPassword(email, password)
            val firebaseUser = result.user ?: throw Exception("User creation failed")

            // 2. Create user profile in Firestore
            val userProfile = UserProfile(
                userId = firebaseUser.uid,
                username = username,
                displayName = displayName ?: username,
                email = email,
                avatarUrl = null, // No avatar for email signup initially
                philosophyBio = "Designing the void...",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                preferences = SystemPreferences()
            )

            userProfileService.createUserProfile(userProfile)

            Napier.i("Sign Up Successful: ${firebaseUser.uid}", tag = "AuthService")
            Result.success(userProfile)

        } catch (e: Exception) {
            Napier.e("Sign Up Failed", e, tag = "AuthService")
            Result.failure(e)
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<UserProfile> {
        return try {
            Napier.d("Attempting Sign In: $email", tag = "AuthService")

            // 1. Sign in with Firebase Auth
            val result = auth.signInWithEmailAndPassword(email, password)
            val firebaseUser = result.user ?: throw Exception("Sign in failed")

            // 2. Fetch user profile from Firestore
            val profileResult = userProfileService.getUserProfile(firebaseUser.uid)
            val userProfile = profileResult.getOrNull()
                ?: throw Exception("User profile not found")

            Napier.i("Sign In Successful: ${firebaseUser.uid}", tag = "AuthService")
            Result.success(userProfile)

        } catch (e: Exception) {
            Napier.e("Sign In Failed", e, tag = "AuthService")
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<UserProfile> {
        return try {
            Napier.d("Attempting Google Sign In", tag = "AuthService")

            // 1. Sign in with Google credential
            val credential = GoogleAuthProvider.credential(idToken, null)
            val result = auth.signInWithCredential(credential)
            val firebaseUser = result.user ?: throw Exception("Google sign in failed")

            // 2. Check if user profile exists in Firestore
            val existingProfile = userProfileService.getUserProfile(firebaseUser.uid)

            val userProfile = if (existingProfile.isSuccess && existingProfile.getOrNull() != null) {
                // User exists, return existing profile
                existingProfile.getOrNull()!!
            } else {
                // First time Google login - create profile
                val username = generateUsernameFromEmail(firebaseUser.email ?: "")

                // Get Google user info - might be accessed differently in your Firebase SDK
                // For dev.gitlive.firebase.auth, try these approaches:
                val displayName = firebaseUser.displayName ?: username
                val photoUrl = firebaseUser.photoURL

                val newProfile = UserProfile(
                    userId = firebaseUser.uid,
                    username = username,
                    displayName = displayName,
                    email = firebaseUser.email ?: "",
                    avatarUrl = photoUrl,
                    philosophyBio = "Designing the void...",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    preferences = SystemPreferences()
                )
                userProfileService.createUserProfile(newProfile)
                newProfile
            }

            Napier.i("Google Sign In Successful: ${firebaseUser.uid}", tag = "AuthService")
            Result.success(userProfile)

        } catch (e: Exception) {
            Napier.e("Google Sign In Failed", e, tag = "AuthService")
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        Napier.w("User logging out: ${auth.currentUser?.uid}", tag = "AuthService")
        auth.signOut()
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            Napier.d("Sending password reset email to: $email", tag = "AuthService")
            auth.sendPasswordResetEmail(email)
            Napier.i("Password reset email sent successfully", tag = "AuthService")
            Result.success(Unit)
        } catch (e: Exception) {
            Napier.e("Failed to send password reset email", e, tag = "AuthService")
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUserProfile(): UserProfile? {
        val currentFirebaseUser = auth.currentUser ?: return null
        return userProfileService.getUserProfile(currentFirebaseUser.uid).getOrNull()
    }

    private fun generateUsernameFromEmail(email: String): String {
        // Take part before @ and remove special characters
        val baseUsername = email.substringBefore("@")
            .replace(Regex("[^a-zA-Z0-9]"), "")
            .take(20)

        // If empty, use a default
        return baseUsername.ifEmpty { "user_${System.currentTimeMillis()}" }
    }
}
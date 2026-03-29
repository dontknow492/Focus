package com.ghost.todo.data.service

import com.ghost.todo.data.model.SystemPreferences
import com.ghost.todo.data.model.UserProfile
import dev.gitlive.firebase.firestore.FirebaseFirestore
import io.github.aakira.napier.Napier


class UserProfileService(
    private val firestore: FirebaseFirestore
) {

    private val usersCollection = firestore.collection("users")

    // ✅ Create Profile
    suspend fun createUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            Napier.d(
                "Creating profile for UID: ${profile.userId}",
                tag = "UserProfileService"
            )

            usersCollection
                .document(profile.userId)
                .set(profile)

            Napier.i(
                "Profile created successfully: ${profile.userId}",
                tag = "UserProfileService"
            )

            Result.success(Unit)

        } catch (e: Exception) {
            Napier.e(
                "Failed to create profile",
                e,
                tag = "UserProfileService"
            )
            Result.failure(e)
        }
    }

    // ✅ Get Profile (USED BY AUTH SERVICE)
    suspend fun getUserProfile(userId: String): Result<UserProfile> {
        return try {
            Napier.d(
                "Fetching profile for UID: $userId",
                tag = "UserProfileService"
            )

            val snapshot = usersCollection
                .document(userId)
                .get()

            val profile = snapshot.data<UserProfile>()

            if (profile != null) {
                Napier.i(
                    "Profile fetched successfully",
                    tag = "UserProfileService"
                )
                Result.success(profile)
            } else {
                Napier.w(
                    "Profile not found for UID: $userId",
                    tag = "UserProfileService"
                )
                Result.failure(Exception("User profile not found"))
            }

        } catch (e: Exception) {
            Napier.e(
                "Error fetching profile",
                e,
                tag = "UserProfileService"
            )
            Result.failure(e)
        }
    }

    // ✅ Update Full Profile
    suspend fun updateUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            Napier.d(
                "Updating profile for UID: ${profile.userId}",
                tag = "UserProfileService"
            )

            usersCollection
                .document(profile.userId)
                .set(
                    profile.copy(updatedAt = System.currentTimeMillis())
                )

            Napier.i(
                "Profile updated successfully",
                tag = "UserProfileService"
            )

            Result.success(Unit)

        } catch (e: Exception) {
            Napier.e(
                "Failed to update profile",
                e,
                tag = "UserProfileService"
            )
            Result.failure(e)
        }
    }

    // ✅ Partial Update
    suspend fun updateProfileFields(
        userId: String,
        displayName: String? = null,
        avatarUrl: String? = null,
        philosophyBio: String? = null
    ): Result<Unit> {
        return try {
            val updates = mutableMapOf<String, Any>()

            displayName?.let { updates["displayName"] = it }
            avatarUrl?.let { updates["avatarUrl"] = it }
            philosophyBio?.let { updates["philosophyBio"] = it }

            updates["updatedAt"] = System.currentTimeMillis()

            Napier.d(
                "Updating fields for UID: $userId -> $updates",
                tag = "UserProfileService"
            )

            usersCollection
                .document(userId)
                .update(updates)

            Napier.i(
                "Profile fields updated",
                tag = "UserProfileService"
            )

            Result.success(Unit)

        } catch (e: Exception) {
            Napier.e(
                "Failed partial update",
                e,
                tag = "UserProfileService"
            )
            Result.failure(e)
        }
    }

    // ✅ Update Preferences
    suspend fun updatePreferences(
        userId: String,
        preferences: SystemPreferences
    ): Result<Unit> {
        return try {
            Napier.d(
                "Updating preferences for UID: $userId",
                tag = "UserProfileService"
            )

            usersCollection
                .document(userId)
                .update(
                    mapOf(
                        "preferences" to preferences,
                        "updatedAt" to System.currentTimeMillis()
                    )
                )

            Napier.i(
                "Preferences updated",
                tag = "UserProfileService"
            )

            Result.success(Unit)

        } catch (e: Exception) {
            Napier.e(
                "Failed to update preferences",
                e,
                tag = "UserProfileService"
            )
            Result.failure(e)
        }
    }

    // ✅ Check if Profile Exists (Useful for Google login optimization)
    suspend fun profileExists(userId: String): Boolean {
        return try {
            val snapshot = usersCollection
                .document(userId)
                .get()

            snapshot.exists
        } catch (e: Exception) {
            Napier.e(
                "Error checking profile existence",
                e,
                tag = "UserProfileService"
            )
            false
        }
    }
}
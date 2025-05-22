package com.example.taskflow.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    // 🔐 Тіркелу
    suspend fun register(email: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = firebaseAuth.currentUser?.uid ?: return@withContext Result.failure(Exception("UID жоқ"))

                // 🔸 Firestore-да user құжатын жасау (role = user)
                val newUser = hashMapOf(
                    "email" to email,
                    "role" to "user"
                )
                firestore.collection("users").document(userId).set(newUser).await()

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // AuthRepository.kt
    suspend fun getUserRole(userId: String): String {
        val document = FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .get()
            .await()

        return document.getString("role") ?: "user"
    }


    // 🔐 Кіру
    suspend fun login(email: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val userId = firebaseAuth.currentUser?.uid ?: return@withContext Result.failure(Exception("UID жоқ"))

                val doc = firestore.collection("users").document(userId).get().await()
                val role = if (doc.exists()) {
                    doc.getString("role") ?: "user"
                } else {
                    // 🔸 Егер user құжаты жоқ болса, жаңасын жасаймыз
                    val newUser = hashMapOf(
                        "email" to email,
                        "role" to "user"
                    )
                    firestore.collection("users").document(userId).set(newUser).await()
                    "user"
                }

                Result.success(role)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}

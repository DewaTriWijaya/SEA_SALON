package com.dewa.sea.data

import android.content.Context
import android.util.Log
import com.dewa.sea.data.model.DataUser
import com.dewa.sea.utils.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class Repository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    suspend fun registerUser(
        email: String,
        password: String,
        name: String,
        phone: String,
        context: Context
    ): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("User is null")

            val userData = hashMapOf(
                "uid" to user.uid,
                "name" to name,
                "email" to email,
                "phone" to phone,
                "role" to "user"
            )
            fireStore.collection("users").document(user.uid).set(userData).await()
            SharedPreferences(context).apply {
                setEmail(email)
                setLogin(true)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String, context: Context): Result<Unit> {
        return try {
            fireStore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val id = document.id
                        val name = document.getString("name")
                        val phone = document.getString("phone")
                        val role = document.getString("role")

                        SharedPreferences(context).apply {
                            setEmail(email)
                            setName(name.toString())
                            setPhone(phone.toString())
                            setRole(role.toString())
                            setUid(id)
                            setLogin(true)
                        }
                        Log.d("CEK", role.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("UserRepository", "Error getting document: ", e)
                }
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserByEmail(email: String, callback: (DataUser?) -> Unit) {
        fireStore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val id = document.id
                    val name = document.getString("name")
                    val phone = document.getString("phone")
                    val role = document.getString("role")

                    val data = DataUser(
                        email,
                        name.toString(),
                        phone.toString(),
                        role.toString(),
                        id,
                    )
                    callback(data)
                }
            }
            .addOnFailureListener { e ->
                Log.e("UserRepository", "Error getting document: ", e)
                callback(null)
            }
    }

}
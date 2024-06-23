package com.dewa.sea.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.dewa.sea.data.model.DataReservation
import com.dewa.sea.data.model.DataServices
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
                setName(name)
                setPhone(phone)
                setRole("user")
                setUid(user.uid)
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

    fun getServices(callback: (List<DataServices>) -> Unit) {
        fireStore.collection("services")
            .get()
            .addOnSuccessListener { documents ->
                val services = mutableListOf<DataServices>()
                for (document in documents) {
                    val id = document.id
                    val name = document.getString("img_service")
                    val references = document.get("references") as? List<String> ?: emptyList()

                    val data = DataServices(
                        id,
                        name.toString(),
                        references
                    )
                    services.add(data)
                }
                callback(services)
            }
            .addOnFailureListener { e ->
                Log.e("UserRepository", "Error getting document: ", e)
                callback(emptyList())
            }
    }

    fun addReservation(
        uid: String,
        name: String,
        phone: String,
        service: String,
        date: String,
        time: String,
        barcode: String,
        status: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val reservationData = hashMapOf(
            "uid" to uid,
            "name" to name,
            "phone" to phone,
            "service" to service,
            "date" to date,
            "time" to time,
            "barcode" to barcode,
            "status" to status
        )

        fireStore.collection("reservation")
            .add(reservationData)
            .addOnSuccessListener { documentReference ->
                callback(true, documentReference.id)
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }


    fun getReservationsByIDUser(userID: String, callback: (List<DataReservation>) -> Unit) {
        val reservation = mutableListOf<DataReservation>()
        fireStore.collection("reservation")
            .whereEqualTo("uid", userID)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val id = document.id
                    val name = document.getString("name")
                    val phone = document.getString("phone")
                    val service = document.getString("service")
                    val date = document.getString("date")
                    val time = document.getString("time")
                    val barcode = document.getString("barcode")
                    val status = document.getString("status")
                    reservation.add(
                        DataReservation(
                            id,
                            name.toString(),
                            phone.toString(),
                            service.toString(),
                            date.toString(),
                            time.toString(),
                            barcode.toString(),
                            status.toString()
                        )
                    )
                }
                callback(reservation)
            }
            .addOnFailureListener { exception ->
                Log.e("ServiceRepository", "Error getting documents: ", exception)
                callback(emptyList())
            }
    }

    fun deleteReservation(documentId: String, callback: (Boolean, String?) -> Unit) {
        fireStore.collection("reservation")
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }

    fun getReservationsAdmin(callback: (List<DataReservation>) -> Unit) {
        val reservation = mutableListOf<DataReservation>()
        fireStore.collection("reservation").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val id = document.id
                    val name = document.getString("name")
                    val phone = document.getString("phone")
                    val service = document.getString("service")
                    val date = document.getString("date")
                    val time = document.getString("time")
                    val barcode = document.getString("barcode")
                    val status = document.getString("status")
                    reservation.add(
                        DataReservation(
                            id,
                            name.toString(),
                            phone.toString(),
                            service.toString(),
                            date.toString(),
                            time.toString(),
                            barcode.toString(),
                            status.toString()
                        )
                    )
                }
                callback(reservation)
            }
            .addOnFailureListener { exception ->
                Log.e("ServiceRepository", "Error getting documents: ", exception)
                callback(emptyList())
            }
    }

    fun updateReservationStatusAdmin(
        reservationId: String,
        newStatus: String,
        callback: (Boolean) -> Unit
    ) {
        fireStore.collection("reservation")
            .document(reservationId)
            .update("status", newStatus)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.e("Repository", "Error updating document: ", exception)
                callback(false)
            }
    }

}
package com.dewa.sea.data

import android.content.Context
import android.net.Uri
import android.util.Log
import com.dewa.sea.data.model.DataReservation
import com.dewa.sea.data.model.DataReview
import com.dewa.sea.data.model.DataServices
import com.dewa.sea.utils.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class Repository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

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
            .document(barcode)
            .set(reservationData)
            .addOnSuccessListener {
                callback(true, barcode)
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }

    fun getReservedTimes(service: String, date: String, callback: (List<String>) -> Unit) {
        Log.d("CEK REPOSITORY", "$service / $date")
        fireStore.collection("reservation")
            .whereEqualTo("service", service)
            .whereEqualTo("date", date)
            .whereIn("status", listOf("reservation", "proses"))  // cek dulu
            .get()
            .addOnSuccessListener { documents ->
                val reserved = mutableListOf<String>()
                for (document in documents) {
                    val time = document.getString("time")

                    reserved.add(time.toString())
                }

                Log.d("CEK SUCCESS", "$reserved")
                callback(reserved)
            }
            .addOnFailureListener { exception ->
                Log.e("Repository", "Error getting reserved times: ", exception)
                callback(emptyList())
            }
    }

    fun checkIfReviewed(reservationId: String, callback: (Boolean) -> Unit) {
        fireStore.collection("reviews")
            .whereEqualTo("id", reservationId)
            .get()
            .addOnSuccessListener { document ->
                if (!document.isEmpty) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Repository", "Error checking review: ", exception)
                callback(false)
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

    fun addReview(reviewData: DataReview, callback: (Boolean) -> Unit) {
        val reviewMap = hashMapOf(
            "id" to reviewData.id,
            "name" to reviewData.name,
            "service" to reviewData.service,
            "date" to reviewData.date,
            "rating" to reviewData.rating,
            "review" to reviewData.review
        )
        fireStore.collection("reviews")
            .add(reviewMap)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e("Repository", "Error adding review: ", e)
                callback(false)
            }
    }

    fun getReview(serviceData: String, callback: (List<DataReview>) -> Unit) {
        val reviews = mutableListOf<DataReview>()
        fireStore.collection("reviews")
            .whereEqualTo("service", serviceData)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val id = document.id
                    val name = document.getString("name")
                    val service = document.getString("service")
                    val date = document.getString("date")
                    val review = document.getString("review")
                    val ratingBar = document.getString("rating")
                    reviews.add(
                        DataReview(
                            id,
                            name.toString(),
                            service.toString(),
                            date.toString(),
                            ratingBar.toString(),
                            review.toString()
                        )
                    )
                }
                callback(reviews)
            }
            .addOnFailureListener { exception ->
                Log.e("ServiceRepository", "Error getting documents: ", exception)
                callback(emptyList())
            }
    }

    fun checkIDStatus(reservationId: String, callback: (Boolean) -> Unit) {
        fireStore.collection("reservation")
            .whereEqualTo("barcode", reservationId)
            .whereEqualTo("status", "reservation")
            .get()
            .addOnSuccessListener { document ->
                if (!document.isEmpty) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Repository", "Error checking review: ", exception)
                callback(false)
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
        Log.d("CEK", reservationId)
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

    fun addService(
        service: String,
        imgService: Uri,
        imgReferences: List<Uri>,
        callback: (Boolean) -> Unit
    ) {
        val imgServiceRef = storage.reference.child("services/$service/img_service.jpg")

        imgServiceRef.putFile(imgService)
            .addOnSuccessListener {
                imgServiceRef.downloadUrl.addOnSuccessListener { imgServiceUri ->
                    uploadReferences(
                        service,
                        imgServiceUri.toString(),
                        imgReferences,
                        callback
                    )
                }
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    private fun uploadReferences(
        service: String,
        imgServiceUrl: String,
        imgReferences: List<Uri>,
        callback: (Boolean) -> Unit
    ) {
        val imgReferenceUrls = mutableListOf<String>()
        imgReferences.forEachIndexed { index, uri ->
            val imgReferenceRef =
                storage.reference.child("services/$service/img_reference_$index.jpg")
            imgReferenceRef.putFile(uri)
                .addOnSuccessListener {
                    imgReferenceRef.downloadUrl.addOnSuccessListener { imgReferenceUri ->
                        imgReferenceUrls.add(imgReferenceUri.toString())
                        if (imgReferenceUrls.size == imgReferences.size) {
                            saveServiceToFireStore(
                                service,
                                imgServiceUrl,
                                imgReferenceUrls,
                                callback
                            )
                        }
                    }
                }
                .addOnFailureListener {
                    callback(false)
                }
        }
    }

    private fun saveServiceToFireStore(
        service: String,
        imgServiceUrl: String,
        imgReferenceUrls: List<String>,
        callback: (Boolean) -> Unit
    ) {
        val serviceData = mapOf(
            "service" to service,
            "img_service" to imgServiceUrl,
            "references" to imgReferenceUrls
        )
        fireStore.collection("services")
            .document(service)
            .set(serviceData)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

}
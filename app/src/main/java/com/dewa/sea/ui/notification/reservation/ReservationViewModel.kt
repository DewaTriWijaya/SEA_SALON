package com.dewa.sea.ui.notification.reservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dewa.sea.data.Repository
import com.dewa.sea.data.model.DataReservation
import com.dewa.sea.data.model.DataReview

class ReservationViewModel(private val repository: Repository) : ViewModel() {

    private val _services = MutableLiveData<List<DataReservation>>()
    val services: LiveData<List<DataReservation>> get() = _services

    private val _deleteResult = MutableLiveData<Result<Boolean>>()
    val deleteResult: LiveData<Result<Boolean>> = _deleteResult


    fun fetchServices(userID: String) {
        repository.getReservationsByIDUser(userID) { servicesList ->
            _services.value = servicesList
        }
    }

    fun deleteReservation(documentId: String) {
        repository.deleteReservation(documentId) { isSuccess, message ->
            if (isSuccess) {
                _deleteResult.value = Result.success(true)
            } else {
                _deleteResult.value = Result.failure(Exception(message))
            }
        }
    }

    fun addReview(id: String, name: String, service: String, date: String, rating: String, review: String, callback: (Boolean) -> Unit) {
        val reviewData = DataReview(
            id = id,
            name = name,
            service = service,
            date = date,
            rating = rating,
            review = review
        )
        repository.addReview(reviewData) { success ->
            callback(success)
        }
    }

    fun checkIfReviewed(reservationId: String, callback: (Boolean) -> Unit) {
        repository.checkIfReviewed(reservationId, callback)
    }
}
package com.dewa.sea.ui.notification.reservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dewa.sea.data.Repository
import com.dewa.sea.data.model.DataReservation

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
}
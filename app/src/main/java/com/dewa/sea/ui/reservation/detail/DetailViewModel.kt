package com.dewa.sea.ui.reservation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dewa.sea.data.Repository

class DetailViewModel(private val repository: Repository) : ViewModel() {
    private val _reservationResult = MutableLiveData<Result<String>>()
    val reservationResult: LiveData<Result<String>> = _reservationResult

    fun addReservation(
        uid: String,
        name: String,
        phone: String,
        service: String,
        date: String,
        time: String,
        barcode: String,
        status: String
    ) {
        repository.addReservation(uid, name, phone, service, date, time, barcode, status) { isSuccess, message ->
            if (isSuccess) {
                _reservationResult.value = Result.success(message ?: "")
            } else {
                _reservationResult.value = Result.failure(Exception(message))
            }
        }
    }
}
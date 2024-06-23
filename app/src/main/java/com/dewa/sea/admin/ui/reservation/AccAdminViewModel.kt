package com.dewa.sea.admin.ui.reservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dewa.sea.data.Repository
import com.dewa.sea.data.model.DataReservation

class AccAdminViewModel(private val repository: Repository) : ViewModel() {

    private val _services = MutableLiveData<List<DataReservation>>()
    val services: LiveData<List<DataReservation>> get() = _services

    fun fetchServices() {
        repository.getReservationsAdmin { servicesList ->
            _services.value = servicesList
        }
    }
}
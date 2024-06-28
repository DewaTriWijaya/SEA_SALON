package com.dewa.sea.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dewa.sea.data.Repository
import com.dewa.sea.data.model.DataServices

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _services = MutableLiveData<List<DataServices>>()
    val services: LiveData<List<DataServices>> get() = _services

    fun fetchServices() {
        repository.getServices { servicesList ->
            _services.value = servicesList
        }
    }
}
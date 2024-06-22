package com.dewa.sea.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dewa.sea.data.Repository
import com.dewa.sea.data.model.DataUser

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _user = MutableLiveData<DataUser?>()
    val user: LiveData<DataUser?> get() = _user

    fun fetchUserByEmail(email: String) {
        repository.getUserByEmail(email) { fetchedUser ->
            _user.value = fetchedUser
        }
    }
}
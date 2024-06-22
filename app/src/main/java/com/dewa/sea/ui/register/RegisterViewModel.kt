package com.dewa.sea.ui.register

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewa.sea.data.Repository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val authResult = MutableLiveData<Result<Unit>>()

    fun registerUser(email: String, password: String, name: String, phone: String, context: Context) {
        loading.value = true
        viewModelScope.launch {
            val result = repository.registerUser(email, password, name, phone, context)
            loading.value = false
            authResult.value = result
        }
    }
}
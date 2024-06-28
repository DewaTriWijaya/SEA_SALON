package com.dewa.sea.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewa.sea.data.Repository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val authResult = MutableLiveData<Result<Unit>>()
    fun loginUser(email: String, password: String, context: Context) {
        loading.value = true
        viewModelScope.launch {
            val result = repository.loginUser(email, password, context)
            loading.value = false
            authResult.value = result
        }
    }
}
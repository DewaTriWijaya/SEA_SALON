package com.dewa.sea.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dewa.sea.admin.ui.reservation.AccAdminViewModel
import com.dewa.sea.data.Repository
import com.dewa.sea.ui.home.HomeViewModel
import com.dewa.sea.ui.login.LoginViewModel
import com.dewa.sea.ui.notification.reservation.ReservationViewModel
import com.dewa.sea.ui.register.RegisterViewModel
import com.dewa.sea.ui.reservation.detail.DetailViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(ReservationViewModel::class.java)) {
            return ReservationViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(AccAdminViewModel::class.java)) {
            return AccAdminViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
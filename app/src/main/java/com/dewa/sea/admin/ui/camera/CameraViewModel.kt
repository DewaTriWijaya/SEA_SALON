package com.dewa.sea.admin.ui.camera

import androidx.lifecycle.ViewModel
import com.dewa.sea.data.Repository

class CameraViewModel(private val repository: Repository) : ViewModel() {

    fun checkIDStatus(reservationId: String, callback: (Boolean) -> Unit) {
        repository.checkIDStatus(reservationId, callback)
    }

    fun updateReservationStatus(reservationId: String, newStatus: String, callback: (Boolean) -> Unit) {
        repository.updateReservationStatusAdmin(reservationId, newStatus) { success ->
            callback(success)
        }
    }
}
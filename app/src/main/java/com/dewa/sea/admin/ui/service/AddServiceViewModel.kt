package com.dewa.sea.admin.ui.service

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.dewa.sea.data.Repository

class AddServiceViewModel(private val repository: Repository) : ViewModel() {
    fun addService(service: String, imgService: Uri, imgReferences: List<Uri>, callback: (Boolean) -> Unit) {
        repository.addService(service, imgService, imgReferences, callback)
    }
}
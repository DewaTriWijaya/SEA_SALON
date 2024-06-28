package com.dewa.sea.ui.reservation.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dewa.sea.data.Repository
import com.dewa.sea.data.model.DataReview

class ReviewsViewModel(private val repository: Repository) : ViewModel() {

    private val _reviews = MutableLiveData<List<DataReview>>()
    val reviews: LiveData<List<DataReview>> get() = _reviews

    fun fetchReviews(service: String) {
        repository.getReview(service) { reviewsList ->
            _reviews.value = reviewsList
        }
    }
}
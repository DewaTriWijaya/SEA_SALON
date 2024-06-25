package com.dewa.sea.ui.reservation.review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dewa.sea.data.Repository
import com.dewa.sea.data.ViewModelFactory
import com.dewa.sea.databinding.ActivityReviewsBinding

class ReviewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewsBinding
    private val reviewsViewModel: ReviewsViewModel by viewModels {
        ViewModelFactory(Repository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapterReviews()
    }

    private fun adapterReviews() {
        val services = intent.getStringExtra("SERVICE")
        reviewsViewModel.fetchReviews(services.toString())
        binding.rvReviews.layoutManager = LinearLayoutManager(this)

        reviewsViewModel.reviews.observe(this){
            binding.rvReviews.adapter = AdapterReviews(it)
        }
    }
}
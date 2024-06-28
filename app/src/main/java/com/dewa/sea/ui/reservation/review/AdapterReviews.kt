package com.dewa.sea.ui.reservation.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dewa.sea.data.model.DataReview
import com.dewa.sea.databinding.ItemCardReviewsBinding

class AdapterReviews(private val items: List<DataReview>) : RecyclerView.Adapter<AdapterReviews.AdapterPreferenceViewHolder>() {

    inner class AdapterPreferenceViewHolder(private val binding: ItemCardReviewsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: DataReview) {
            binding.tvName.text = item.name
            binding.tvDate.text = item.date
            binding.tvReview.text = item.review
            binding.ratingBar.rating = item.rating.toFloat()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPreferenceViewHolder {
        val binding = ItemCardReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterPreferenceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterPreferenceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
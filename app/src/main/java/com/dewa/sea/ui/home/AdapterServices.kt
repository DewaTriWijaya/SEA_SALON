package com.dewa.sea.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dewa.sea.data.model.DataServices
import com.dewa.sea.databinding.ItemCardServiceBinding
import com.dewa.sea.ui.reservation.detail.DetailReservationActivity

class AdapterServices(private val items: List<DataServices>) :
    RecyclerView.Adapter<AdapterServices.AdapterServiceViewHolder>() {

    inner class AdapterServiceViewHolder(private val binding: ItemCardServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataServices) {
            binding.tvService.text = item.title

            Glide.with(itemView)
                .load(item.imageResId)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(binding.imageService)
            binding.cardService.setOnClickListener {
                val intent =
                    Intent(itemView.context, DetailReservationActivity::class.java).apply {
                        putExtra("TITLE", item.title)
                    }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterServiceViewHolder {
        val binding =
            ItemCardServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterServiceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
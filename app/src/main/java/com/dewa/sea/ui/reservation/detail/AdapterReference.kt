package com.dewa.sea.ui.reservation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dewa.sea.databinding.ItemCardReferenceBinding

class AdapterReference(private val items: List<String>) : RecyclerView.Adapter<AdapterReference.AdapterPreferenceViewHolder>() {

    inner class AdapterPreferenceViewHolder(private val binding: ItemCardReferenceBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: String) {
            Glide.with(itemView)
                .load(item)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(binding.imgReference)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPreferenceViewHolder {
        val binding = ItemCardReferenceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterPreferenceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterPreferenceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
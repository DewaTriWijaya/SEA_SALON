package com.dewa.sea.admin.ui.service

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dewa.sea.databinding.ItemCardReferenceBinding

class AddAdapterReference(private val items: List<Uri>) : RecyclerView.Adapter<AddAdapterReference.AdapterPreferenceViewHolder>() {

    inner class AdapterPreferenceViewHolder(private val binding: ItemCardReferenceBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Uri) {
            binding.imgReference.setImageURI(item)
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
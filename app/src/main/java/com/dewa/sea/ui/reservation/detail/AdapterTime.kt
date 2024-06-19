package com.dewa.sea.ui.reservation.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dewa.sea.databinding.ItemCardTimeBinding

class AdapterTime(private val items: List<String>) : RecyclerView.Adapter<AdapterTime.ButtonViewHolder>() {

    private var selectedItemPosition: Int = -1

    inner class ButtonViewHolder(val binding: ItemCardTimeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val binding = ItemCardTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ButtonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.titleService.text = items[position]

        // Set warna berdasarkan apakah item dipilih atau tidak
        holder.binding.titleService.setBackgroundColor(if (position == selectedItemPosition) Color.GREEN else Color.WHITE)



        holder.binding.root.setOnClickListener {
            val previousItemPosition = selectedItemPosition
            selectedItemPosition = position

            // Notifikasi perubahan pada item yang dipilih sebelumnya dan yang baru dipilih
            notifyItemChanged(previousItemPosition)
            notifyItemChanged(selectedItemPosition)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
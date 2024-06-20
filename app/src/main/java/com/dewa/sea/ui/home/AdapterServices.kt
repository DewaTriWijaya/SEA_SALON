package com.dewa.sea.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dewa.sea.data.model.DataServices
import com.dewa.sea.databinding.DialogAddPhoneBinding
import com.dewa.sea.databinding.ItemCardServiceBinding
import com.dewa.sea.ui.reservation.detail.DetailReservationActivity
import com.dewa.sea.utils.SharedPreferences

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
                if (SharedPreferences(itemView.context).getPhone() == "") {
                    addPhone(itemView.context)
                } else {
                    val intent =
                        Intent(itemView.context, DetailReservationActivity::class.java).apply {
                            putExtra("TITLE", item.title)
                        }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    private fun addPhone(context: Context) {
        val dialogBinding = DialogAddPhoneBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnSave.setOnClickListener {
            val phoneNumber = dialogBinding.etPhoneNumber.text.toString().trim()
            if (phoneNumber.isNotEmpty()) {
                SharedPreferences(context).setPhone(phoneNumber)
                Toast.makeText(context, "Phone Number: $phoneNumber", Toast.LENGTH_SHORT).show()
                builder.dismiss()
            } else {
                dialogBinding.etPhoneNumber.error = "Phone number cannot be empty"
            }
        }
        builder.show()
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
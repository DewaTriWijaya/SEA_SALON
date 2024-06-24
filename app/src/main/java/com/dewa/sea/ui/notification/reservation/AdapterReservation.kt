package com.dewa.sea.ui.notification.reservation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dewa.sea.data.model.DataReservation
import com.dewa.sea.databinding.ItemCardReservationBinding

class AdapterReservation(
    private val callback: OnSelectedListener,
) : ListAdapter<DataReservation, AdapterReservation.AdapterReservationViewHolder>(DiffCallback()) {

    interface OnSelectedListener {
        fun onSelected(id: String, service: String, date: String)
    }

    inner class AdapterReservationViewHolder(private val binding: ItemCardReservationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataReservation) {
            binding.apply {
                tvName.text = item.nameData
                tvService.text = item.serviceData
                tvDate.text = item.dateData
                tvBarcode.text = item.barcodeData
                tvTime.text = item.timeData

                when (item.statusData) {
                    "reservation" ->{
                        btnCancel.text = "Cancel"
                    }
                    "proses" -> {
                        btnCancel.text = "Proses"
                    }
                    "done" ->{
                        btnCancel.text = "Review"
                    }
                }

                binding.btnCancel.setOnClickListener {
                    callback.onSelected(item.id, item.serviceData, item.dateData)
                }
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterReservationViewHolder {
        val binding =
            ItemCardReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterReservationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<DataReservation>() {
        override fun areItemsTheSame(oldItem: DataReservation, newItem: DataReservation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataReservation, newItem: DataReservation): Boolean {
            return oldItem == newItem
        }
    }
}
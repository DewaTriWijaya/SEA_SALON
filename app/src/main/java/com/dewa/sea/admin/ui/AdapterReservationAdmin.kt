package com.dewa.sea.admin.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dewa.sea.data.model.DataReservation
import com.dewa.sea.databinding.ItemCardReservationBinding

class AdapterReservationAdmin(
    private val callback: OnTimeSelectedListener,
) : ListAdapter<DataReservation, AdapterReservationAdmin.AdapterReservationViewHolder>(DiffCallback()) {

    interface OnTimeSelectedListener {
        fun onTimeSelected(id: String)
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
                        btnCancel.text = "Reservation"
                    }
                    "proses" ->{
                        btnCancel.text = "Proses"
                    }
                    "done" ->{
                        btnCancel.text = "Done"
                    }
                }

                binding.btnCancel.setOnClickListener {
                    callback.onTimeSelected(item.id)
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
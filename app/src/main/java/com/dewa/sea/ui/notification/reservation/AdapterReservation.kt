package com.dewa.sea.ui.notification.reservation

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dewa.sea.data.model.DataReservation
import com.dewa.sea.databinding.ItemCardReservationBinding
import com.dewa.sea.ui.reservation.code.CodeReservationActivity

class AdapterReservation(
    private val callback: OnSelectedListener,
    private val viewModel: ReservationViewModel
) : ListAdapter<DataReservation, AdapterReservation.AdapterReservationViewHolder>(DiffCallback()) {

    interface OnSelectedListener {
        fun onSelected(id: String, service: String, date: String)
    }

    inner class AdapterReservationViewHolder(private val binding: ItemCardReservationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: DataReservation) {
            binding.apply {
                tvName.text = item.nameData
                tvService.text = item.serviceData
                tvDate.text = item.dateData
                tvBarcode.text = item.barcodeData
                tvTime.text = item.timeData

                when (item.statusData) {
                    "reservation" -> {
                        btnCancel.text = "Cancel"
                    }

                    "proses" -> {
                        btnCancel.text = "Proses"
                    }

                    "done" -> {
                        viewModel.checkIfReviewed(item.id) {
                            if (it) {
                                btnCancel.text = "Done"
                            } else {
                                btnCancel.text = "Review"
                            }
                        }
                    }
                }

                binding.btnCancel.setOnClickListener {
                    callback.onSelected(item.id, item.serviceData, item.dateData)
                }

                binding.cardService.setOnClickListener {
                    val intent = Intent(itemView.context, CodeReservationActivity::class.java).apply {
                        putExtra("NAME", item.nameData)
                        putExtra("PHONE", item.phoneData)
                        putExtra("SERVICE", item.serviceData)
                        putExtra("DATE", item.dateData)
                        putExtra("TIME", item.timeData)
                        putExtra("BARCODE", item.barcodeData)
                        putExtra("STATUS", item.statusData)
                    }
                    itemView.context.startActivity(intent)
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

        override fun areContentsTheSame(
            oldItem: DataReservation,
            newItem: DataReservation
        ): Boolean {
            return oldItem == newItem
        }
    }
}
package com.dewa.sea.ui.reservation.detail

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dewa.sea.data.model.DataReferences.itemFT
import com.dewa.sea.data.model.DataReferences.itemMP
import com.dewa.sea.data.model.DataReferences.itemsHS
import com.dewa.sea.data.model.DataServices
import com.dewa.sea.databinding.ActivityDetailReservationBinding
import java.util.Calendar

class DetailReservationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailReservationBinding
    private var title = ""
    private var adapter = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timeAdapter()
        dataDetail()
        adapterReference()

        binding.btnDate.setOnClickListener{
            showDatePickerDialog()
        }
    }

    private fun dataDetail() {
        title = intent.getStringExtra("TITLE").toString()
        binding.tvService.text = title
    }

    private fun adapterReference(){
        when(title){
            "Haircuts and Styling" -> adapter = itemsHS
            "Manicure and Pedicure" -> adapter = itemMP
            "Facial Treatments" -> adapter = itemFT
        }
        binding.rvReference.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvReference.adapter = AdapterReference(adapter)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedDate =
                    String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                binding.tvSelectedDate.text = selectedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun timeAdapter() {
        val timeList = mutableListOf<String>()
        for (hour in 9..21) {
            val formattedTime = String.format("%02d:00", hour)
            timeList.add(formattedTime)
        }

        binding.rvTime.layoutManager = GridLayoutManager(this, 3)
        binding.rvTime.adapter = AdapterTime(timeList)
    }

}
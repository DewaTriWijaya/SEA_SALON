package com.dewa.sea.ui.reservation.detail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dewa.sea.data.model.DataReferences.itemFT
import com.dewa.sea.data.model.DataReferences.itemMP
import com.dewa.sea.data.model.DataReferences.itemsHS
import com.dewa.sea.databinding.ActivityDetailReservationBinding
import com.dewa.sea.ui.reservation.code.CodeReservationActivity
import com.dewa.sea.utils.SharedPreferences
import java.util.Calendar
import kotlin.random.Random

class DetailReservationActivity : AppCompatActivity(), AdapterTime.OnTimeSelectedListener {

    private lateinit var binding: ActivityDetailReservationBinding
    private lateinit var pref: SharedPreferences
    private lateinit var adapterTime: AdapterTime
    private var selectedDate: Calendar = Calendar.getInstance()
    private var adapter = listOf<String>()

    private var title = ""
    private var timeReservation = ""
    private var dateReservation = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SharedPreferences(this)

        timeAdapter()
        dataDetail()
        adapterReference()

        binding.btnDate.setOnClickListener {
            showDatePickerDialog()
        }

        addDataReservation()
    }

    @SuppressLint("SetTextI18n")
    private fun dataDetail() {
        title = intent.getStringExtra("TITLE").toString()
        binding.tvService.text = title
        binding.title.text = "$title Model Reference"
        binding.tvName.text = pref.getName()
        binding.tvPhone.text = pref.getPhone()
    }

    private fun adapterReference() {
        when (title) {
            "Haircuts and Styling" -> adapter = itemsHS
            "Manicure and Pedicure" -> adapter = itemMP
            "Facial Treatments" -> adapter = itemFT
        }
        binding.rvReference.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvReference.adapter = AdapterReference(adapter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedDateDialog =
                    String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                binding.tvSelectedDate.text = selectedDateDialog
                dateReservation = selectedDateDialog
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                updateAdapterWithSelectedDate(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun timeAdapter() {
        val timeList = mutableListOf<String>()
        for (hour in 9..21) {
            val formattedTime = String.format("%02d:00", hour)
            timeList.add(formattedTime)
        }

        adapterTime = AdapterTime(this, timeList, this, selectedDate)
        binding.rvTime.layoutManager = GridLayoutManager(this, 3)
        binding.rvTime.adapter = adapterTime
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateAdapterWithSelectedDate(selectedDate: Calendar) {
        adapterTime.setDate(selectedDate)
        adapterTime.notifyDataSetChanged()
    }

    override fun onTimeSelected(time: String) {
        Toast.makeText(this, "Selected time: $time", Toast.LENGTH_SHORT).show()
        timeReservation = time
    }

    private fun generateRandomNumber(): String {
        val number = Random.nextInt(1000, 10000)
        return number.toString()
    }

    private fun addDataReservation() {
        binding.btnReservation.setOnClickListener {
            if (dateReservation.isEmpty()) {
                Toast.makeText(this, "try rechecking the data", Toast.LENGTH_SHORT).show()
            } else if (timeReservation.isEmpty()) {
                Toast.makeText(this, "try rechecking the data", Toast.LENGTH_SHORT).show()
            } else {
                val name = pref.getName()
                val phone = pref.getPhone()
                val service = title
                val date = dateReservation
                val time = timeReservation
                val barcode = pref.getUid() + generateRandomNumber()
                val status = "reservation" // reservation, proses, cancel, done

                Log.d("CEK", "$name & $phone & $service & $date & $time & $status")
                val intent =
                    Intent(this, CodeReservationActivity::class.java).apply {
                        putExtra("NAME", name)
                        putExtra("PHONE", phone)
                        putExtra("SERVICE", service)
                        putExtra("DATE", date)
                        putExtra("TIME", time)
                        putExtra("BARCODE", barcode)
                        putExtra("STATUS", status)
                    }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
        }
    }

}
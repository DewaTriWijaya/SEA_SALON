package com.dewa.sea.ui.reservation.detail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dewa.sea.R
import com.dewa.sea.data.Repository
import com.dewa.sea.databinding.ActivityDetailReservationBinding
import com.dewa.sea.data.ViewModelFactory
import com.dewa.sea.ui.reservation.code.CodeReservationActivity
import com.dewa.sea.utils.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

class DetailReservationActivity : AppCompatActivity(), AdapterTime.OnTimeSelectedListener {

    private lateinit var binding: ActivityDetailReservationBinding
    private lateinit var pref: SharedPreferences
    private lateinit var adapterTime: AdapterTime
    private var selectedDate: Calendar = Calendar.getInstance()

    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory(Repository())
    }

    private var title = ""
    private var timeReservation = ""
    private var dateReservation = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.navigationIcon?.setTint(resources.getColor(R.color.white, theme))

        pref = SharedPreferences(this)

        timeAdapter()
        dataDetail()
        adapterReference()

        binding.btnDate.setOnClickListener {
            showDatePickerDialog()
        }

        addDataReservation()
        observer()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
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
        val references = intent.getStringArrayListExtra("REFERENCES")
        binding.rvReference.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvReference.adapter = AdapterReference(references?.toList() as List<String>)
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

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormatter.format(selectedDate.time)

        adapterTime = AdapterTime(this, timeList, this, selectedDate, detailViewModel, title, formattedDate)
        binding.rvTime.layoutManager = GridLayoutManager(this, 3)
        binding.rvTime.adapter = adapterTime

        // Initial call to get reserved times
        detailViewModel.getReservedTimes(title, formattedDate) { reservedTimes ->
            adapterTime.setReservedTimes(reservedTimes)
            Log.d("CEK DETAIL TIME", "$reservedTimes")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateAdapterWithSelectedDate(selectedDate: Calendar) {
        title = intent.getStringExtra("TITLE").toString()
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormatter.format(selectedDate.time)
        adapterTime.setDate(selectedDate,title,formattedDate)
        adapterTime.notifyDataSetChanged()

        // Update reserved times for the selected date
        Log.d("CEK DETAIL DATA", "$title / $formattedDate")
        detailViewModel.getReservedTimes(title, formattedDate) { reservedTimes ->
            adapterTime.setReservedTimes(reservedTimes)
            Log.d("CEK DETAIL UPDATE", "$reservedTimes")
        }
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
                val uidUser = pref.getUid().toString()
                val nameData = pref.getName().toString()
                val phoneData = pref.getPhone().toString()
                val serviceData = title
                val dateData = dateReservation
                val timeData = timeReservation
                val barcodeData = pref.getUid() + generateRandomNumber()
                val statusData = "reservation" // reservation, proses, cancel, done

                detailViewModel.addReservation(
                    uidUser,
                    nameData,
                    phoneData,
                    serviceData,
                    dateData,
                    timeData,
                    barcodeData,
                    statusData
                )
            }
        }
    }

    private fun observer(){
        detailViewModel.reservationResult.observe(this) { result ->
            result.onSuccess { documentId ->
                Toast.makeText(this, "Reservation added successfully", Toast.LENGTH_SHORT).show()

                val nameData = pref.getName().toString()
                val phoneData = pref.getPhone().toString()
                val serviceData = title
                val dateData = dateReservation
                val timeData = timeReservation
                val barcodeData = pref.getUid() + generateRandomNumber()
                val statusData = "reservation"

                val intent = Intent(this, CodeReservationActivity::class.java).apply {
                    putExtra("NAME", nameData)
                    putExtra("PHONE", phoneData)
                    putExtra("SERVICE", serviceData)
                    putExtra("DATE", dateData)
                    putExtra("TIME", timeData)
                    putExtra("BARCODE", barcodeData)
                    putExtra("STATUS", statusData)
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
            result.onFailure { exception ->
                Toast.makeText(
                    this,
                    "Failed to add reservation: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
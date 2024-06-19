package com.dewa.sea.ui.reservation.code

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dewa.sea.databinding.ActivityCodeReservationBinding

class CodeReservationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCodeReservationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
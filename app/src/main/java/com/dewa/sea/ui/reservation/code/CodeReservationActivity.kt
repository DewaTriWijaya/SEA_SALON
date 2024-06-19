package com.dewa.sea.ui.reservation.code

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dewa.sea.databinding.ActivityCodeReservationBinding
import com.dewa.sea.ui.home.HomeActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

class CodeReservationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCodeReservationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data()

        binding.btnHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun data(){
        val name = intent.getStringExtra("NAME").toString()
        val phone = intent.getStringExtra("PHONE").toString()
        val service = intent.getStringExtra("SERVICE").toString()
        val date = intent.getStringExtra("DATE").toString()
        val time = intent.getStringExtra("TIME").toString()
        val barcode = intent.getStringExtra("BARCODE").toString()
        val status = intent.getStringExtra("STATUS").toString()

        binding.apply {
            tvName.text = name
            tvPhone.text = phone
            tvService.text = service
            tvSelectedDate.text = date
            tvTime.text = time
            tvBarcode.text = barcode
            tvStatus.text = status
            generateBarcode(barcode)
        }
    }

    private fun generateBarcode(data: String) {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 600, 600)
            binding.imgBarcode.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
            Toast.makeText(this, "Error generating barcode: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
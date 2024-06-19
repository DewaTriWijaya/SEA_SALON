package com.dewa.sea.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.dewa.sea.R
import com.dewa.sea.data.model.DataServices
import com.dewa.sea.databinding.ActivityHomeBinding
import com.dewa.sea.databinding.DialogContactBinding
import com.dewa.sea.ui.notification.NotificationPagerActivity
import com.dewa.sea.ui.profile.ProfileActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        serviceAdapter()
        binding.btnContactUs.setOnClickListener {
            contactUsDialog()
        }
    }

    private fun serviceAdapter() {
        val items = listOf(
            DataServices(
                "https://raw.githubusercontent.com/DewaTriWijaya/ImageAsset/main/Haircuts%20and%20Styling.jpg",
                "Haircuts and Styling"
            ),
            DataServices(
                "https://raw.githubusercontent.com/DewaTriWijaya/ImageAsset/main/Manicure%20and%20Pedicure.jpeg",
                "Manicure and Pedicure"
            ),
            DataServices(
                "https://raw.githubusercontent.com/DewaTriWijaya/ImageAsset/main/Facial%20Treatments.jpg",
                "Facial Treatments"
            )
        )

        binding.rvServices.layoutManager = LinearLayoutManager(this)
        binding.rvServices.adapter = AdapterServices(items)
    }

    private fun contactUsDialog() {
        val dialogBinding = DialogContactBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnClose.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notification -> {
                startActivity(Intent(this, NotificationPagerActivity::class.java))
                true
            }
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
package com.dewa.sea.admin.ui.service

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dewa.sea.R
import com.dewa.sea.admin.AdminActivity
import com.dewa.sea.data.Repository
import com.dewa.sea.data.ViewModelFactory
import com.dewa.sea.databinding.ActivityAddServiceAdminBinding

class AddServiceAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddServiceAdminBinding
    private val viewModel: AddServiceViewModel by viewModels {
        ViewModelFactory(Repository())
    }
    private var imgServiceUri: Uri? = null
    private val imgReferenceUris = mutableListOf<Uri>()

    private val pickServiceImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imgServiceUri = it
            binding.imgReference.setImageURI(it)
        }
    }

    private val pickReferenceImages = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        uris.let {
            imgReferenceUris.clear()
            imgReferenceUris.addAll(it)

            // Update RecyclerView adapter with new URIs
            binding.rvReference.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.rvReference.adapter = AddAdapterReference(imgReferenceUris.toList())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServiceAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.navigationIcon?.setTint(resources.getColor(R.color.white, theme))

        binding.btnAddFotoService.setOnClickListener {
            pickServiceImage.launch("image/*")
        }

        binding.btnAddFotoReferences.setOnClickListener {
            pickReferenceImages.launch("image/*")
        }

        binding.btnAddService.setOnClickListener {
            addService()
        }
    }

    private fun addService() {
        val serviceName = binding.edtNameService.text.toString()

        if (serviceName.isNotEmpty() && imgServiceUri != null && imgReferenceUris.isNotEmpty()) {
            viewModel.addService(serviceName, imgServiceUri!!, imgReferenceUris) { success ->
                if (success) {
                    Toast.makeText(this, "Service added successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AdminActivity::class.java))
                } else {
                    Toast.makeText(this, "Failed to add service", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
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
}
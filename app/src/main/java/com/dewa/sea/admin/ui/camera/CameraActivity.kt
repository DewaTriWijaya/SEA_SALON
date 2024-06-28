package com.dewa.sea.admin.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dewa.sea.R
import com.dewa.sea.admin.AdminActivity
import com.dewa.sea.data.Repository
import com.dewa.sea.data.ViewModelFactory
import com.dewa.sea.databinding.ActivityCameraBinding
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var previewView: PreviewView

    private val cameraViewModel: CameraViewModel by viewModels {
        ViewModelFactory(Repository())
    }
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewView = findViewById(R.id.previewView)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(cameraExecutor) { imageProxy ->
                    processImageProxy(imageProxy)
                }
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (exc: Exception) {
                Log.e("MainActivity", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        val rawValue = barcode.rawValue
                        if (rawValue != null) {
                            runOnUiThread {
                                cameraViewModel.checkIDStatus(rawValue) { isReviewed ->
                                    if (isReviewed) {
                                        cameraViewModel.updateReservationStatus(rawValue, "proses") { success ->
                                            if (success) {
                                                showAlertDialog()
                                            } else {
                                                Toast.makeText(binding.root.context, "Failed to update status", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                    } else {
                                        Toast.makeText(this, "Reservation ID Done", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e("MainActivity", "Barcode scanning failed", it)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reservation Acc")
        builder.setPositiveButton("OK") { _, _ ->
            Toast.makeText(binding.root.context, "Status updated to proses", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AdminActivity::class.java))
            finish()
        }
        builder.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        }
    }
}
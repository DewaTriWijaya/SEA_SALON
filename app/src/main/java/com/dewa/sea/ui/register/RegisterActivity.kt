package com.dewa.sea.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dewa.sea.data.Repository
import com.dewa.sea.databinding.ActivityRegisterBinding
import com.dewa.sea.ui.ViewModelFactory
import com.dewa.sea.ui.home.HomeActivity
import com.dewa.sea.ui.login.LoginActivity
import com.dewa.sea.utils.SharedPreferences

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory(Repository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        register()
        btnLogin()
    }

    private fun btnLogin() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun register() {
        registerViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        registerViewModel.authResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Register successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
            }
            result.onFailure { exception ->
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.tvEmail.text.toString().trim()
            val password = binding.tvPassword.text.toString().trim()
            val name = binding.tvName.text.toString().trim()
            val phone = binding.tvPhone.text.toString().trim()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                registerViewModel.registerUser(email, password, name, phone, this)
            }
        }
    }
}
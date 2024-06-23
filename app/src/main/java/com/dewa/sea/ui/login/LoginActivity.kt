package com.dewa.sea.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dewa.sea.admin.AdminActivity
import com.dewa.sea.data.Repository
import com.dewa.sea.databinding.ActivityLoginBinding
import com.dewa.sea.data.ViewModelFactory
import com.dewa.sea.ui.home.HomeActivity
import com.dewa.sea.ui.register.RegisterActivity
import com.dewa.sea.utils.SharedPreferences

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: SharedPreferences

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(Repository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SharedPreferences(this)

        loginEmailPassword()
        btnRegister()
    }

    private fun btnRegister() {
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginEmailPassword() {
        loginViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        loginViewModel.authResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "successful", Toast.LENGTH_SHORT).show()

                val role = pref.getRole().toString()
                Log.d("CEK", role)

                if (role == "user") {
                    startActivity(Intent(this, HomeActivity::class.java))
                } else if (role == "admin") {
                    startActivity(Intent(this, AdminActivity::class.java))
                }
            }
            result.onFailure { exception ->
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.tvEmail.text.toString().trim()
            val password = binding.tvPassword.text.toString().trim()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.loginUser(email, password, this)
            }
        }
    }
}
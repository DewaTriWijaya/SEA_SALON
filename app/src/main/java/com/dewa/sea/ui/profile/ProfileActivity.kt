package com.dewa.sea.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dewa.sea.databinding.ActivityProfileBinding
import com.dewa.sea.ui.login.LoginActivity
import com.dewa.sea.utils.SharedPreferences

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        pref = SharedPreferences(this)
        data()

        binding.btnLogout.setOnClickListener {
            logout()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun data() {
        binding.apply {
            tvEmail.text = pref.getEmail()
            tvName.text = pref.getName()
            tvPhone.text = pref.getPhone()
        }
    }

    private fun logout(){
        pref.apply {
            setName("")
            setEmail("")
            setUid("")
            setPhone("")
            setLogin(false)
        }
    }
}
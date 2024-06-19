package com.dewa.sea.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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

        pref = SharedPreferences(this)
        data()

        binding.btnLogout.setOnClickListener {
            logout()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun data() {
        Glide.with(this)
            .load(pref.getPhoto())
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(binding.imgProfile)

        binding.apply {
            tvEmail.text = pref.getEmail()
            tvName.text = pref.getName()
        }
    }

    private fun logout(){
        pref.apply {
            setPhoto("")
            setName("")
            setEmail("")
            setUid("")
            setLogin(false)
        }
    }
}
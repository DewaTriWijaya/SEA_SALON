package com.dewa.sea

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dewa.sea.admin.AdminActivity
import com.dewa.sea.databinding.ActivityMainBinding
import com.dewa.sea.ui.home.HomeActivity
import com.dewa.sea.ui.login.LoginActivity
import com.dewa.sea.utils.SharedPreferences

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var delayNumber = 2000
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SharedPreferences(this)

        if (pref.getLogin()) {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    run {
                        val role = pref.getRole().toString()
                        if (role == "user") {
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        }else if (role == "admin"){
                            val intent = Intent(this@MainActivity, AdminActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }
                }, delayNumber.toLong()
            )
        } else {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }
}
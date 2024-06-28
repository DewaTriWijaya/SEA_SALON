package com.dewa.sea.utils

import android.content.Context
import androidx.preference.PreferenceManager
import android.content.SharedPreferences

class SharedPreferences(context: Context) {

    private var sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = sharedPreferences.edit()

    fun getLogin(): Boolean {
        return sharedPreferences.getBoolean(THEME, false)
    }
    fun setLogin(theme: Boolean) {
        editor.putBoolean(THEME, theme)
        editor.apply()
    }

    fun getUid(): String? {
        return sharedPreferences.getString(UID, "")
    }
    fun setUid(user: String) {
        editor.putString(UID, user)
        editor.apply()
    }

    fun getName(): String? {
        return sharedPreferences.getString(NAME, "")
    }
    fun setName(user: String) {
        editor.putString(NAME, user)
        editor.apply()
    }

    fun getEmail(): String? {
        return sharedPreferences.getString(EMAIL, "")
    }
    fun setEmail(user: String) {
        editor.putString(EMAIL, user)
        editor.apply()
    }

    fun getPhone(): String? {
        return sharedPreferences.getString(PHONE, "")
    }
    fun setPhone(user: String) {
        editor.putString(PHONE, user)
        editor.apply()
    }

    fun getRole(): String? {
        return sharedPreferences.getString(ROLE, "")
    }
    fun setRole(user: String) {
        editor.putString(ROLE, user)
        editor.apply()
    }

    companion object {
        private const val THEME = "theme"
        private const val EMAIL = "email"
        private const val NAME = "name"
        private const val PHONE ="phone"
        private const val UID = "uid"
        private const val ROLE = "role"
    }
}
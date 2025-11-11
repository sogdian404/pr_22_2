package com.example.pr22_2

import android.content.Context

class Preferences(private val context: Context) {
    private val prefs = context.getSharedPreferences("currate_prefs", Context.MODE_PRIVATE)

    var userName: String?
        get() = prefs.getString("user_name", null)
        set(value) = prefs.edit().putString("user_name", value).apply()

    var lastRatesJson: String?
        get() = prefs.getString("last_rates_json", null)
        set(value) = prefs.edit().putString("last_rates_json", value).apply()
}

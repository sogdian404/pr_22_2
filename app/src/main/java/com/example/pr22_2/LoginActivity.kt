package com.example.pr22_2


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var prefs: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = Preferences(this)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Если пользователь уже есть - переходим сразу
        prefs.userName?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {
            val name = etUsername.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Имя не должно быть пустым", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            prefs.userName = name
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}

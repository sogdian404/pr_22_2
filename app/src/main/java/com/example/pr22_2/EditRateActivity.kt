package com.example.pr22_2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditRateActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_rate)

        db = AppDatabase.getInstance(this)

        val etPair = findViewById<EditText>(R.id.etPair)
        val etRate = findViewById<EditText>(R.id.etRate)
        val btnSave = findViewById<Button>(R.id.btnSaveRate)

        val id = intent.getIntExtra("id", -1)
        val pair = intent.getStringExtra("pair")
        val rate = intent.getStringExtra("rate")
        if (pair != null) etPair.setText(pair)
        if (rate != null) etRate.setText(rate)

        btnSave.setOnClickListener {
            val p = etPair.text.toString().trim()
            val r = etRate.text.toString().trim()
            if (p.isEmpty() || r.isEmpty()) {
                Toast.makeText(this, "Поля не должны быть пустыми", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                if (id != -1) {
                    db.rateDao().update(RateEntity(id = id, pair = p, rate = r))
                } else {
                    db.rateDao().insert(RateEntity(pair = p, rate = r))
                }
                runOnUiThread {
                    Toast.makeText(this@EditRateActivity, "Сохранено", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}

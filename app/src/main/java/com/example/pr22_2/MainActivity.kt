package com.example.pr22_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val API_KEY = "3ffa5f468e750aa0952b02b0bf754f61"
    private lateinit var prefs: Preferences
    private lateinit var db: AppDatabase
    private lateinit var adapter: RatesAdapter
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = Preferences(this)
        db = AppDatabase.getInstance(this)

        val tvUser = findViewById<TextView>(R.id.tvUser)
        val etPairs = findViewById<EditText>(R.id.etPairs)
        val btnFetch = findViewById<Button>(R.id.btnFetch)
        val tvRawJson = findViewById<TextView>(R.id.tvRawJson)
        val rvRates = findViewById<RecyclerView>(R.id.rvRates)
        val btnOpenSaved = findViewById<Button>(R.id.btnOpenSaved)

        tvUser.text = "Пользователь: ${prefs.userName ?: "—"}"

        adapter = RatesAdapter(mutableListOf(), onSave = { pair, rate ->
            // сохраняем в ROOM
            lifecycleScope.launch(Dispatchers.IO) {
                db.rateDao().insert(RateEntity(pair = pair, rate = rate))
            }
            runOnUiThread { Toast.makeText(this, "Сохранено в базе", Toast.LENGTH_SHORT).show() }
        }, onEdit = { pair, rate, id ->
            // открыть EditRateActivity для редактирования существующей записи если id != null
            val intent = Intent(this, EditRateActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("pair", pair)
            intent.putExtra("rate", rate)
            startActivity(intent)
        }, onDelete = { id ->
            lifecycleScope.launch(Dispatchers.IO) {
                val dao = db.rateDao()
                // требуется получить объект для удаления — упрощенно: создаём с id
                dao.delete(RateEntity(id = id, pair = "", rate = ""))
            }
        })

        rvRates.layoutManager = LinearLayoutManager(this)
        rvRates.adapter = adapter

        // если есть последний json - показать
        prefs.lastRatesJson?.let {
            tvRawJson.text = it
            parseAndShow(it)
        }

        btnFetch.setOnClickListener {
            val pairs = etPairs.text.toString().trim()
            if (pairs.isEmpty()) {
                Toast.makeText(this, "Введите хотя бы одну валютную пару", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            fetchRates(pairs, tvRawJson)
        }

        btnOpenSaved.setOnClickListener {
            startActivity(Intent(this, SavedRatesActivity::class.java))
        }
    }

    private fun fetchRates(pairs: String, tvRawJson: TextView) {
        val url = "https://currate.ru/api/?get=rates&pairs=${pairs}&key=$API_KEY"
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(url,
            { response ->
                prefs.lastRatesJson = response
                tvRawJson.text = response
                parseAndShow(response)
            },
            { error ->
                Toast.makeText(this, "Ошибка сети: ${error.message}", Toast.LENGTH_SHORT).show()
            })
        queue.add(request)
    }
    private fun parseAndShow(response: String) {
        try {
            val obj = JSONObject(response)
            val data = obj.getJSONObject("data")
            val list = mutableListOf<Pair<String, String>>()
            data.keys().forEach { key ->
                val value = data.getString(key)
                list.add(key to value)
            }
            adapter.update(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
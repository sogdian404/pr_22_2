package com.example.pr22_2


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedRatesActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: SavedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_rates)

        db = AppDatabase.getInstance(this)

        val rvSaved = findViewById<RecyclerView>(R.id.rvSaved)
        val btnAddManual = findViewById<Button>(R.id.btnAddManual)

        adapter = SavedAdapter(mutableListOf(),
            onEdit = { rate ->
                val i = Intent(this, EditRateActivity::class.java)
                i.putExtra("id", rate.id)
                i.putExtra("pair", rate.pair)
                i.putExtra("rate", rate.rate)
                startActivity(i)
            },
            onDelete = { rate ->
                CoroutineScope(Dispatchers.IO).launch {
                    db.rateDao().delete(rate)
                }
            })
        rvSaved.layoutManager = LinearLayoutManager(this)
        rvSaved.adapter = adapter

        db.rateDao().getAll().observe(this, Observer { list ->
            adapter.update(list.toMutableList())
        })

        btnAddManual.setOnClickListener {
            val i = Intent(this, EditRateActivity::class.java)
            startActivity(i) // без extras => создание новой записи
        }
    }
}

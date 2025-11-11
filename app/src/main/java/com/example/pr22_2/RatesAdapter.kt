package com.example.pr22_2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RatesAdapter(
    private var items: MutableList<Pair<String, String>>,
    private val onSave: (pair: String, rate: String) -> Unit,
    private val onEdit: (pair: String, rate: String, id: Int?) -> Unit,
    private val onDelete: (id: Int) -> Unit
) : RecyclerView.Adapter<RatesAdapter.VH>() {

    class VH(view: View): RecyclerView.ViewHolder(view) {
        val tvPair: TextView = view.findViewById(R.id.tvPair)
        val tvRate: TextView = view.findViewById(R.id.tvRate)
        val btnSave: Button = view.findViewById(R.id.btnSave)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val (pair, rate) = items[position]
        holder.tvPair.text = pair
        holder.tvRate.text = rate
        holder.btnSave.setOnClickListener { onSave(pair, rate) }
        holder.btnEdit.setOnClickListener { onEdit(pair, rate, null) }
        // здесь id не известно — для удаление в ROOM используйте экран "Сохранённые записи"
        holder.btnDelete.setOnClickListener { /* noop */ }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<Pair<String, String>>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

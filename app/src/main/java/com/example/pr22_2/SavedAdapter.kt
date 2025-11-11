package com.example.pr22_2


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SavedAdapter(
    private var items: MutableList<RateEntity>,
    private val onEdit: (RateEntity) -> Unit,
    private val onDelete: (RateEntity) -> Unit
) : RecyclerView.Adapter<SavedAdapter.VH>() {

    class VH(view: View): RecyclerView.ViewHolder(view) {
        val tvPair: TextView = view.findViewById(R.id.tvPairS)
        val tvRate: TextView = view.findViewById(R.id.tvRateS)
        val btnEdit: Button = view.findViewById(R.id.btnEditS)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteS)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.saved_item, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tvPair.text = item.pair
        holder.tvRate.text = item.rate
        holder.btnEdit.setOnClickListener { onEdit(item) }
        holder.btnDelete.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: MutableList<RateEntity>) {
        items = newItems
        notifyDataSetChanged()
    }
}

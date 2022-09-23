package com.example.tubes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes.entity.Pelanggan
import org.w3c.dom.Text

class RVPelangganAdapter(private val data: Array<Pelanggan>) : RecyclerView.Adapter<RVPelangganAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_pelanggan, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvNama.text = currentItem.nama
        holder.tvWorkout.text = currentItem.workout
        holder.tvMulai.text = currentItem.jamMulai
        holder.tvAkhir.text = currentItem.jamAkhir
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvNama : TextView = itemView.findViewById(R.id.namaTrainer)
        val tvWorkout : TextView = itemView.findViewById(R.id.workoutPlan)
        val tvMulai : TextView = itemView.findViewById(R.id.jamMulai)
        val tvAkhir : TextView = itemView.findViewById(R.id.jamAkhir)
    }
}
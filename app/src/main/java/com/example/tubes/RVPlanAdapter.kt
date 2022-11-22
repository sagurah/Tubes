package com.example.tubes

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes.main_fragment.PlansFragment
import com.example.tubes.room.jadwal.Jadwal
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

//class RVPlanAdapter(private val data: Array<Jadwal>) : RecyclerView.Adapter<RVPlanAdapter.viewHolder>() {
class RVPlanAdapter(
    private var data: List<Jadwal>, context: Context, fragment: Fragment
) : RecyclerView.Adapter<RVPlanAdapter.viewHolder>(), Filterable {
    private val context: Context
    private val fragment: Fragment
    private var filteredList: MutableList<Jadwal>

    init {
        filteredList = ArrayList<Jadwal>(data)
        this.context = context
        this.fragment = fragment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_plan, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = filteredList[position]
        holder.tvNama.text = currentItem.namaTrainer
        holder.tvWorkout.text = currentItem.plan
        holder.tvMulai.text = currentItem.jamMulai
        holder.tvAkhir.text = currentItem.jamAkhir

        holder.btnDelete.setOnClickListener{
            val dialog = MaterialAlertDialogBuilder(context)
            dialog.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin untuk delete?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_,_ ->
                    if(fragment is PlansFragment) currentItem.id?.let{ it1 ->
                        fragment.deletePlan(
                            it1
                        )
                    }
                }
                .show()
        }
    }

    fun setData(data2: Array<Jadwal>) {
        this.data = data2.toList()
        filteredList = data2.toMutableList()
    }
    override fun getItemCount(): Int {
        return filteredList.size
    }

    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvNama : TextView = itemView.findViewById(R.id.namaTrainer)
        val tvWorkout : TextView = itemView.findViewById(R.id.workoutPlan)
        val tvMulai : TextView = itemView.findViewById(R.id.jamMulai)
        val tvAkhir : TextView = itemView.findViewById(R.id.jamAkhir)
        val btnDelete : Button = itemView.findViewById(R.id.btnDelete)
        val btnEdit : Button = itemView.findViewById(R.id.btnEdit)
    }
    override fun getFilter(): Filter {
        return object : Filter (){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Jadwal> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(data)
                }
                else{
                    for(jadwal in data){
                        if(jadwal.namaTrainer.lowercase(Locale.getDefault()).contains(charSequenceString.lowercase(Locale.getDefault()))){
                            filtered.add(jadwal)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults : FilterResults){
                filteredList.clear()
                filteredList.addAll(filterResults.values as List<Jadwal>)
                notifyDataSetChanged()
                Log.d("bindviewpub", filteredList.size.toString())
            }
        }

    }

}
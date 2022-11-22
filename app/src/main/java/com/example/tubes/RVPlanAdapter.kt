package com.example.tubes

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes.main_fragment.PlansFragment
import com.example.tubes.room.jadwal.Jadwal
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import com.itextpdf.kernel.pdf.PdfWriter
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph

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

        holder.btnEdit.setOnClickListener{

        }

        holder.btnCetak.setOnClickListener{
            val dialog = MaterialAlertDialogBuilder(context)
            dialog.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin untuk membuat PDF?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Buat"){_,_ ->
                    createPdf(namaTrainer, jamMulai, jamAkhir, currentItem.id)
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
        val btnCetak : Button = itemView.findViewById(R.id.btnCetak)
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

    private fun createPdf(namaTrainer: String, plan: String, jamMulai: String, jamAkhir: String, index: Int){
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
        val file = File(pdfPath, "Jadwal_Latihan_" + index + ".pdf")
        FileOutputStream(file)

        val writer = PdfWriter(file)
        val pdfDoc = PdfDocument(writer)
        val doc = Document(pdfDoc)
        pdfDoc.defaultPageSize = PageSize.A4
        doc.setMargins(5f, 5f, 5f, 5f)

        val width = floatArrayOf(100f, 100f)
        val table = Table(width)

        val group = Paragraph(
            """
                Data Workout Plans: 
            """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Nama Trainer")))
        table.addCell(Cell().add(Paragraph(namaTrainer)))
        table.addCell(Cell().add(Paragraph("Workout Plan")))
        table.addCell(Cell().add(Paragraph(plan)))
        table.addCell(Cell().add(Paragraph("Jam Mulai")))
        table.addCell(Cell().add(Paragraph(jamMulai)))
        table.addCell(Cell().add(Paragraph("Jam Akhir")))
        table.addCell(Cell().add(Paragraph(jamAkhir)))

        doc.add(group)
        doc.add(table)

        doc.close()

        Toast.makeText(this, "PDF Created", Toast.LENGTH_SHORT).show()
    }

}
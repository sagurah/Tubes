package com.example.tubes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.tubes.room.JadwalDB
import com.example.tubes.room.jadwal.Jadwal
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class EditPlansActivity : AppCompatActivity() {

    private lateinit var btnSubmit: Button
    private lateinit var tilNamaTrainer: TextInputLayout
    private lateinit var tilWorkoutPlan: TextInputLayout
    private lateinit var tilJamMulai: TextInputLayout
    private lateinit var tilJamAkhir: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_plan)

        btnSubmit = findViewById(R.id.btnSave)
        tilNamaTrainer = findViewById(R.id.namaTrainer)
        tilWorkoutPlan = findViewById(R.id.workoutPlan)
        tilJamAkhir = findViewById(R.id.jamAkhir)
        tilJamMulai = findViewById(R.id.jamMulai)

        val db by lazy { JadwalDB(this) }
        val jadwalDAO = db.JadwalDAO()

        btnSubmit.setOnClickListener() {
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("DB TEST", "D A O")
                val jadwal = Jadwal(0, tilNamaTrainer.editText?.text.toString(), tilJamMulai.editText?.text.toString(), tilJamAkhir.editText?.text.toString(), tilWorkoutPlan.editText?.text.toString())
                jadwalDAO.addJadwal(jadwal)
            }
        }
    }
}
package com.example.tubes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes.api.PlansApi
import com.example.tubes.database.Constant
import com.example.tubes.databinding.ActivityEditPlanBinding
import com.example.tubes.entity.ResponseCreate
import com.example.tubes.entity.ResponseData
import com.example.tubes.models.Plans
import com.example.tubes.room.JadwalDB
import com.example.tubes.room.jadwal.Jadwal
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_plan.*
import kotlinx.android.synthetic.main.rv_item_plan.*
import kotlinx.android.synthetic.main.rv_item_plan.jamAkhir
import kotlinx.android.synthetic.main.rv_item_plan.jamMulai
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.w3c.dom.Text
import java.nio.charset.StandardCharsets

class EditPlansActivity : AppCompatActivity() {

    private var id: String = ""
    private var binding: ActivityEditPlanBinding? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        queue = Volley.newRequestQueue(this)
        binding = ActivityEditPlanBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupView()
        setupListener()
    }
    fun setupView(){

        val intentType = intent.getIntExtra("intent_type", 0)

        when (intentType){
            Constant.TYPE_CREATE -> {
                btnSaveUpdate.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                btnSaveCreate.visibility = View.GONE
                btnSaveUpdate.visibility = View.GONE
                getNote()
            }
            Constant.TYPE_UPDATE -> {
                btnSaveCreate.visibility = View.GONE
                getNote()
            }
        }
    }

    private fun setupListener() {

        btnSaveCreate.setOnClickListener{
            var temp :Plans = Plans(binding?.namaTrainer?.editText?.text.toString(), binding?.workoutPlan?.editText?.text.toString(), binding?.jamMulai?.editText?.text.toString(), binding?.jamAkhir?.editText?.text.toString())
            createPlans()
        }
        btnSaveUpdate.setOnClickListener {
            updatePlans(intent.getStringExtra("intent_id")!!)
        }
    }
    fun getNote() {
        id = intent.getStringExtra("intent_id")!!

        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PlansApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val plan = gson.fromJson(response, ResponseData::class.java).data[0]
                if(plan != null)
                    Toast.makeText(this@EditPlansActivity, "Data berhasil diambil", Toast.LENGTH_SHORT).show()

                binding?.namaTrainer?.getEditText()?.setText(plan.nama_trainer)
                binding?.workoutPlan?.getEditText()?.setText(plan.plan)
                binding?.jamMulai?.getEditText()?.setText(plan.jam_mulai)
                binding?.jamAkhir?.getEditText()?.setText(plan.jam_akhir)
//                binding?.namaTrainer?.settext(plan.data[0].nama_trainer.toString())
//                binding?.workoutPlan?.setText(plan.data[0].plan.toString())
//                binding?.jamMulai?.setText(plan.data[0].jam_mulai.toString())
//                binding?.jamAkhir?.setText(plan.data[0].jam_akhir.toString())

            }, Response.ErrorListener { error ->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@EditPlansActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                }
                catch (e:Exception){
                    Toast.makeText(this@EditPlansActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>{
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

        }
        queue!!.add(stringRequest)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun createPlans(){
        val plan = Plans(
            binding!!.namaTrainer.getEditText()?.getText().toString(),
            binding!!.workoutPlan.getEditText()?.getText().toString(),
            binding!!.jamMulai.getEditText()?.getText().toString(),
            binding!!.jamAkhir.getEditText()?.getText().toString(),
        )
        val stringRequest: StringRequest = object :
            StringRequest(Method.POST, PlansApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                val plan = gson.fromJson(response, ResponseCreate::class.java)

                if(plan != null)
                    Toast.makeText(this@EditPlansActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()
            }, Response.ErrorListener { error ->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@EditPlansActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                }
                catch (e:Exception){
                    Toast.makeText(this@EditPlansActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>{
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray{
                val gson = Gson()
                val requestBody = gson.toJson(plan)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(stringRequest)
    }

    private fun updatePlans(id: String){

        val plan = Plans(
            binding!!.namaTrainer.getEditText()?.getText().toString(),
            binding!!.workoutPlan.getEditText()?.getText().toString(),
            binding!!.jamMulai.getEditText()?.getText().toString(),
            binding!!.jamAkhir.getEditText()?.getText().toString(),
            //Test API Di postman trus yg di pake tu id ato namatrainer kau coba ya cuk oawkoawk
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, PlansApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                val plan = gson.fromJson(response, ResponseCreate::class.java)

                if(plan != null)
                    Toast.makeText(this@EditPlansActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()
            }, Response.ErrorListener { error ->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@EditPlansActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                }
                catch (e:Exception){
                    Toast.makeText(this@EditPlansActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>{
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray{
                val gson = Gson()
                val requestBody = gson.toJson(plan)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(stringRequest)
    }

}

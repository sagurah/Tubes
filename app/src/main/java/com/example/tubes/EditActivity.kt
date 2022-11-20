package com.example.tubes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes.*
import com.example.tubes.api.UserApi
import com.example.tubes.models.User
//import com.example.tubes.room.user.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_register.view.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class EditActivity : AppCompatActivity() {
    private val id = "id"
    private val preference = "myPref"
    var sharedPreferences : SharedPreferences? = null

    private val CHANNEL_ID_REGISTER = "channel_notification_01"
    private val notificationId = 101
//    private var bind: ActivityEditBinding? = null
//    private val binding get() = bind!!

    private var queue: RequestQueue? = null
    private var username: EditText? = null
    private var password: EditText? = null
    private var email: EditText? = null
    private var tglLahir: EditText? = null
    private var noTelp: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        queue = Volley.newRequestQueue(this)
        username = findViewById(R.id.tilUsername)
        password = findViewById(R.id.tilPassword)
        email = findViewById(R.id.tilEmail)
        tglLahir = findViewById(R.id.tilTglLahir)
        noTelp = findViewById(R.id.tilNoTelp)

        val btnSave = findViewById<Button>(R.id.btnEditProfile)
        val id = intent.getLongExtra("id", -1)
        if (id != -1L) {
            getUserById(id)
            btnSave.setOnClickListener { updateUser(id) }
        }

//        btnEditProfile.setOnClickListener(){
//            val username: String = findViewById<View?>(R.id.tilUsername).toString()
//            val password: String = findViewById<View?>(R.id.tilPassword).toString()
//            val email: String = findViewById<View?>(R.id.tilEmail).toString()
//            val tglLahir: String = findViewById<View?>(R.id.tilTglLahir).toString()
//            val noTelp: String = findViewById<View?>(R.id.tilNoTelp).toString()
//
//            CoroutineScope(Dispatchers.IO).launch {
//                val user = User(0, username, password, tglLahir, email, noTelp)
//                userDAO.updateUser(user)
//
//                createNotificationChannel()
//                sendNotification(username)
//
//                replaceFragment(ProfileFragment())
//            }
//        }

    }

    private fun getUserById(id: Long) {
        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET,
                UserApi.GET_BY_ID_URL + id,
                Response.Listener { response ->
                    val gson = Gson()
                    val user = gson.fromJson(response, User::class.java)

                    username!!.setText(user.username)
                    password!!.setText(user.password)
                    email!!.setText(user.email)
                    tglLahir!!.setText(user.tgl_lahir)
                    noTelp!!.setText(user.no_telp)

                    Toast.makeText(
                        this@EditActivity,
                        "Data berhasil diambil!",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                Response.ErrorListener { error ->
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@EditActivity,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun updateUser(id: Long){
        val user = User(
            username!!.text.toString(),
            password!!.text.toString(),
            email!!.text.toString(),
            tglLahir!!.text.toString(),
            noTelp!!.text.toString()
        )

        val stringRequest: StringRequest =
            object: StringRequest(Method.PUT, UserApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                var user = gson.fromJson(response, User::class.java)

                if(user != null)
                    Toast.makeText(this@EditActivity, "Data berhasil diubah", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

            }, Response.ErrorListener { error ->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@EditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(user)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }

    fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Edit"
            val descTxt = "Notification Description"

            val ch = NotificationChannel(CHANNEL_ID_REGISTER, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descTxt
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(ch)
        }
    }

    private fun sendNotification(user: String){
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val broadcastIntent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", "Profile Berhasil diedit!")
        val actionIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_REGISTER)
            .setSmallIcon(R.drawable.ic_baseline_circle_icon_notifications_24)
            .setContentTitle("Notifikasi Edit")
            .setContentText("Akun Berhasil diedit")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setColor(Color.BLUE)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.mipmap.ic_launcher, "DONE", actionIntent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque consectetur tincidunt ante. Vivamus eros metus, hendrerit eget leo at, vulputate elementum velit. Phasellus cursus nec eros sit amet vestibulum. Vivamus dui est, pharetra eu efficitur sed, sollicitudin vitae nunc. Pellentesque nec fermentum lectus, in sollicitudin ante. Nullam at dui imperdiet, egestas augue non, efficitur turpis. Fusce ex massa, euismod et convallis pretium, vehicula a justo. Nulla ac sollicitudin urna. Aliquam a imperdiet nunc, eu fringilla odio."))

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }
    }

}
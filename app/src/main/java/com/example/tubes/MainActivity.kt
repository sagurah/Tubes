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
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tubes.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.example.tubes.room.user.User
import com.example.tubes.room.UserDB
import com.example.tubes.room.user.UserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var username : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var mainLayout : ConstraintLayout
    val db by lazy { UserDB(this) }
    var sharedPreferences: SharedPreferences? = null
    var checkLogin : Boolean = false

    private var binding: ActivityMainBinding? = null
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101
    private val CHANNEL_ID_2 = "channel_notification_02"
    private val notificationId2 = 102


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        mainLayout = findViewById(R.id.mainLayout)

        setText()

        val btnRegister : Button = findViewById(R.id.btnRegis)
        btnRegister.setOnClickListener(View.OnClickListener {
            val moveRegistration = Intent(this, RegisterActivity::class.java)
            startActivity(moveRegistration)
        })

        val btnClear : Button = findViewById(R.id.btnClear)
        btnClear.setOnClickListener(View.OnClickListener{
            username = findViewById(R.id.textUsername)
            password = findViewById(R.id.textPassword)

            Snackbar.make(mainLayout, "Text Cleared Successfully", Snackbar.LENGTH_LONG).show()
            username.setText("")
            password.setText("")
        })

        val btnLogin : Button = findViewById(R.id.btnLogin)
        val userDAO = db.UserDAO()

        btnLogin.setOnClickListener(View.OnClickListener{

            username = findViewById(R.id.textUsername)
            password = findViewById(R.id.textPassword)
            val bundle = intent.extras

            var uname = username.text.toString()
            var pass = password.text.toString()

            // Soal nomor 4 (Plis bener)
            CoroutineScope(Dispatchers.IO).launch {
                if(uname.isEmpty())
                    username.setError("Username must be filled with text")
                checkLogin = false

                if(pass.isEmpty())
                    password.setError("Password must be filled with text")
                checkLogin = false

                if (bundle != null) {
                    if(
                        uname == bundle.getString("username")
                        && pass == bundle.getString("password")
                    ) checkLogin = true
                }

                val user = userDAO.getUserByCreds(uname, pass)

                if(user != null){
                    sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                    editor.putString("id", user.id.toString())
                    editor.apply()
                    checkLogin = true
                }
            }


            if(!checkLogin){
                Snackbar.make(mainLayout, "Username atau Password anda tidak sesuai", Snackbar.LENGTH_LONG).show()
                return@OnClickListener
            }
            else{
                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)
            }
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(
                CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

//    private fun sendNotification1() {
//        val intent: Intent = Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent : PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//
//        val broadcastIntent: Intent = Intent(this, NotificationReceiver::class.java)
//        broadcastIntent.putExtra("toastMessage", username.toString())
//        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
//            .setSmallIcon(R.drawable.ic_baseline_looks_one_24)
//            //.setContentTitle(username.editText?.text.toString())
//            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//            .setColor(Color.BLUE)
//            .setAutoCancel(true)
//            .setOnlyAlertOnce(true)
//            .setStyle(NotificationCompat.InboxStyle()
//             tambahin data yang perlu ditampilkan saat register
//             )
//            .setContentIntent(pendingIntent)
//            .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        with(NotificationManagerCompat.from(this)){
//            notify(notificationId1, builder.build())
//        }
//    }

//    private fun sendNotification2() {
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
//            .setSmallIcon(R.drawable.ic_baseline_looks_one_24)
//            .setStyle(NotificationCompat.BigTextStyle()
//                .bigText("Akun dengan username " + username.editText?.text.toString() +
//                " berhasil ditambahkan sebagai pengguna aplikasi Gymmers"))
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//
//        with(NotificationManagerCompat.from(this)){
//            notify(notificationId2, builder.build())
//        }
//    }

    fun setText() {
        username = findViewById(R.id.textUsername)
        password = findViewById(R.id.textPassword)

        val bundle = intent.extras
        if (bundle != null) {
            username.setText(bundle.getString("username"))
        }
    }
}
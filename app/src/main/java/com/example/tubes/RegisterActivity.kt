package com.example.tubes

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.example.tubes.databinding.ActivityMainMenuBinding
import com.example.tubes.databinding.ActivityRegisterBinding
import com.example.tubes.room.UserDB
import com.example.tubes.room.user.User
import com.google.android.material.textfield.TextInputLayout
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    var binding : ActivityRegisterBinding? = null
    val db by lazy { UserDB(this) }
    private val userDAO = db.UserDAO()

    private val CHANNEL_ID_REGISTER = "channel_notification_01"
    private val notificationId = 101
    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val moveLogin = Intent(this, MainActivity::class.java)
//        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnRegister?.setOnClickListener(View.OnClickListener{
            var checkRegistration = false
            val uname: String = binding?.registerLayoutUsername?.getEditText()?.getText().toString()
            val pass: String = binding?.registerLayoutPassword?.getEditText()?.getText().toString()
            val tgl: String = binding?.registerLayoutTanggalLahir?.getEditText()?.getText().toString()
            val email: String = binding?.registerLayoutEmail?.getEditText()?.getText().toString()
            val noTelp: String = binding?.registerLayoutNoTelp?.getEditText()?.getText().toString()

            val bundle = Bundle()

            if(uname.isEmpty()){
                FancyToast.makeText(this, "Username tidak boleh kosong", FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show()
                checkRegistration = false
            }

            if(pass.isEmpty()){
                FancyToast.makeText(this, "Password tidak boleh kosong", FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show()
                checkRegistration = false
            }

            if(tgl.isEmpty()){
                FancyToast.makeText(this, "Tgl Lahir tidak boleh kosong", FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show()
                checkRegistration = false
            }

            if(email.isEmpty() || !EMAIL_ADDRESS_PATTERN.matcher(email).matches()){
                FancyToast.makeText(this, "Email harus sesuai dan tidak boleh kosong", FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show()
                checkRegistration = false
            }

            if(noTelp.isEmpty()){
                FancyToast.makeText(this, "Nomor Telepon tidak boleh kosong", FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show()
                checkRegistration = false
            }

            if(
                !uname.isEmpty()
                && !pass.isEmpty()
                && !tgl.isEmpty()
                && !email.isEmpty()
//                && !EMAIL_ADDRESS_PATTERN.matcher(email).matches()
                && !noTelp.isEmpty()
            ) checkRegistration = true

            if(checkRegistration == true){
                CoroutineScope(Dispatchers.IO).launch {
                    // Store registration data after validation
                    if(checkRegistration == true){
                        val user = User(0, uname, pass, tgl, email, noTelp)
                        userDAO.addUser(user)

                        bundle.putString("username", uname)
                        bundle.putString("password", pass)
                        moveLogin.putExtras(bundle)

//                        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo)
//                        createNotificationChannel()
//                        sendNotification(
//                            binding?.registerLayoutUsername?.getEditText()?.getText().toString(),
//                            Bitmap.createScaledBitmap(bitmap, 300, 100, false)
//                        )
                        startActivity(moveLogin)
                    }
                }
            } else{
                return@OnClickListener
            }
        })
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Register"
            val descTxt = "Notification Description"

            val ch = NotificationChannel(CHANNEL_ID_REGISTER, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descTxt
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(ch)
        }
    }

    private fun sendNotification(user: String, bitmap: Bitmap){
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE)
        val broadcastIntent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", "Akun " + user + " Berhasil dibuat!")
        val actionIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_REGISTER)
            .setSmallIcon(R.drawable.ic_baseline_circle_icon_notifications_24)
            .setContentTitle("Registrasi Sukses")
            .setContentText("Akun " + user + " Berhasil dibuat")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setColor(Color.BLUE)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }
    }
}
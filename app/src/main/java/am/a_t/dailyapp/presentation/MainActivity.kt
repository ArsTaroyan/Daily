package am.a_t.dailyapp.presentation

import am.a_t.dailyapp.R
import am.a_t.dailyapp.extension.convertStringToGson
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

    }

    private fun createNotificationChannel() {
        val name: CharSequence = "Task Alarm"
        val description = "Channel from Alarm Manager"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("Task", name, importance)
        channel.description = description
        val notificationManager = getSystemService(
            NotificationManager::class.java
        )

        notificationManager.createNotificationChannel(channel)
    }
}
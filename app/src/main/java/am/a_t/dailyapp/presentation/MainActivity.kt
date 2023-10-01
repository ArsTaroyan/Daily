package am.a_t.dailyapp.presentation

import am.a_t.dailyapp.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
package am.a_t.dailyapp.presentation

import am.a_t.dailyapp.R
import am.a_t.dailyapp.data.preferences.Preference
import am.a_t.dailyapp.domain.utils.ListType
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val preference: Preference by lazy { Preference(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

    }

    private fun createNotificationChannel() {
        saveType()

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

    private fun saveType() {
        lifecycleScope.launch {
            preference.saveType(Preference.TYPE, ListType.TASKS.typeName)
        }
    }
}
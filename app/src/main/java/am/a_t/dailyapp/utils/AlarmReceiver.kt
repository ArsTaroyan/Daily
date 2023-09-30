package am.a_t.dailyapp.utils

import am.a_t.dailyapp.R
import am.a_t.dailyapp.extension.convertGsonToString
import am.a_t.dailyapp.extension.convertStringToGson
import am.a_t.dailyapp.presentation.MainActivity
import am.a_t.dailyapp.presentation.ui.mainFragment.MainViewModel
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val i = Intent(context, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("cancel", true)
        val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val id = intent.getLongExtra("id", 0)
        val builder = NotificationCompat.Builder(context, "Task").apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(description)
            setAutoCancel(true)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(pendingIntent)
        }


        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(id.toInt(), builder.build())
    }
}

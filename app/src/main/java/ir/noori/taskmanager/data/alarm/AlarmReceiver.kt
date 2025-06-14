package ir.noori.taskmanager.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ir.noori.taskmanager.presentation.notification.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("title") ?: return
        val description = intent.getStringExtra("description") ?: ""
        val taskId = intent.getIntExtra("taskId", -1)

        context?.let {
            NotificationHelper.showNotification(it, title, description, taskId)
        }
    }
}
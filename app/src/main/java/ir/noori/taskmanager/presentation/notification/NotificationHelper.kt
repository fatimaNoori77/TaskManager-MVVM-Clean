package ir.noori.taskmanager.presentation.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import ir.noori.taskmanager.R
import ir.noori.taskmanager.core.constant.AppConstant
import ir.noori.taskmanager.presentation.ui.MainActivity


object NotificationHelper {

    fun showNotification(context: Context, title: String, description: String, taskId: Int) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        Log.i("TAG", "schedule: log3 show notification ")
        val notification = NotificationCompat.Builder(context, AppConstant.NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.launcher)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.launcher))
            .setContentTitle(title)
            .setContentText(description)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(taskId, notification)
    }
}
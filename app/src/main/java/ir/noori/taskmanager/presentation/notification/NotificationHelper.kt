package ir.noori.taskmanager.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import ir.noori.taskmanager.R
import ir.noori.taskmanager.core.constant.AppConstant
import ir.noori.taskmanager.presentation.ui.tasklist.MainActivity
import java.util.Random


object NotificationHelper {

    private const val CHANNEL_ID = AppConstant.NOTIFICATION_CHANNEL

    fun showNotification(context: Context, title: String, description: String, taskId: Int) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                AppConstant.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.launcher)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.launcher))
            .setContentTitle(title)
            .setContentText(description)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(Random().nextInt(), notification)
    }
}
package ir.noori.taskmanager


import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import ir.noori.taskmanager.core.constant.AppConstant

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val manager = getSystemService(NotificationManager::class.java)

            if (manager == null) {
                Log.w("MyApplication", "NotificationManager is null!")
                return
            }

            val channel = NotificationChannel(
                AppConstant.NOTIFICATION_CHANNEL,
                AppConstant.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
                vibrationPattern = longArrayOf(200, 200, 200)
            }
            manager.createNotificationChannel(channel)
        }
    }
}

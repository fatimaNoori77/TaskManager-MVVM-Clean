package ir.noori.taskmanager.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.noori.taskmanager.core.constant.AppConstant
import ir.noori.taskmanager.domain.model.Task
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun schedule(task: Task) {
        if (!task.hasReminder) return

        val reminderTime = task.dueDate
        val adjustedReminderTime = reminderTime - 5 * 60 * 1000 // 5 minutes before

        if (reminderTime < System.currentTimeMillis()) {
            Log.w("AlarmScheduler", "Reminder time is in the past. Skipping.")
            return
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AppConstant.EXTRA_TITLE, task.title)
            putExtra(AppConstant.EXTRA_DESCRIPTION, task.description ?: "")
            putExtra(AppConstant.EXTRA_TASK_ID, task.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val manager = alarmManager
        if (manager == null) {
            Log.w("AlarmScheduler", "AlarmManager is null")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val permission = alarmManager.canScheduleExactAlarms()
            if (!permission) {
                Log.w("AlarmScheduler", "Exact alarms not permitted by user.")
                return
            }
        }


        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            adjustedReminderTime,
            pendingIntent
        )
    }

    fun cancel(task: Task) {
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager?.cancel(pendingIntent)
    }
}
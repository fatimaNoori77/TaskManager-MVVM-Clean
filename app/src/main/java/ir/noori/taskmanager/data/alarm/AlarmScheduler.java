package ir.noori.taskmanager.data.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import ir.noori.taskmanager.domain.model.Task;

public class AlarmScheduler {

    private final Context context;

    public AlarmScheduler(Context context) {
        this.context = context;
    }

    public void schedule(Task task) {
        if (!task.getReminderTime()) return;

        long reminderTime = task.getDueDate();
        long adjustedReminderTime = reminderTime - 5 * 60 * 1000;

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("description", task.getDescription() != null ? task.getDescription() : "");
        intent.putExtra("taskId", task.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                task.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        adjustedReminderTime,
                        pendingIntent
                );
            } else {
                Intent requestPermissionIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                requestPermissionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(requestPermissionIntent);
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    adjustedReminderTime,
                    pendingIntent
            );
        }
    }

    public void cancel(Task task) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                task.getId(),
                intent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}

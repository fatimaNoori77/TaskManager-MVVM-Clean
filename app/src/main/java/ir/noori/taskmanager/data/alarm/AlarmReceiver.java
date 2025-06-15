package ir.noori.taskmanager.data.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ir.noori.taskmanager.presentation.notification.NotificationHelper;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || context == null) return;

        String title = intent.getStringExtra("title");
        if (title == null) return;

        String description = intent.getStringExtra("description");
        if (description == null) description = "";

        int taskId = intent.getIntExtra("taskId", -1);

        NotificationHelper.INSTANCE.showNotification(context, title, description, taskId);
    }
}
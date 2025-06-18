package ir.noori.taskmanager.data.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ir.noori.taskmanager.core.constant.AppConstant;
import ir.noori.taskmanager.presentation.notification.NotificationHelper;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || context == null) return;

        String title = intent.getStringExtra(AppConstant.EXTRA_TITLE);
        if (title == null) return;

        String description = intent.getStringExtra(AppConstant.EXTRA_DESCRIPTION);
        if (description == null) description = "";

        int taskId = intent.getIntExtra(AppConstant.EXTRA_TASK_ID, -1);

        NotificationHelper.INSTANCE.showNotification(context, title, description, taskId);
    }
}
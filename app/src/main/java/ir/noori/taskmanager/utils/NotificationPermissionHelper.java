package ir.noori.taskmanager.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;


public class NotificationPermissionHelper {

    public static final String NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS;

    public static boolean isPermissionRequired() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
    }

    public static boolean isPermissionGranted(Activity activity) {
        if (isPermissionRequired()) {
            return ContextCompat.checkSelfPermission(activity, NOTIFICATION_PERMISSION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public static void requestPermission(
            Activity activity,
            ActivityResultLauncher<String> permissionLauncher
    ) {
        if (isPermissionRequired() && !isPermissionGranted(activity)) {
            permissionLauncher.launch(NOTIFICATION_PERMISSION);
        }
    }
}
package com.bangladeshisoftware.myapplication;

import static androidx.core.content.ContextCompat.getSystemService;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.List;

public class FloatingIconAccessibilityService extends AccessibilityService {

    private FloatingViewService floatingIconService;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        floatingIconService = new FloatingViewService();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Detect when the app is closed or another app comes to the foreground
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (!isAppInForeground()) {
                int[] position = floatingIconService.getLastPosition();
                String message = "Floating Icon Location: X=" + position[0] + ", Y=" + position[1];
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onInterrupt() {}

    private boolean isAppInForeground() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                    processInfo.processName.equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }
}


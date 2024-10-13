package com.bangladeshisoftware.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;

public class MyAccessibilityService extends AccessibilityService {

    private static final String TAG = "MyAccessibilityService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Log the type of event that occurred
        Log.d(TAG, "onAccessiblity");

        String packname= event.getPackageName().toString();
        PackageManager packageManager = this.getPackageManager();

        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packname,0);
            CharSequence charSequence = packageManager.getApplicationLabel(applicationInfo);
            Log.e(TAG,"App name is:"+charSequence);

        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onInterrupt() {
        // Handle cases when the service is interrupted (e.g. low memory)
        Log.d(TAG, "Accessibility service interrupted");
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
                AccessibilityEvent.TYPE_VIEW_FOCUSED;

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;

        Log.d(TAG,"my");


    }
}

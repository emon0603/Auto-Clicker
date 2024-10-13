package com.bangladeshisoftware.myapplication;


import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnAccessibility;
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAccessibility = findViewById(R.id.btn_accessibility);

        btnAccessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });

        //---------------------- Runtime Permission -------------------------------



    } //--- Oncreate Finish

    private void startFloatingViewService() {
        Intent intent = new Intent(MainActivity.this, FloatingViewService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAccessibilityServiceEnabled(MainActivity.this, MyAccessibilityService.class)) {
            btnAccessibility.setText("Disable Accessibility");
            RuntimePermission();
        } else {
            btnAccessibility.setText("Enable Accessibility");
        }
    }


    private boolean isAccessibilityServiceEnabled(MainActivity context, Class<? extends AccessibilityService> service) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo enabledService : enabledServices) {
            if (enabledService.getResolveInfo().serviceInfo.packageName.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    void RuntimePermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                // Start the floating view service if permission is granted
                startFloatingViewService();
            }
        } else {
            // Directly start the floating view service for lower versions
            startFloatingViewService();
        }



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // Permission granted
                    startFloatingViewService();
                } else {
                    // Permission denied
                }
            }
        }
    }





}

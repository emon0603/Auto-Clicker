package com.bangladeshisoftware.myapplication;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class FloatingViewService extends Service {

    private WindowManager windowManager;
    private View floatingView;
    private WindowManager.LayoutParams params;
    private int[] lastPosition = {0, 0};  // Store the last coordinates
    private Handler handler = new Handler();

    // Runnable to simulate auto-click at intervals
    private Runnable autoClickRunnable = new Runnable() {
        @Override
        public void run() {
            // Trigger a click on the floating icon programmatically
            floatingView.performClick();

            // Optionally log the event and show a toast
            Log.d("FloatingIcon", "Auto-click performed at X = " + lastPosition[0] + ", Y = " + lastPosition[1]);
            Toast.makeText(FloatingViewService.this,
                    "Auto-click performed at X=" + lastPosition[0] + ", Y=" + lastPosition[1],
                    Toast.LENGTH_SHORT).show();

            // Repeat this action every 5 seconds (5000 milliseconds)
            handler.postDelayed(this, 5000);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize WindowManager and set up the floating view
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_icon, null);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // Initial position
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 100;
        params.y = 100;

        // Add the floating view to the WindowManager
        windowManager.addView(floatingView, params);

        // Handle drag and drop (same as before)
        floatingView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingView, params);

                        lastPosition[0] = params.x;
                        lastPosition[1] = params.y;

                        return true;
                }
                return false;
            }
        });

        // Set up a click listener for the floating icon
        floatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event (auto-click or manual click)
                Log.d("FloatingIcon", "Floating icon clicked!");
                Toast.makeText(FloatingViewService.this,
                        "Floating icon clicked!",
                        Toast.LENGTH_SHORT).show();

                // Perform any action when the floating icon is clicked
                // Example: launch an activity, open a menu, etc.
            }
        });

        // Start the auto-click simulation
        handler.post(autoClickRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;  // Keep the service running after the app is closed
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) {
            windowManager.removeView(floatingView);
        }

        // Stop the auto-click when the service is destroyed
        handler.removeCallbacks(autoClickRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int[] getLastPosition() {
        return lastPosition;
    }
}

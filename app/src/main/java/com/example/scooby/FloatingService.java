package com.example.scooby;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class FloatingService extends Service {
    private WindowManager wm;
    private LinearLayout ll;
    ImageView close;
    Button emergency;
    View viewRoot;
    EditText task1,task2;
    String[] courses = { "","Morning", "DSA",
            "Friends", "Wasted","Food"
             };
    public FloatingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        wm =(WindowManager ) getSystemService(WINDOW_SERVICE);
        int LAYOUT_FLAG;
//        seeking required permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
//        providing a constant notification while service is running
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("com.example.floatinglayout", "Floating Layout Service", NotificationManager.IMPORTANCE_LOW);
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "com.example.floatinglayout");
            Notification notification = builder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Floating Layout Service is Running")
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2, notification);
        }

        viewRoot = LayoutInflater.from(this).inflate(R.layout.floating_layout, null);
        WindowManager.LayoutParams parameters=new WindowManager.LayoutParams(900, ViewGroup.LayoutParams.WRAP_CONTENT,LAYOUT_FLAG,WindowManager.LayoutParams.FLAG_BLUR_BEHIND, PixelFormat.TRANSLUCENT);
        close = viewRoot.findViewById(R.id.window_close);
        emergency = viewRoot.findViewById(R.id.emergency);
        task1 =viewRoot.findViewById(R.id.task1);
        task2 =viewRoot.findViewById(R.id.task2);

        close.setOnClickListener(view -> stopService());
        emergency.setOnClickListener(view -> stop());

        parameters.x=0;
        parameters.y=0;
//        parameters.alpha = 0.8f;
        parameters.gravity= Gravity.CENTER| Gravity.CENTER;
        wm.addView(viewRoot,parameters);

        Spinner spin1 = viewRoot.findViewById(R.id.spinner1);
        Spinner spin2 = viewRoot.findViewById(R.id.spinner2);
        Spinner spin3 = viewRoot.findViewById(R.id.spinner3);
        Spinner spin4 = viewRoot.findViewById(R.id.spinner4);
//        spin.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                courses);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin1.setAdapter(ad);
        spin2.setAdapter(ad);
        spin3.setAdapter(ad);
        spin4.setAdapter(ad);

    }
    private void stopService() {
//        prevents closing tab until task added
        if(!task1.getText().toString().isEmpty() && !task2.getText().toString().isEmpty()){
            try {
                stopForeground(true);
                stopSelf();
                wm.removeViewImmediate(viewRoot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "Please enter at least 2 task", Toast.LENGTH_SHORT).show();
        }
    }
    private void stop() {
//        prevents closing tab until task added
            try {
                stopForeground(true);
                stopSelf();
                wm.removeViewImmediate(viewRoot);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
}
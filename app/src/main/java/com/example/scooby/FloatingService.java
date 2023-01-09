package com.example.scooby;

import static android.content.ContentValues.TAG;

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
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FloatingService extends Service {
    private WindowManager wm;
    private LinearLayout ll;
    ImageView close;
    Button emergency,submit;
    View viewRoot;
    EditText task1,task2,task3,task4;
    int cnt=0;
    String[] courses = { "","Morning", "DSA",
            "Friends", "Wasted","Food"
             };
//    ..................................................................................
    FirebaseFirestore db = FirebaseFirestore.getInstance();

//    planned structure dates folder -> date -> time slots doc -> (activity ,time ,tag)
//    ..................................................................................
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
        submit = viewRoot.findViewById(R.id.submit);
        task1 =viewRoot.findViewById(R.id.task1);
        task2 =viewRoot.findViewById(R.id.task2);

        close.setOnClickListener(view -> stopService());
        emergency.setOnClickListener(view -> stop());
        submit.setOnClickListener(view -> save());

        parameters.x=0;
        parameters.y=0;
//        parameters.alpha = 0.8f;
        parameters.gravity= Gravity.CENTER| Gravity.CENTER;
        wm.addView(viewRoot,parameters);

        Spinner spin1 = viewRoot.findViewById(R.id.spinner1);
        Spinner spin2 = viewRoot.findViewById(R.id.spinner2);
        Spinner spin3 = viewRoot.findViewById(R.id.spinner3);
        Spinner spin4 = viewRoot.findViewById(R.id.spinner4);

        task1 = viewRoot.findViewById(R.id.task1);
        task2 = viewRoot.findViewById(R.id.task2);
        task3 = viewRoot.findViewById(R.id.task3);
        task4 = viewRoot.findViewById(R.id.task4);
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

    private void save() {
        cnt++;
        Map<String, Object> tasks = new HashMap<>();
        tasks.put("task1", task1.getText().toString());
        tasks.put("task2", task2.getText().toString());
        tasks.put("task3", cnt);


        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);


        db.collection("task").document(strDate)
                .set(tasks)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

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

//firestore documents have limit ,cant have more than 20,000 fields
//firestore retrieves all data from document , can't retrieve partial data fetching irrelevant data will
//drain more battery , wastes data so better to create documents inside documents
//but u are billed according to queries , so if for fetching some data again and again you have to make 3 queries due to hierarchy that's expensive
//queries find documents in a single collection

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FloatingService extends Service {
    private WindowManager wm;
    private LinearLayout ll;
    ImageView close;
    Button emergency,submit,add;
    View viewRoot;
    EditText task1,task2,task3,task4;
    EditText time1,time2,time3,time4;
    Spinner spin1,spin2,spin3,spin4;
    int cnt=0;
    ArrayList<String> courses = new ArrayList<String>();

//    ,"Morning", "DSA",
//            "Friends", "Wasted","Food"
//             };
//    .................................................................................
    ArrayList<task_struc> task_collection=new ArrayList<>();
    RecyclerView recycle;
    task_recycler_adapter adapter;
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
        WindowManager.LayoutParams parameters=new WindowManager.LayoutParams(900, WindowManager.LayoutParams.WRAP_CONTENT,LAYOUT_FLAG,WindowManager.LayoutParams.FLAG_BLUR_BEHIND, PixelFormat.TRANSLUCENT);
        close = viewRoot.findViewById(R.id.window_close);
        emergency = viewRoot.findViewById(R.id.emergency);
        submit = viewRoot.findViewById(R.id.submit);
//        task1 =viewRoot.findViewById(R.id.task1);
//        task2 =viewRoot.findViewById(R.id.task2);

        add=viewRoot.findViewById(R.id.add);
        recycle=viewRoot.findViewById(R.id.recycle);
        recycle.setLayoutManager(new LinearLayoutManager(this));

        courses.add("");
        courses.add("Morning");
        courses.add("DSA");
        courses.add("Friends");
        courses.add("Wasted");
        courses.add("Food");
        task_collection.add(new task_struc("","","0"));


        adapter=new task_recycler_adapter(this, task_collection,courses);
        recycle.setAdapter(adapter);
        close.setOnClickListener(view -> stopService());
        emergency.setOnClickListener(view -> stop());
        submit.setOnClickListener(view -> save());
        add.setOnClickListener(view->addcard());

        parameters.x=0;
        parameters.y=0;
//        parameters.alpha = 0.8f;
        parameters.gravity= Gravity.CENTER| Gravity.CENTER;
        wm.addView(viewRoot,parameters);

//        spin1 = viewRoot.findViewById(R.id.spinner1);
//        spin2 = viewRoot.findViewById(R.id.spinner2);
//        spin3 = viewRoot.findViewById(R.id.spinner3);
//        spin4 = viewRoot.findViewById(R.id.spinner4);

//        task1 = viewRoot.findViewById(R.id.task1);
//        task2 = viewRoot.findViewById(R.id.task2);
//        time1 = viewRoot.findViewById(R.id.time1);
//        spin.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of courses
//        ArrayAdapter ad
//                = new ArrayAdapter(
//                this,
//                android.R.layout.simple_spinner_item,
//                courses);
//
//        // set simple layout resource file
//        // for each item of spinner
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
//        spin1.setAdapter(ad);
//        spin2.setAdapter(ad);
//        spin3.setAdapter(ad);
//        spin4.setAdapter(ad);


    }

    private void addcard() {
//        task_collection.clear();
//        recycle.getLayoutManager().scrollToPosition();
        for (int i = 0; i < recycle.getChildCount(); i++) {
//            recycle.getLayoutManager().scrollToPosition(i);
            task_recycler_adapter.ViewHolder holder = (task_recycler_adapter.ViewHolder) recycle.getChildViewHolder(recycle.getChildAt(i));
            task_collection.get(i).task=holder.task.getText().toString();
            task_collection.get(i).tag=holder.tag.getSelectedItem().toString();
            task_collection.get(i).time=holder.time.getText().toString();
//            task_collection.add(new task_struc(holder.task.getText().toString(),holder.tag.getText().toString(),holder.time.getText().toString()));
        }
        task_collection.add(new task_struc("","","0"));
        adapter.notifyDataSetChanged();
    }

    private void save() {
        cnt++;
//        task_collection.clear();
        for (int i = 0; i < recycle.getChildCount(); i++) {
            task_recycler_adapter.ViewHolder holder = (task_recycler_adapter.ViewHolder) recycle.getChildViewHolder(recycle.getChildAt(i));
            task_collection.get(i).task=holder.task.getText().toString();
            task_collection.get(i).tag=holder.tag.getSelectedItem().toString();
            task_collection.get(i).time=holder.time.getText().toString();
//            task_collection.add(new task_struc(holder.task.getText().toString(),holder.tag.getText().toString(),holder.time.getText().toString()));
        }
        Map<String, Object> dates = new HashMap<>();
//        Map<String, Object> tasks    = new HashMap<>();
//        Map<String, Object> details    = new HashMap<>();

//        details.put("desc", task1.getText().toString());
//        details.put("duration", time1.getText().toString());
//        details.put("tag", spin1.getSelectedItem().toString());
//
//        tasks.put("Task:"+Integer.toString(cnt),details);



        Date date = Calendar.getInstance().getTime();
//        small mm is minutes
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        DateFormat timeFormat = new SimpleDateFormat("hh");
        String strDate = dateFormat.format(date);
        String strTime = timeFormat.format(date);
        int intTime=Integer.parseInt(strTime);
        dates.put("time:"+(intTime-1)+"-"+intTime,task_collection);


        db.collection("task").document(strDate)
                .set(dates, SetOptions.merge())
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
//        if(!task1.getText().toString().isEmpty() && !task2.getText().toString().isEmpty()){
//            try {
                stopForeground(true);
                stopSelf();
                wm.removeViewImmediate(viewRoot);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        else{
//            Toast.makeText(this, "Please enter at least 2 task", Toast.LENGTH_SHORT).show();
//        }
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

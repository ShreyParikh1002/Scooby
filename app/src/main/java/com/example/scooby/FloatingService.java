package com.example.scooby;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FloatingService extends Service {
    private WindowManager wm;
    private LinearLayout ll;
    ImageView close;
    Button emergency,submit,add;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    View viewRoot;
    Vibrator v ;
    int cnt=0;
    ArrayList<String> courses = new ArrayList<String>();
    Date date = Calendar.getInstance().getTime();
    //        small mm is minutes, capital MM is months
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    DateFormat timeFormat = new SimpleDateFormat("kk");
    String strDate = dateFormat.format(date);
    String strTime = timeFormat.format(date);
    int intTime=Integer.parseInt(strTime);

//    .................................................................................
    ArrayList<task_struc> task_collection=new ArrayList<>();
    RecyclerView recycle;
    task_recycler_adapter adapter;
    MediaPlayer mp; //base infrastructure for alarm ringtone still in place, current alarm mode vibration
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
//..................................................................................
//        mp= MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
//        mp.setLooping(true);
//        mp.start();
//..................................................................................
        v=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000};
// First 0 means tart without a delay
// Vibrate for 1000 milliseconds
// Sleep for 1000 milliseconds

        v.vibrate(pattern, -1);

// If '-1' was replaced by '0' here, means to repeat indefinitely
// '0' is actually the index at which the pattern keeps repeating from (the start)
// To repeat the pattern from any other point, you could increase the index, e.g. '1'
// But we don't want to repeat, we need it to stop once this pattern for 15 second is done, so we set repeat as '-1'
//..................................................................................
        wm =(WindowManager ) getSystemService(WINDOW_SERVICE);
        int LAYOUT_FLAG;
//        seeking required permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
//        providing a constant notification while service is running
//        without this notification pop up is force closed by system os
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
        add=viewRoot.findViewById(R.id.add);
        recycle=viewRoot.findViewById(R.id.recycle);
        recycle.setLayoutManager(new LinearLayoutManager(this));
//      populating the drop down
        courses.add("");
        courses.add("DSA");
        courses.add("Class");
        courses.add("Development");
        courses.add("Necessity");
        courses.add("Morning");
        courses.add("Wasted");
        courses.add("Binging");
        courses.add("Enjoyment");
        courses.add("Food");
        courses.add("Studying");
        courses.add("Creative proc");
        courses.add("Family");
        courses.add("Applying/searching");
        courses.add("Friends/room");
        courses.add("Extra Sleep");
        courses.add("Exercise");
//        displaying  blank recycler view card for taking input
        task_collection.add(new task_struc("","","0",""));
        adapter=new task_recycler_adapter(this, task_collection,courses);
        recycle.setAdapter(adapter);
//        disabling the close button to compel user for input
        close.setEnabled(false);
        close.setOnClickListener(view -> stopService());
//        initial plan to provide for 2 emergencies per day, it wil close the pop up no questions asked
        emergency.setOnClickListener(view -> emergency_stop());
        submit.setOnClickListener(view -> save());
        add.setOnClickListener(view->addcard());

        parameters.x=0;
        parameters.y=0;
//        aplha is used to set transparency of pop up window
//        parameters.alpha = 0.8f;
        parameters.gravity= Gravity.CENTER| Gravity.CENTER;
        wm.addView(viewRoot,parameters);
    }

    private void addcard() {
//        recycle.getLayoutManager().scrollToPosition();
        int proceed=1;
        for (int i = 0; i < recycle.getChildCount(); i++) {
//            recycle.getLayoutManager().scrollToPosition(i);
            task_recycler_adapter.ViewHolder holder = (task_recycler_adapter.ViewHolder) recycle.getChildViewHolder(recycle.getChildAt(i));
            String getTask,getTag,getTime;
            getTask = holder.task.getText().toString();;
            getTime=holder.time.getText().toString();
            getTag=holder.tag.getSelectedItem().toString();
            if(getTask=="" || getTime=="" || getTag==""){
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                proceed=0;
                break;
            }
            task_collection.get(i).task=getTask;
            task_collection.get(i).time=getTime;
            task_collection.get(i).tag=getTag;
            task_collection.get(i).hour=Integer.toString(intTime-1);
//            task_collection.add(new task_struc(holder.task.getText().toString(),holder.tag.getText().toString(),holder.time.getText().toString()));
        }
        if(proceed==1) {
            task_collection.add(new task_struc("", "", "0",""));
            adapter.notifyDataSetChanged();
//            stopping the vibrations
            v.cancel();
        }
//        stopping the alarm ringtone
//        mp.stop();
    }

    private void save() {



        cnt++;
        int proceed=1;
//        task_collection.clear();
        for (int i = 0; i < recycle.getChildCount(); i++) {
            task_recycler_adapter.ViewHolder holder = (task_recycler_adapter.ViewHolder) recycle.getChildViewHolder(recycle.getChildAt(i));
            String getTask,getTag,getTime;
            getTask = holder.task.getText().toString();;
            getTime=holder.time.getText().toString();
            getTag=holder.tag.getSelectedItem().toString();
            if(getTask=="" || getTime=="" || getTag==""){
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                proceed=0;
                break;
            }
            task_collection.get(i).task=getTask;
            task_collection.get(i).time=getTime;
            task_collection.get(i).tag=getTag;
            task_collection.get(i).hour=Integer.toString(intTime-1);
//            task_collection.add(new task_struc(holder.task.getText().toString(),holder.tag.getText().toString(),holder.time.getText().toString()));
        }
        if (proceed==1){
            if(intTime<=10){
                strTime="0"+Integer.toString(intTime-1);
            }
//            else if(intTime==10){
//                strTime="time:0"+(intTime-1)+"-"+intTime;
//            }
            else{
                strTime=Integer.toString(intTime-1);
            }
//            dates.put(Integer.toString(intTime),task_collection);
            if(intTime==24){
                int prevdate=(Integer.parseInt(strDate.substring(0,2))-1);
                if(prevdate<10){
                    strDate="0"+prevdate+strDate.substring(2);
                }
                else{
                    strDate=prevdate+strDate.substring(2);
                }
            }
            // TODO: 06-02-2023 integrate this update method as a replacement for adding
//            db.collection("userid").document("date").collection("09-02-2023").document("tasks")
            DocumentReference id= db.collection(user.getUid()).document(strDate);
            id.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.i("TAG", "Document exists!");
                            for (int i = 0; i < task_collection.size(); i++) {
                                id.update("task", FieldValue.arrayUnion(task_collection.get(i)));
                            }
                        } else {
                            Log.i("TAG", "Document does not exist!");
                            ArrayList<task_struc> empty=new ArrayList<>();
                            Map<String, ArrayList<task_struc>> initialise = new HashMap<>();

                            for(int i=8;i<=24;i++){
                                empty.add(new task_struc("", "", "0",Integer.toString(i)));
//                                empty.get(0).hour=Integer.toString(i);
                                String initialStr;
                                if(i<10){
                                    initialStr="0"+Integer.toString(i);
                                }
                                else{
                                    initialStr=Integer.toString(i);
                                }
                            }

                            initialise.put("task",empty);
                            id.set(initialise, SetOptions.merge());

                            for (int i = 0; i < task_collection.size(); i++) {
                                id.update("task", FieldValue.arrayUnion(task_collection.get(i)));
                            }
                        }
                    }
                    else {
                        Log.i("TAG", "Failed with: ", task.getException());
                    }
                }
            });
//            old database design
//            *********************************************************************************************************
//            DocumentReference updateRef=db.collection("task").document(strDate);
//            for (int i = 0; i < task_collection.size(); i++) {
//                updateRef.update(strTime, FieldValue.arrayUnion(task_collection.get(i)));
//            }
//            **************************************************************************************************orignal
//                    id.set(dates, SetOptions.merge())
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d(TAG, "DocumentSnapshot successfully written!");
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w(TAG, "Error writing document", e);
//                        }
//                    });
//            *********************************************************************************************************

//            db.collection("task").document("07-02-2023").get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                Map<String, Object> users = document.getData();
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    users.forEach((k, v) -> Log.i("tag","Key = "
//                                            + k + ", Value = " + v));
//                                }
////                                Log.i("TAG", users.get(0).get(users.get(0).keySet().toArray()[0]));
//
//                            }
//                        }
//                    });
//            for(Object d:dates.values()){
////                                fsdataliist=(ArrayList<task_struc>) d;
//                Log.i("tag",d+"");
//            }
//            *********************************************************************************************************
            v.cancel();
            stopService();}
    }


    private void stopService() {
//        prevents closing tab until task added
//        if(!task1.getText().toString().isEmpty() && !task2.getText().toString().isEmpty()){
            try {
                stopForeground(true);
                stopSelf();
                wm.removeViewImmediate(viewRoot);
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
//        else{
//            Toast.makeText(this, "Please enter at least 2 task", Toast.LENGTH_SHORT).show();
//        }
        v.cancel();
    }
    private void emergency_stop() {
//        prevents closing tab until task added
            try {
                stopForeground(true);
                stopSelf();
                wm.removeViewImmediate(viewRoot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        v.cancel();
    }
}

//firestore documents have limit ,cant have more than 20,000 fields
//firestore retrieves all data from document , can't retrieve partial data fetching irrelevant data will
//drain more battery , wastes data so better to create documents inside documents
//but u are billed according to queries , so if for fetching some data again and again you have to make 3 queries due to hierarchy that's expensive
//queries find documents in a single collection

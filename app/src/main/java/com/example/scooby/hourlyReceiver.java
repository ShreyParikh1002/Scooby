package com.example.scooby;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class hourlyReceiver extends BroadcastReceiver {
//code kept as backup, permission requests already executed in main

//        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == RESULT_OK) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startForegroundService(new Intent(MainActivity.this, FloatingService.class));
//                } else {
//                    startService(new Intent(MainActivity.this, FloatingService.class));
//                }
//            } else {
//                Toast.makeText(this, "Please Grant Overlay Permission", Toast.LENGTH_SHORT).show();
//            }
//        });
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmy=(AlarmManager) context.getSystemService(context.ALARM_SERVICE);

//      System.currentTimeMillis() return time of device in mili second since a reference known as the UNIX epoch: 1970-01-01 00:00:00 UTC
        long triggerTime=System.currentTimeMillis()+(5*60+30)*60*1000;
        if((triggerTime-((triggerTime)%(60*60*1000)))%(24*60*60*1000)==0){
            triggerTime=(triggerTime-((triggerTime)%(60*60*1000)))+(9*60*60*1000)-(5*60+30)*60*1000;
        }
        else{
            triggerTime=(triggerTime-((triggerTime)%(60*60*1000)))+(61*60*1000)-(5*60+30)*60*1000;
//            India is 5 hr 30 mins ahead so subtracted it for IST conversion
//            subtracting the remainder ((triggerTime)%(60*60*1000)) converts to to nearest hour
        }
        Intent broadcast=new Intent(context,hourlyReceiver.class);
        PendingIntent pi=PendingIntent.getBroadcast(context,100,broadcast,PendingIntent.FLAG_MUTABLE);
        alarmy.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerTime,pi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, FloatingService.class));
        } else {
            context.startService(new Intent(context, FloatingService.class));
        }
    }
}

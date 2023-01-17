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

//        if (!Settings.canDrawOverlays(MainActivity.this)) {
//            Intent in = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//            activityResultLauncher.launch(in);
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(new Intent(MainActivity.this, FloatingService.class));
//            } else {
//                startService(new Intent(MainActivity.this, FloatingService.class));
//            }
//        }
        AlarmManager alarmy=(AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        long triggerTime=System.currentTimeMillis();
        triggerTime=(triggerTime-((triggerTime-(5*60+30)*60*1000)%(60*60*1000)))+(61*60*1000);
//        Toast.makeText(this, Long.toString(triggerTime), Toast.LENGTH_SHORT).show();
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

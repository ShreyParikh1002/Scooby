package com.example.scooby;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(MainActivity.this, FloatingService.class));
                } else {
                    startService(new Intent(MainActivity.this, FloatingService.class));
                }
            } else {
                Toast.makeText(this, "Please Grant Overlay Permission", Toast.LENGTH_SHORT).show();
            }
        });
        if (!Settings.canDrawOverlays(MainActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            activityResultLauncher.launch(intent);
        }
//......................................
        AlarmManager alarmy=(AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerTime=System.currentTimeMillis()+(20*1000);
//        Toast.makeText(this, Long.toString(triggerTime), Toast.LENGTH_SHORT).show();
        Intent broadcast=new Intent(MainActivity.this,hourlyReceiver.class);
        PendingIntent pi=PendingIntent.getBroadcast(MainActivity.this,100,broadcast,PendingIntent.FLAG_MUTABLE);
        alarmy.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,triggerTime,pi);
//.................................................................................................
//        Button scoob=(Button) findViewById(R.id.scoob);
//        scoob.setOnClickListener(view -> {
//            if (!Settings.canDrawOverlays(MainActivity.this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//                activityResultLauncher.launch(intent);
//            } else {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startForegroundService(new Intent(MainActivity.this, FloatingService.class));
//                } else {
//                    startService(new Intent(MainActivity.this, FloatingService.class));
//                }
//            }
//        });
    }
}
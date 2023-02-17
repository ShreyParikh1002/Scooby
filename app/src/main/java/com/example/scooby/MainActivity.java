package com.example.scooby;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//........................................................................
//        chipNavigationBar = findViewById(R.id.nav);
//        chipNavigationBar.setItemSelected(R.id.nav,
//                true);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container,
//                        new Home()).commit();
//        bottomMenu();
//........................................................................

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
        long triggerTime=System.currentTimeMillis()+(10*1000);
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
        chipNavigationBar = findViewById(R.id.nav);
        chipNavigationBar.setItemSelected(R.id.nav,
                true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,
                        new Home()).commit();
        bottomMenu();
    }


    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener
                (new ChipNavigationBar.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int i) {
                        Fragment fragment = null;
                        switch (i){
                            case R.id.bottom_nav_home:
                                fragment = new Home();
                                break;
                            case R.id.bottom_nav_analytics:
                                fragment = new Analytics();
                                break;
                            case R.id.bottom_nav_settings:
                                fragment = new AppSettings();
                                break;
                        }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,
                                        fragment).commit();
                    }
                });
    }
}
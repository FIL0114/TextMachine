package com.example.petr.textmachine;

import android.Manifest;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import android.database.sqlite.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int SEND_SMS = 123 ;
    Context mContext;
    ListView listview;
    @Override
    protected void onResume() {
        super.onResume();

        final MyAdapter adapter = new MyAdapter(this,R.layout.row_alarm, MyDb.getInstance(getApplicationContext()).getAllAlarm());
        listview.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this.getApplicationContext();
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i = new Intent(MainActivity.this, NewAlarm.class);
              startActivity(i);
            }
        });

        //dbinit
        String name  = MyDb.getInstance(getApplicationContext()).getDatabaseName();
        //MyDb.getInstance(getApplicationContext()).insertSMS("0","PrvniSMS","+420722059820","Necum","allthetime","true");

       // int rows  = MyDb.getInstance(getApplicationContext()).numberOfRows();


//https://www.tutorialspoint.com/android/android_sqlite_database.htm

        //permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},SEND_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(mContext, "Permission is not granted", Toast.LENGTH_SHORT).show();

        }
        else {
            ///start service
            //loop
            Infinityloop mSensorService = new Infinityloop(mContext);
            Intent mServiceIntent = new Intent(mContext, mSensorService.getClass());
            if (!isMyServiceRunning(mSensorService.getClass())) {
                startService(mServiceIntent);
            }
        }



       //// String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
       //         "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
       //         "Linux" };
       // String tel = "774333444";
       // ArrayList<AlarmData> result = new ArrayList<AlarmData>();

       // for (int i = 0; i < values.length; ++i) {
       //     AlarmData a =new AlarmData(0);
       //     a.description = values[i];
       //     a.tel = tel;
      //      result.add(a);
       // }
      //  result.add( new AlarmData(0));
        final MyAdapter adapter = new MyAdapter(this,R.layout.row_alarm, MyDb.getInstance(getApplicationContext()).getAllAlarm());
        listview.setAdapter(adapter);


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

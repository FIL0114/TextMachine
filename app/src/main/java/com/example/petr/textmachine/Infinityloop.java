package com.example.petr.textmachine;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.Console;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Infinityloop extends Service {
    static Context applicationContext;
    public int counter=0;
    public Infinityloop(Context context) {
        super();
        Log.i("HERE", "here I am!");
        applicationContext  = context;
    }

    public Infinityloop() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        //Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);

       // sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 60 second
        timer.schedule(timerTask, 10000, 60000);
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {

                Log.i("in timer", "in timer ++++  "+ (counter++));
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplication().getApplicationContext(), "in timer: +++ "+counter, Toast.LENGTH_SHORT);
                    }
                });

                final java.util.Calendar c = java.util.Calendar.getInstance();
                int hour = c.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = c.get(java.util.Calendar.MINUTE);
                int dayOfWeek = c.get(java.util.Calendar.DAY_OF_WEEK);
                ArrayList<AlarmData> toExecute = MyDb.getInstance(getApplication().getApplicationContext()).getAlarmsToExecute(hour,minute,dayOfWeek);
                if (toExecute.size()!=0) {
                    for (AlarmData al:toExecute) {
                        Log.i("Textmachne", "tel: "+al.tel+" text: "+al.smsText);
                        sendSMS(al.tel,al.smsText);
                    }

                }
            }
        };
    }
    private void sendSMS(String phoneNumber, String message) {
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();

        PendingIntent sentPI = PendingIntent.getBroadcast(getApplication().getApplicationContext(), 0,
                new Intent(getApplication().getApplicationContext(), SmsSentReceiver.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplication().getApplicationContext(), 0,
                new Intent(getApplication().getApplicationContext(), SmsDeliveredReceiver.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(getBaseContext(), "SMS sending failed...",Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
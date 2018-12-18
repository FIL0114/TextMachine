package com.example.petr.textmachine;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class NewAlarm extends AppCompatActivity {
    DialogFragment newFragment;
    Context mContext;
    ToggleButton poTB;
    ToggleButton utTB;
    ToggleButton stTB;
    ToggleButton ctTB;
    ToggleButton paTB;
    ToggleButton soTB;
    ToggleButton neTB;
    EditText descriptionET;
    EditText telET;
    EditText smsET;
    Button saveBT;
    Button sendNow;
    TextView timeText;
    int editedAlarmId;
    AlarmData editedAlarm;
    Button deleteAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alarm);
        mContext = this.getApplicationContext();
        poTB= (ToggleButton)findViewById(R.id.toggleButton3);
        utTB= (ToggleButton)findViewById(R.id.toggleButton4);
        stTB= (ToggleButton)findViewById(R.id.toggleButton5);
        ctTB= (ToggleButton)findViewById(R.id.toggleButton6);
        paTB= (ToggleButton)findViewById(R.id.toggleButton7);
        soTB= (ToggleButton)findViewById(R.id.toggleButton8);
        neTB= (ToggleButton)findViewById(R.id.toggleButton9);
        descriptionET = (EditText)findViewById(R.id.editText3) ;
        telET = (EditText)findViewById(R.id.editText) ;
        smsET = (EditText)findViewById(R.id.editText2) ;
        saveBT = (Button)findViewById(R.id.button10);
        sendNow = (Button) findViewById(R.id.sendNow);
        timeText = (TextView)findViewById(R.id.TimeText);
        deleteAlarm = (Button)findViewById(R.id.btnDeleteAlarm);
        Intent intent = getIntent();
        editedAlarmId = intent.getIntExtra("currentId", -1);

    if(editedAlarmId >=0)
    {

       editedAlarm = loadAlarm(editedAlarmId);
        deleteAlarm.setVisibility(View.VISIBLE);
    }

    deleteAlarm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyDb.getInstance().deleteAlarm(editedAlarmId);
            Intent i = new Intent(NewAlarm.this, MainActivity.class);
            startActivity(i);
        }
    });

    sendNow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = smsET.getText().toString();
            String tel = telET.getText().toString();

            if(tel.length()<9 || tel.length() >14){

                Toast toast = Toast.makeText(mContext, "spatne cislo", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            sendSMS(tel,text);

        }
    });


        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String description = descriptionET.getText().toString();
             String tel = telET.getText().toString();
             String smsText = smsET.getText().toString();

             if(description.length()<1){

                 Toast toast = Toast.makeText(mContext, "vlozte popis", Toast.LENGTH_SHORT);
                 toast.show();
                 return;
             }

                if(tel.length()<9 || tel.length() >14){

                    Toast toast = Toast.makeText(mContext, "spatne cislo", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                int hour =0;
                int min = 0;
                if(newFragment !=null)
                {
                    hour = ((TimePickerFragment) newFragment).hour;
                    min = ((TimePickerFragment) newFragment).minute;
                }
                if(editedAlarmId >=0)
                {

                    if(newFragment ==null)
                    {
                        hour = editedAlarm.hour;
                        min =  editedAlarm.min;

                    }

                    editedAlarm = new AlarmData(hour,min ,
                            poTB.isChecked(), utTB.isChecked(), stTB.isChecked(), ctTB.isChecked(), paTB.isChecked(), soTB.isChecked(), neTB.isChecked(),
                            smsText, tel, description);
                    editedAlarm.id = editedAlarmId;
                    MyDb.getInstance().updateAlarm(editedAlarm);

                }
                else{

                    AlarmData newAlarmData = new AlarmData(hour, min,
                            poTB.isChecked(), utTB.isChecked(), stTB.isChecked(), ctTB.isChecked(), paTB.isChecked(), soTB.isChecked(), neTB.isChecked(),
                            smsText, tel, description);
                    newAlarmData.saveToDB();
                }


                Intent i = new Intent(NewAlarm.this, MainActivity.class);
                startActivity(i);
            }
        });




        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }


        });


        //((TimePickerFragment)newFragment).hour

    }

    private AlarmData loadAlarm(int editedAlarm) {

        AlarmData data = MyDb.getInstance().getAlarm(editedAlarm);

        poTB.setChecked(data.monday);
        utTB.setChecked(data.tuesday);
        stTB.setChecked(data.wednesday);
        ctTB.setChecked(data.thursday);
        paTB.setChecked(data.friday);
        soTB.setChecked(data.saturday);
        neTB.setChecked(data.sunday);
        descriptionET.setText(data.description);
        telET.setText(data.tel);
        smsET.setText(data.smsText);
        saveBT.setText("Uprav");
        timeText.setText(data.hour+":"+data.min);


        return data;

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
}

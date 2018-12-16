package com.example.petr.textmachine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

public class MyAdapter extends ArrayAdapter<AlarmData> {

      List<AlarmData> data;
    Context context;
    int layoutResourceId;
    MyAdapter i ;
    public MyAdapter(Context context, int layoutResourceId,
            List<AlarmData> data) {

            super(context, layoutResourceId, data);
            this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        i = this;
        }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        View row = convertView;
        EntryHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new EntryHolder();

            holder.txtName = (TextView)row.findViewById(R.id.txtName);
            holder.txtTel = (TextView)row.findViewById(R.id.txtTel);
            holder.enabled = (Switch)row.findViewById(R.id.switch1);
            holder.time = (TextView)row.findViewById(R.id.textView);
            holder.dates = (TextView)row.findViewById(R.id.txtDays);



            final AlarmData entry = data.get(position);


        }
        else
        {
            holder = (EntryHolder)row.getTag();
        }

        final AlarmData entry = data.get(position);
        holder.txtName.setText(entry.description );
        holder.txtTel.setText(entry.tel);
        String str = entry.monday?"Po ":"";
        str +=entry.tuesday?"Ut ":"";
        str +=entry.wednesday?"St ":"";
        str +=entry.thursday?"Ct ":"";
        str +=entry.friday?"Pa ":"";
        str +=entry.saturday?"So ":"";
        str +=entry.sunday?"Ne ":"";

        holder.dates.setText( str);
        String h = String.format("%02d" , entry.hour);
        String m = String.format("%02d" , entry.min);
        holder.time.setText(h +":"+m);
        holder.enabled.setChecked(entry.enable);

        row.setTag(holder);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, NewAlarm.class);
                i.putExtra("currentId",entry.id);
                context.startActivity(i);
            }
        });

    holder.enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //entry.id

            MyDb.getInstance(context).updateAlarmEnabled(entry.id,isChecked);
            data = MyDb.getInstance(context).getAllAlarm();
            i.notifyDataSetChanged();
        }
    });

        return row;
    }
    static class EntryHolder
    {

        TextView txtTel;
        TextView txtName;
        TextView dates;
        Switch enabled;
        TextView time;
       // ImageView imgView;
    }

    }


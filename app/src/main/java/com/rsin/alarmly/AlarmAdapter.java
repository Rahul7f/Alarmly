package com.rsin.alarmly;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.rsin.alarmly.Room.Alarm;
import com.rsin.alarmly.Room.MyDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    Context context;
    List<Alarm> alarmList;

    public AlarmAdapter(Context context, List<Alarm> alarmList) {
        this.context = context;
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public AlarmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.alarm_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmAdapter.ViewHolder holder, int position) {
        holder.title.setText(alarmList.get(position).getTitle());
        holder.time.setText(alarmList.get(position).getTime());

        if (alarmList.get(position).getStatus().equals("on"))
        {
            holder.switchMaterial.setChecked(true);
        }
        else {
            holder.switchMaterial.setChecked(false);
        }

        holder.delete_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm(alarmList.get(position));

                MyDatabase myDatabase = Room.databaseBuilder(context,MyDatabase.class,"AlarmDB")
                        .allowMainThreadQueries()
                        .build();
                myDatabase.dao().alarmDelete(alarmList.get(position));
                alarmList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    startAlarm(alarmList.get(position));
                    Toast.makeText(context, "on", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "alaram off", Toast.LENGTH_SHORT).show();
                    cancelAlarm(alarmList.get(position));
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,time;
        SwitchMaterial switchMaterial;
        ImageView delete_alarm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.alarm_title);
            time = itemView.findViewById(R.id.alarm_time);
            switchMaterial = itemView.findViewById(R.id.alarm_switch);
            delete_alarm = itemView.findViewById(R.id.delete_alarm);
        }
    }

    private void cancelAlarm(Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlaramReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getAlarm_id().intValue(), intent, 0);
        alarmManager.cancel(pendingIntent);
        updateAlarm("off",alarm);

    }

    private void startAlarm(Alarm alarm) {

        Calendar c = Calendar.getInstance();
        Date date = new Date(alarm.getAlarm_time());
        c.setTime(date);
        // set new alarm
        Calendar newC = Calendar.getInstance();
        newC.set(c.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY) );
        newC.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
        newC.set(Calendar.SECOND,0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlaramReceiver.class);
        Bundle args = new Bundle();
        args.putSerializable("alarm",(Serializable)alarm);
        intent.putExtra("DATA",args);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getAlarm_id().intValue(), intent, 0);

        if (System.currentTimeMillis()>newC.getTimeInMillis())
        {
            newC.add(Calendar.DATE,1);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, newC.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        else {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, newC.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        updateAlarm2("on",alarm,newC);
    }

    private void updateAlarm2(String status, Alarm alarm,Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String timeis = sdf.format(calendar.getTime());
        MyDatabase myDatabase = Room.databaseBuilder(context,MyDatabase.class,"AlarmDB")
                .allowMainThreadQueries()
                .build();
        Alarm updatedAlarm = new Alarm(alarm.getAlarm_id(),alarm.getTitle(),timeis,status,calendar.getTimeInMillis());
        myDatabase.dao().alarmUpdate(updatedAlarm);
    }

    private void updateAlarm(String status,Alarm alarm)
    {
        MyDatabase myDatabase = Room.databaseBuilder(context,MyDatabase.class,"AlarmDB")
                .allowMainThreadQueries()
                .build();
        Alarm updatedAlarm = new Alarm(alarm.getAlarm_id(),alarm.getTitle(),alarm.getTime(),status,alarm.getAlarm_time());
        myDatabase.dao().alarmUpdate(updatedAlarm);
    }
}

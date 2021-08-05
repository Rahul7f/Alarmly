package com.rsin.alarmly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rsin.alarmly.Room.Alarm;
import com.rsin.alarmly.Room.MyDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class SetAlarmActivity extends AppCompatActivity {
    TimePicker timePicker;
    EditText title;
    Button setAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        timePicker = findViewById(R.id.time_picker);
        title = findViewById(R.id.set_title);
        setAlarm = findViewById(R.id.set_btn);
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                calendar.set(Calendar.SECOND,0);
                String timeis = sdf.format(calendar.getTime());

                Long newID = System.currentTimeMillis();

                Log.d("rsin",timeis);
                Log.d("rsin",title.getText().toString());
                Log.d("rsin",newID+"");
                

                if (title.getText().toString().isEmpty())
                {
                    Toast.makeText(SetAlarmActivity.this, "enter title", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (System.currentTimeMillis()>calendar.getTimeInMillis())
                    {
                        calendar.add(Calendar.DATE,1);
                        Toast.makeText(SetAlarmActivity.this, "time added", Toast.LENGTH_SHORT).show();
                        MyDatabase myDatabase = Room.databaseBuilder(getApplicationContext(),MyDatabase.class,"AlarmDB")
                                .allowMainThreadQueries()
                                .build();

                        Alarm alarm = new Alarm(newID,title.getText().toString(),timeis,"on",calendar.getTimeInMillis());
                        myDatabase.dao().alarmInsertion(alarm);
                        startAlarm(calendar,newID,alarm);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);// New activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        MyDatabase myDatabase = Room.databaseBuilder(getApplicationContext(),MyDatabase.class,"AlarmDB")
                                .allowMainThreadQueries()
                                .build();

                        Alarm alarm = new Alarm(newID,title.getText().toString(),timeis,"on",calendar.getTimeInMillis());
                        myDatabase.dao().alarmInsertion(alarm);
                        startAlarm(calendar,newID,alarm);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);// New activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }

                }

            }
        });
    }

    private void startAlarm(Calendar c ,Long id,Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlaramReceiver.class);
        Bundle args = new Bundle();
        args.putSerializable("alarm",(Serializable)alarm);
        intent.putExtra("DATA",args);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id.intValue(), intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
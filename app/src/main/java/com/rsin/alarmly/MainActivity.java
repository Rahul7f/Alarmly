package com.rsin.alarmly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rsin.alarmly.Room.Alarm;
import com.rsin.alarmly.Room.MyDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_alarm_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.alarm_recycle_view);
        add_alarm_btn = findViewById(R.id.add_alarm_btn);

        MyDatabase myDatabase = Room.databaseBuilder(getApplicationContext(),MyDatabase.class,"AlarmDB")
                .allowMainThreadQueries()
                .build();
        List<Alarm> alarmList =  myDatabase.dao().getAlarm();

        if (alarmList.isEmpty())
        {
            Toast.makeText(this, "no data yet", Toast.LENGTH_SHORT).show();
        }
        else {
            AlarmAdapter alarmAdapter = new AlarmAdapter(getApplicationContext(),alarmList);
            recyclerView.setAdapter(alarmAdapter);
        }

        add_alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SetAlarmActivity.class));
            }
        });
    }

}
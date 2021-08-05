package com.rsin.alarmly.Room;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Alarm.class},version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract MyDao dao();
}

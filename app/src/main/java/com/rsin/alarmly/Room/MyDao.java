package com.rsin.alarmly.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDao {
    @Insert
    public void alarmInsertion(Alarm alarm);

    @Delete
    public void alarmDelete(Alarm alarm);

    @Update
    public void alarmUpdate(Alarm alarm);

    @Query("Select * from Alarm")
    public List<Alarm> getAlarm();

}

package com.rsin.alarmly.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Alarm implements Serializable{
    @PrimaryKey
    @ColumnInfo(name = "alarm_id")
    Long alarm_id;
    @ColumnInfo(name = "title")
    String title;
    @ColumnInfo(name = "time")
    String time;
    @ColumnInfo(name = "status")
    String status;
    @ColumnInfo(name = "alarm_time")
    Long alarm_time;

    public Alarm(Long alarm_id, String title, String time, String status, Long alarm_time) {
        this.alarm_id = alarm_id;
        this.title = title;
        this.time = time;
        this.status = status;
        this.alarm_time = alarm_time;
    }

    public void setAlarm_id(Long alarm_id) {
        this.alarm_id = alarm_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAlarm_time(Long alarm_time) {
        this.alarm_time = alarm_time;
    }

    public Long getAlarm_id() {
        return alarm_id;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public Long getAlarm_time() {
        return alarm_time;
    }
}

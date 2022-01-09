package com.example.eminz.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    public long scheduleId;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "recipients")
    public String recipients;

    @ColumnInfo(name = "body")
    public String body;

    @ColumnInfo(name = "schedule_at")
    public long date;
    @ColumnInfo(name = "time")
    public long time;

    @ColumnInfo(name = "repeat_every")
    public int repeatEvery;

    @ColumnInfo(name = "repeat_unit")
    public String repeatUnit;

    @ColumnInfo(name = "repeat_count")
    public int repeatCount;

    @ColumnInfo(name = "remaining_repeat")
    public int remainingRepeat;

    @ColumnInfo(name = "status")
    public String status;

    public Schedule(long scheduleId, String type, String recipients, String body, long date, long time, int repeatEvery, String repeatUnit, int repeatCount, int remainingRepeat, String status) {
        this.scheduleId = scheduleId;
        this.type = type;
        this.recipients = recipients;
        this.body = body;
        this.date = date;
        this.repeatEvery = repeatEvery;
        this.repeatUnit = repeatUnit;
        this.repeatCount = repeatCount;
        this.remainingRepeat = remainingRepeat;
        this.status = status;
        this.time = time;
    }
}

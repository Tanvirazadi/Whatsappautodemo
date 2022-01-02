package com.example.eminz.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    public int scheduleId;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "recipients")
    public String recipients;

    @ColumnInfo(name = "body")
    public String body;

    @ColumnInfo(name = "schedule_at")
    public long scheduleAt;

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

    public Schedule(int scheduleId, String type, String recipients, String body, long scheduleAt, int repeatEvery, String repeatUnit, int repeatCount, int remainingRepeat, String status) {
        this.scheduleId = scheduleId;
        this.type = type;
        this.recipients = recipients;
        this.body = body;
        this.scheduleAt = scheduleAt;
        this.repeatEvery = repeatEvery;
        this.repeatUnit = repeatUnit;
        this.repeatCount = repeatCount;
        this.remainingRepeat = remainingRepeat;
        this.status = status;
    }

    @Ignore
    public Schedule(String type, String recipients, String body, long scheduleAt, int repeatEvery, String repeatUnit, int repeatCount, int remainingRepeat, String status) {
        this.type = type;
        this.recipients = recipients;
        this.body = body;
        this.scheduleAt = scheduleAt;
        this.repeatEvery = repeatEvery;
        this.repeatUnit = repeatUnit;
        this.repeatCount = repeatCount;
        this.remainingRepeat = remainingRepeat;
        this.status = status;
    }
}

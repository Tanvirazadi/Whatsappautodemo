package com.example.eminz.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.eminz.database.daos.ScheduleDao;
import com.example.eminz.database.entities.Schedule;

@Database(entities = {Schedule.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScheduleDao scheduleDaoDao();


}

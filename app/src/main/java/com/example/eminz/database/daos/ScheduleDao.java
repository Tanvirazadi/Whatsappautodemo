package com.example.eminz.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.eminz.database.entities.Schedule;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    List<Schedule> getAll();

    @Query("SELECT * FROM schedule WHERE scheduleId IN (:scheduleIds)")
    List<Schedule> loadAllByIds(int... scheduleIds);

    @Query("SELECT * FROM schedule WHERE type = :type")
    List<Schedule> findByType(String type);

    @Query("SELECT * FROM schedule WHERE status = :status")
    List<Schedule> findByStatus(String status);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(Schedule... data);

    @Update
    Integer update(Schedule... users);

    @Delete
    Integer delete(Schedule data);
}
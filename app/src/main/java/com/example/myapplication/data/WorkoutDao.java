package com.example.myapplication.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.myapplication.models.Workout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface WorkoutDao {
    @Query("SELECT * FROM Workout")
    List<Workout> getAll();

    @Query("SELECT * FROM Workout")
    LiveData<List<Workout>> getAllLiveData();

    @Query("SELECT * FROM Workout WHERE ID IN (:userIds)")
    List<Workout> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Workout WHERE ID = :ID LIMIT 1")
    Workout findById(int ID);

    //
    @Query("SELECT * FROM Workout WHERE MONTH = :month and YEAR = :year")
    List<Workout> CollectEventsPerMonth(String month, String year);

    @Query("SELECT * FROM Workout WHERE DATE = :date")
    List<Workout> CollectEvents(String date);

    @Query("SELECT * FROM Workout WHERE DATE = :date")
    LiveData<List<Workout>> getLiveDataForDate(String date);
    //

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Workout workout);

    @Update
    void update(Workout workout);

    @Delete
    void delete(Workout workout);
}

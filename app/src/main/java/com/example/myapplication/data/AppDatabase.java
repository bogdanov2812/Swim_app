package com.example.myapplication.data;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myapplication.models.Result;
import com.example.myapplication.models.Workout;

@Database(entities = {Result.class,Workout.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ResultDao resultDao();
    public abstract WorkoutDao workoutDao();
}

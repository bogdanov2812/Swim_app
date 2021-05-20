package com.example.myapplication;

import android.app.Application;

import androidx.room.Room;

import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.ResultDao;
import com.example.myapplication.data.WorkoutDao;

public class App extends Application {

    private AppDatabase database;
    private ResultDao resultDao;

    private static App instance;

    //делаю календарь
    private WorkoutDao workoutDao;

    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance=this;

        database = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "app-db-name")
                .allowMainThreadQueries()
                .build();

        resultDao = database.resultDao();
        workoutDao = database.workoutDao();

    }

    public AppDatabase getDatabase() {
        return database;
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    public ResultDao getResultDao() {
        return resultDao;
    }

    public void setResultDao(ResultDao resultDao) {
        this.resultDao = resultDao;
    }

    //календарь
    public WorkoutDao getWorkoutDao() {
        return workoutDao;
    }

    public void setWorkoutDao(WorkoutDao workoutDao){
        this.workoutDao = workoutDao;
    }
}

package com.example.myapplication.calendar;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.App;
import com.example.myapplication.models.Workout;

import java.util.List;

public class WorkoutViewModel extends ViewModel {

    public LiveData<List<Workout>> getWorkoutLiveData(String date){
        return App.getInstance().getWorkoutDao().getLiveDataForDate(date);
    }
}

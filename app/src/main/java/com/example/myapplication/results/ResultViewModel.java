package com.example.myapplication.results;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.App;
import com.example.myapplication.models.Result;

import java.util.List;

public class ResultViewModel extends ViewModel {
    private final LiveData<List<Result>> resultLiveData = App.getInstance().getResultDao().getAllLiveData();

    public LiveData<List<Result>> getResultLiveData(){
        return resultLiveData;
    }

}

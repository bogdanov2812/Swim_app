package com.example.myapplication.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.models.Result;

import java.util.List;

@Dao
public interface ResultDao {
    @Query("SELECT * FROM Result")
    List<Result> getAll();

    @Query("SELECT * FROM Result")
    LiveData<List<Result>> getAllLiveData();

    @Query("SELECT * FROM Result WHERE uid IN (:userIds)")
    List<Result> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Result WHERE uid = :uid LIMIT 1")
    Result findById(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Result result);

    @Update
    void update(Result result);

    @Delete
    void delete(Result result);
}

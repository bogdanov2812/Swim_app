package com.example.myapplication.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    private final static String CREATE_WORKOUTS_TABLE = "CREATE TABLE " + DBStructure.WORKOUT_TABLE_NAME+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            +DBStructure.PROGRAM+" TEXT, "+DBStructure.SIZE +" TEXT, "+DBStructure.TIME+" TEXT, "+DBStructure.DATE+" TEXT, "+DBStructure.MONTH+" TEXT, "
            +DBStructure.YEAR+" TEXT)";
    private static final String DROP_EVENTS_TABLE= "DROP TABLE IF EXISTS "+DBStructure.WORKOUT_TABLE_NAME;

    public DBOpenHelper(@Nullable Context context) {
        super(context, DBStructure.DB_NAME, null, DBStructure.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORKOUTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENTS_TABLE);
        onCreate(db);
    }

    public void SaveWorkout(String program, String size , String time, String date, String month, String year, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.PROGRAM,program);
        contentValues.put(DBStructure.SIZE,size);
        contentValues.put(DBStructure.TIME,time);
        contentValues.put(DBStructure.DATE,date);
        contentValues.put(DBStructure.MONTH,month);
        contentValues.put(DBStructure.YEAR,year);
        database.insert(DBStructure.WORKOUT_TABLE_NAME,null,contentValues);
    }

    public Cursor ReadWorkouts(String date, SQLiteDatabase database){
        String [] Projections = {DBStructure.PROGRAM,DBStructure.SIZE, DBStructure.TIME,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR};
        String Selection = DBStructure.DATE +"=?";
        String [] SelectionArgs = {date};

        return database.query(DBStructure.WORKOUT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);
    }

    public Cursor ReadWorkoutsperMonth(String month, String year, SQLiteDatabase database){
        String [] Projections = {DBStructure.PROGRAM,DBStructure.SIZE ,DBStructure.TIME,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR};
        String Selection = DBStructure.MONTH +"=? and "+DBStructure.YEAR+"=?";
        String [] SelectionArgs = {month,year};
        return database.query(DBStructure.WORKOUT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);
    }

    public void deleteWorkout(String program, String size, String time, SQLiteDatabase database){
        String selection = DBStructure.PROGRAM+"=? and "+DBStructure.SIZE+"=? and "+DBStructure.TIME+"=?";
        String[] selectionArg = {program,size,time};
        database.delete(DBStructure.WORKOUT_TABLE_NAME,selection,selectionArg);
    }
}

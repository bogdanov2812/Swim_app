package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Workout implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int ID;

    @ColumnInfo(name = "TIME")
    public String TIME;

    @ColumnInfo(name = "PROGRAM")
    public String PROGRAM;

    @ColumnInfo(name = "SIZE")
    public String SIZE;

    @ColumnInfo(name = "TARGET")
    public String TARGET;

    @ColumnInfo(name = "PHOTO")
    public String PHOTO;

    @ColumnInfo(name = "DATE")
    public String DATE;

    @ColumnInfo(name = "MONTH")
    public String MONTH;

    @ColumnInfo(name = "YEAR")
    public String YEAR;


    public Workout(){

    }

    protected Workout(Parcel in) {
        ID = in.readInt();
        TIME = in.readString();
        PROGRAM = in.readString();
        SIZE = in.readString();
        TARGET = in.readString();
        PHOTO = in.readString();
        DATE = in.readString();
        MONTH = in.readString();
        YEAR = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workout)) return false;
        Workout workout = (Workout) o;
        return ID == workout.ID &&
                TIME.equals(workout.TIME) &&
                PROGRAM.equals(workout.PROGRAM) &&
                SIZE.equals(workout.SIZE) &&
                TARGET.equals(workout.TARGET) &&
                Objects.equals(PHOTO, workout.PHOTO) &&
                DATE.equals(workout.DATE) &&
                MONTH.equals(workout.MONTH) &&
                YEAR.equals(workout.YEAR);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, TIME, PROGRAM, SIZE, TARGET, PHOTO, DATE, MONTH, YEAR);
    }

    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(TIME);
        dest.writeString(PROGRAM);
        dest.writeString(SIZE);
        dest.writeString(TARGET);
        dest.writeString(PHOTO);
        dest.writeString(DATE);
        dest.writeString(MONTH);
        dest.writeString(YEAR);
    }
}



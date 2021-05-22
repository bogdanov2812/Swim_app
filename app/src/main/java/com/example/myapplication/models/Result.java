package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Result implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "distance")
    public String distance;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "data")
    public String date;

    @ColumnInfo(name = "curse")
    public String curse;

    @ColumnInfo(name = "rank")
    public String rank;

    @ColumnInfo(name = "points")
    public String points;

    public Result() {
    }

    protected Result(Parcel in) {
        uid = in.readInt();
        distance = in.readString();
        time = in.readString();
        date = in.readString();
        curse = in.readString();
        rank = in.readString();
        points = in.readString();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result)) return false;
        Result result = (Result) o;
        return uid == result.uid &&
                distance.equals(result.distance) &&
                time.equals(result.time) &&
                date.equals(result.date) &&
                curse.equals(result.curse) &&
                Objects.equals(rank, result.rank) &&
                Objects.equals(points, result.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, distance, time, date, curse, rank, points);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(distance);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(curse);
        dest.writeString(rank);
        dest.writeString(points);
    }


}

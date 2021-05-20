package com.example.myapplication.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.models.Workout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GridAdapter extends ArrayAdapter {



    List<Date> dates ;
    Calendar currentDate;
    LayoutInflater inflater;
    List<Workout> workoutList;

    public GridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Workout> workoutsList) {
        super(context, R.layout.single_cell);
        this.dates = dates;
        this.currentDate = currentDate;
        inflater = LayoutInflater.from(context);
        this.workoutList = workoutsList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        //
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);

        String current_date = dateFormat.format(dateCalendar.getTime());
        System.out.println(dateFormat.format(dateCalendar.getTime()));
        System.out.println(dateCalendar.getTime());
        //
        dateCalendar.setTime(monthDate);
        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        String display_date = dateFormat.format(dateCalendar.getTime());

        //



        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.single_cell,parent,false);
        }

        if (displayMonth == currentMonth && displayYear==currentYear){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.current_month));
            //
            if(display_date.equals(current_date)){
                view.setBackgroundColor((getContext().getResources().getColor(R.color.current_day)));
            }

            //
        }
        else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }


        TextView cellNumber = view.findViewById(R.id.calendar_day);
        TextView WorkoutNumber = view.findViewById(R.id.workouts_id);
        cellNumber.setText(String.valueOf(dayNo));
        Calendar workoutCalendar = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < workoutList.size(); i++){
            workoutCalendar.setTime(convertStringToDate(workoutList.get(i).DATE));
            if(dayNo == workoutCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == workoutCalendar.get(Calendar.MONTH)+1
                    && displayYear == workoutCalendar.get(Calendar.YEAR)){
                arrayList.add(workoutList.get(i).PROGRAM);
                WorkoutNumber.setText(arrayList.size()+" трен");

            }
        }



        return view;
    }


    @Override
    public int getCount() {
        return dates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }


    private Date convertStringToDate(String dateInString){
        java.text.SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateInString);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}


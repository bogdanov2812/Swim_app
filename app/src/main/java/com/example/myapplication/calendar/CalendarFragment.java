package com.example.myapplication.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.App;
import com.example.myapplication.R;
import com.example.myapplication.models.Workout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {


    ImageButton PreviouseButton,NextButton;
    TextView CurrentDate;
    GridView gridView;
    private static final int MAX_CALENDAR_Days = 42;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLLL yyyy", new Locale("ru"));
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM",Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy",Locale.ENGLISH);
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;

    List<Workout> workoutList = new ArrayList<>();
    List<Date> dateList = new ArrayList<>();

    DBOpenHelper dbOpenHelper;


    AlertDialog add_event_dialog;

    GridAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);

        PreviouseButton = view.findViewById(R.id.previousBtn);
        NextButton = view.findViewById(R.id.nextBtn);
        CurrentDate = view.findViewById(R.id.current_Date);
        gridView = view.findViewById(R.id.gridview);

        SetupCalendar();

        PreviouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,-1);
                SetupCalendar();

            }
        });

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,1);
                SetupCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String date = dateFormat.format(dateList.get(position));

                AlertDialog.Builder builder =new AlertDialog.Builder(CalendarFragment.this.getActivity());
                builder.setCancelable(true);

                View addEvent = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_event, null);
                Button AddWorkout = addEvent.findViewById(R.id.add_workout);

                RecyclerView workouts_list= addEvent.findViewById(R.id.workouts_list);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(addEvent.getContext());
                workouts_list.setLayoutManager(layoutManager);
//                workouts_list.setHasFixedSize(true);

                workouts_list.addItemDecoration(new DividerItemDecoration(addEvent.getContext(),DividerItemDecoration.VERTICAL));

                RecyclerAdapter workoutRecyclerAdapter = new RecyclerAdapter(addEvent.getContext());

                workouts_list.setAdapter(workoutRecyclerAdapter);


                //какие-то модели
                WorkoutViewModel workoutViewModel = ViewModelProviders.of(CalendarFragment.this).get(WorkoutViewModel.class);
                workoutViewModel.getWorkoutLiveData(date).observe(getViewLifecycleOwner(), new Observer<List<Workout>>() {
                    @Override
                    public void onChanged(List<Workout> workouts) {
                        workoutRecyclerAdapter.setItemsNew(workouts);
                    }
                });
                //

                AddWorkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WorkoutDetailActivity.start(CalendarFragment.this.getActivity(),null,date,monthFormat.format(dateList.get(position)),yearFormat.format(dateList.get(position)));
                        SetupCalendar();

                    }
                });

                //Отсчет дней


                //

                builder.setView(addEvent);
                add_event_dialog = builder.create();
                add_event_dialog.show();
                add_event_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        SetupCalendar();
                    }
                });
            }
        });


        return view;
    }

    private ArrayList<Workout> CollectWorkout(String date){
        List<Workout> arrayList = new ArrayList<>();

        arrayList = App.getInstance().getWorkoutDao().CollectEvents(date);

        return (ArrayList<Workout>) arrayList;
    }

    private void SaveWorkout(String program, String size, String time, String date, String month, String year){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveWorkout(program,size,time,date,month,year,database);
        dbOpenHelper.close();
        Toast.makeText(context, "Тренировка сохранена", Toast.LENGTH_LONG).show();
    }

    private void SetupCalendar(){
        String StartDate = simpleDateFormat.format(calendar.getTime());
        CurrentDate.setText(StartDate);
        dateList.clear();
        Calendar monthCalendar = (Calendar)calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-2;
        monthCalendar.add(Calendar.DAY_OF_MONTH,-FirstDayOfMonth);


        COllectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormat.format(calendar.getTime()));


        while (dateList.size() < MAX_CALENDAR_Days){
            dateList.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);

        }

        //Отсчет до ближайших соревнований
        String now = dateFormat.format(calendar.getTime());

        //
        adapter = new GridAdapter(this.requireActivity(),dateList,calendar,workoutList);
        gridView.setAdapter(adapter);


    }

    private void COllectEventsPerMonth(String Month,String Year){
        workoutList.clear();

        workoutList = App.getInstance().getWorkoutDao().CollectEventsPerMonth(Month, Year);
        System.out.println(workoutList);

    }
}
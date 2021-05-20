//package com.example.myapplication.calendar;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.myapplication.R;
//import com.example.myapplication.models.Workout;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//public class CustomCalendarView extends LinearLayout {
//    ImageButton PreviouseButton,NextButton;
//    TextView CurrentDate;
//    GridView gridView;
//    private static final int MAX_CALENDAR_Days = 42;
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
//    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM",Locale.ENGLISH);
//    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy",Locale.ENGLISH);
//    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
//    Context context;
//
//    List<Workout> workoutList = new ArrayList<>();
//    List<Date> dateList = new ArrayList<>();
//
//    DBOpenHelper dbOpenHelper;
//
//    AlertDialog alertDialog;
//    AlertDialog add_event_dialog, add_workout_dialog;
//
//    GridAdapter adapter;
//
//
//
//    public CustomCalendarView(Context context) {
//        super(context);
//    }
//
//    public CustomCalendarView(final Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        this.context=context;
//        IntializeLayout();
//        SetupCalendar();
//        PreviouseButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                calendar.add(Calendar.MONTH,-1);
//                SetupCalendar();
//
//            }
//        });
//
//        NextButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                calendar.add(Calendar.MONTH,1);
//                SetupCalendar();
//            }
//        });
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                AlertDialog.Builder builder1 =new AlertDialog.Builder(context);
//                builder1.setCancelable(true);
//
//
//                //мой код
//                View addEvent = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_event, null);
//                Button AddWorkout1 = addEvent.findViewById(R.id.add_workout);
//                Button AddCompetition = addEvent.findViewById(R.id.add_competition);
//
//                //Тут еще будет мой код
//                final String date = dateFormat.format(dateList.get(position));
//
//                RecyclerView workouts_list= (RecyclerView) addEvent.findViewById(R.id.workouts_list);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(addEvent.getContext());
//                workouts_list.setLayoutManager(layoutManager);
//                workouts_list.setHasFixedSize(true);
//                RecyclerAdapter workoutRecyclerAdapter = new RecyclerAdapter(addEvent.getContext()
//                        ,CollectWorkout(date));
//                workouts_list.setAdapter(workoutRecyclerAdapter);
//                workoutRecyclerAdapter.notifyDataSetChanged();
//                //
//
//                //делаю календарь
//                AddCompetition.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        WorkoutDetailActivity.start();
//                    }
//                });
//                //
//
//                AddWorkout1.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AlertDialog.Builder builder =new AlertDialog.Builder(context);
//                        builder.setCancelable(true);
//                        View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_workout,null);
//                        final EditText WorkoutProgram = addView.findViewById(R.id.workout_program);
//                        final EditText WorkoutTime = addView.findViewById(R.id.workout_time);
//                        final EditText WorkoutSize = addView.findViewById(R.id.workout_size);
//                        Button AddWorkout = addView.findViewById(R.id.add_workout);
//
//                        final String date = dateFormat.format(dateList.get(position));
//
//                        AddWorkout.setOnClickListener(new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                SaveWorkout(WorkoutProgram.getText().toString(),WorkoutSize.getText().toString(),WorkoutTime.getText().toString(),date
//                                        ,monthFormat.format(dateList.get(position)),yearFormat.format(dateList.get(position)));
//                                SetupCalendar();
//                                RecyclerAdapter workoutRecyclerAdapter = new RecyclerAdapter(addEvent.getContext()
//                                        ,CollectWorkout(date));
//                                workouts_list.setAdapter(workoutRecyclerAdapter);
//                                workoutRecyclerAdapter.notifyDataSetChanged();
//                                add_workout_dialog.dismiss();
//
//                            }
//                        });
//
//                        builder.setView(addView);
//
//                        add_workout_dialog = builder.create();
//                        add_workout_dialog.show();
//                    }
//                });
//                builder1.setView(addEvent);
//                add_event_dialog = builder1.create();
//                add_event_dialog.show();
//                add_event_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        SetupCalendar();
//                    }
//                });
//                //
//
//            }
//        });
//
//        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                final String date = dateFormat.format(dateList.get(position));
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setCancelable(true);
//                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_workouts,null);
//                RecyclerView workouts_list= (RecyclerView) showView.findViewById(R.id.workouts_list);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
//                workouts_list.setLayoutManager(layoutManager);
//                workouts_list.setHasFixedSize(true);
//
//                RecyclerAdapter workoutRecyclerAdapter = new RecyclerAdapter(showView.getContext()
//                        ,CollectWorkout(date));
//                workouts_list.setAdapter(workoutRecyclerAdapter);
//                workoutRecyclerAdapter.notifyDataSetChanged();
//                builder.setView(showView);
//                alertDialog =builder.create();
//                alertDialog.show();
//                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        SetupCalendar();
//                    }
//                });
//
//                return true;
//            }
//        });
//
//    }
//
//
//    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//
//
//    private ArrayList<Workout> CollectWorkout(String date){
//        ArrayList<Workout> arrayList = new ArrayList<>();
//        dbOpenHelper = new DBOpenHelper(context);
//        SQLiteDatabase sqLiteDatabase = dbOpenHelper.getReadableDatabase();
//        Cursor cursor = dbOpenHelper.ReadWorkouts(date,sqLiteDatabase);
//        while (cursor.moveToNext()){
//            String program = cursor.getString(cursor.getColumnIndex(DBStructure.PROGRAM));
//            String size = cursor.getString(cursor.getColumnIndex(DBStructure.SIZE));
//            String Time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
//            String Date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
//            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
//            String year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
//            Workout workout = new Workout(program,size,Time,Date,month,year);
//            arrayList.add(workout);
//        }
//        cursor.close();
//        dbOpenHelper.close();
//
//        return arrayList;
//    }
//
//    private void IntializeLayout(){
//
//        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.calendar_layout,this);
//        PreviouseButton = view.findViewById(R.id.previousBtn);
//        NextButton = view.findViewById(R.id.nextBtn);
//        CurrentDate = view.findViewById(R.id.current_Date);
//        gridView = view.findViewById(R.id.gridview);
//
//
//    }
//
//    private void SetupCalendar(){
//        String StartDate = simpleDateFormat.format(calendar.getTime());
//        CurrentDate.setText(StartDate);
//        dateList.clear();
//        Calendar monthCalendar = (Calendar)calendar.clone();
//        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
//        int FirstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-2;
//        monthCalendar.add(Calendar.DAY_OF_MONTH,-FirstDayOfMonth);
//
//
//        COllectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormat.format(calendar.getTime()));
//
//
//        while (dateList.size() < MAX_CALENDAR_Days){
//            dateList.add(monthCalendar.getTime());
//            monthCalendar.add(Calendar.DAY_OF_MONTH,1);
//
//        }
//        adapter = new GridAdapter(context,dateList,calendar,workoutList);
//        gridView.setAdapter(adapter);
//
//
//    }
//
//    private void SaveWorkout(String program, String size, String time, String date, String month, String year){
//        dbOpenHelper = new DBOpenHelper(context);
//        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
//        dbOpenHelper.SaveWorkout(program,size,time,date,month,year,database);
//        dbOpenHelper.close();
//        Toast.makeText(context, "Тренировка сохранена", Toast.LENGTH_LONG).show();
//    }
//
//    private Date convertStringToDate(String dateInString){
//        java.text.SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        Date date = null;
//        try {
//            date = format.parse(dateInString);
//
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//        return date;
//    }
//
//    private void COllectEventsPerMonth(String Month,String Year){
//        workoutList.clear();
//        dbOpenHelper = new DBOpenHelper(context);
//        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
//        Cursor cursor = dbOpenHelper.ReadWorkoutsperMonth(Month,Year,database);
//        while (cursor.moveToNext()){
//            String program = cursor.getString(cursor.getColumnIndex(DBStructure.PROGRAM));
//            String size = cursor.getString(cursor.getColumnIndex(DBStructure.SIZE));
//            String Time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
//            String Date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
//            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
//            String year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
//            Workout workout = new Workout(program, size, Time,Date,month,year);
//            workoutList.add(workout);
//        }
//        cursor.close();
//        dbOpenHelper.close();
//    }
//
//
//}

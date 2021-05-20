package com.example.myapplication.results;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.App;
import com.example.myapplication.R;
import com.example.myapplication.calculate.CalculateFragment;
import com.example.myapplication.calculate.DBHelper;
import com.example.myapplication.calendar.CalendarFragment;
import com.example.myapplication.models.Result;
import com.example.myapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Calendar;

public class ResultDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private static final String EXTRA_RESULT = "ResultDetailActivity.EXTRA_RESULT";

    private Result result;

    private TextView time;

    private TextView rank,points;

    private Spinner spinner_curse;

    private Spinner spinner_distance;

    private Button select_date;


    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private DatabaseReference users;

    private User user;

    DBHelper dbHelper;

    String[] distance = {"50 в/ст", "100 в/ст", "200 в/ст", "400 в/ст", "800 в/ст", "1500 в/ст","50 н/сп","100 н/сп","200 н/сп","50 бр","100 бр","200 бр","50 батт","100 батт","200 батт","100 к/пл","200 к/пл","400 к/пл"};
    String[] curse = {"25м", "50м"};

    String[] ranks = {"msmk","ms","kms","first_r","second_r","third_r","first_rj","second_rj","third_rj"};
    String[] ranks1 = {"МСМК", "МС", "КМС", "1 разряд", "2 разряд","3 разряд", "1 юн. разряд", "2 юн. разряд", "3 юн. разряд"};

    public static void start(Activity caller, Result result){
        Intent intent = new Intent(caller,ResultDetailActivity.class);
        if (result != null){
            intent.putExtra(EXTRA_RESULT, result);
        }

        caller.startActivity(intent);
    }

    public static int spinner_after(String[] array, String string){

        for (int i = 0; i<array.length;i++){
            if (array[i].equals(string))
                return i;
        }

        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Защли в самое начало");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        dbHelper = new DBHelper(ResultDetailActivity.this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        spinner_distance = findViewById(R.id.spinner_distance);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter_dist = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, distance);
        // Определяем разметку для использования при выборе элемента
        adapter_dist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner_distance.setAdapter(adapter_dist);



        spinner_curse = findViewById(R.id.spinner_curse);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter_pool = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, curse);
        // Определяем разметку для использования при выборе элемента
        adapter_pool.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner_curse.setAdapter(adapter_pool);


        setTitle(R.string.result_detail_title);

        time = findViewById(R.id.time);
        rank = findViewById(R.id.rank);
        points = findViewById(R.id.points);

        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        users = FirebaseDatabase.getInstance("https://swimapp-24f8a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.child(CurrentUser.getUid()).getValue(User.class);
                System.out.println(user.getGender());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        time.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String Time = time.getText().toString();
                    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                            .appendPattern("mm:ss:SS")
                            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0) // optional, but you can set other value
                            .toFormatter();

                    DateTimeFormatter formatter1 = new DateTimeFormatterBuilder()
                            .appendPattern("mm:ss,SS")
                            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0) // optional, but you can set other value
                            .toFormatter();

                    DateTimeFormatter formatter2 = new DateTimeFormatterBuilder()
                            .appendPattern("ss,SS")
                            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0) // optional, but you can set other value
                            .parseDefaulting(ChronoField.MINUTE_OF_HOUR,0)
                            .toFormatter();

                    try {
                        LocalTime t = LocalTime.parse(Time, formatter);


                        Cursor cursor;

                        Cursor cursor1;
                        if (spinner_curse.getSelectedItem().toString().equals("25м")) {
                            if (user.getGender().equals("Мужчина")) {
                                cursor = database.query(DBHelper.TABLE_SHORT_CURSE_M, new String[]{DBHelper.KEY_BASE_TIME}, "distance = ?", new String[]{spinner_distance.getSelectedItem().toString()}, null, null, null);
                                cursor1 = database.query(DBHelper.TABLE_SHORT_CURSE_M, new String[]{DBHelper.KEY_MSMK,DBHelper.KEY_MS,DBHelper.KEY_KMS,DBHelper.KEY_FIRST_R,DBHelper.KEY_SECOND_R,DBHelper.KEY_THIRD_R,DBHelper.KEY_FIRST_RJ,DBHelper.KEY_SECOND_RJ,DBHelper.KEY_THIRD_RJ}, "distance = ?", new String[]{spinner_distance.getSelectedItem().toString()}, null, null, null);
                            } else {
                                cursor = database.query(DBHelper.TABLE_SHORT_CURSE_W, new String[]{DBHelper.KEY_BASE_TIME}, "distance = ?", new String[]{spinner_distance.getSelectedItem().toString()}, null, null, null);
                                cursor1 = database.query(DBHelper.TABLE_SHORT_CURSE_W, new String[]{DBHelper.KEY_MSMK,DBHelper.KEY_MS,DBHelper.KEY_KMS,DBHelper.KEY_FIRST_R,DBHelper.KEY_SECOND_R,DBHelper.KEY_THIRD_R,DBHelper.KEY_FIRST_RJ,DBHelper.KEY_SECOND_RJ,DBHelper.KEY_THIRD_RJ}, "distance = ?", new String[]{spinner_distance.getSelectedItem().toString()}, null, null, null);

                            }
                        }
                        else {
                            if (user.getGender().equals("Мужчина")) {
                                cursor = database.query(DBHelper.TABLE_LONG_CURSE_M, new String[]{DBHelper.KEY_BASE_TIME}, "distance = ?", new String[]{spinner_distance.getSelectedItem().toString()}, null, null, null);
                                cursor1 = database.query(DBHelper.TABLE_LONG_CURSE_M, new String[]{DBHelper.KEY_MSMK,DBHelper.KEY_MS,DBHelper.KEY_KMS,DBHelper.KEY_FIRST_R,DBHelper.KEY_SECOND_R,DBHelper.KEY_THIRD_R,DBHelper.KEY_FIRST_RJ,DBHelper.KEY_SECOND_RJ,DBHelper.KEY_THIRD_RJ}, "distance = ?", new String[]{spinner_distance.getSelectedItem().toString()}, null, null, null);

                            } else {
                                cursor = database.query(DBHelper.TABLE_LONG_CURSE_W, new String[]{DBHelper.KEY_BASE_TIME}, "distance = ?", new String[]{spinner_distance.getSelectedItem().toString()}, null, null, null);
                                cursor1 = database.query(DBHelper.TABLE_LONG_CURSE_W, new String[]{DBHelper.KEY_MSMK,DBHelper.KEY_MS,DBHelper.KEY_KMS,DBHelper.KEY_FIRST_R,DBHelper.KEY_SECOND_R,DBHelper.KEY_THIRD_R,DBHelper.KEY_FIRST_RJ,DBHelper.KEY_SECOND_RJ,DBHelper.KEY_THIRD_RJ}, "distance = ?", new String[]{spinner_distance.getSelectedItem().toString()}, null, null, null);

                            }
                        }

                        if (cursor.moveToFirst()){
                            cursor.getString(0);
                        }

                        if(cursor1.moveToFirst()){
                            for (int i = 0;i<cursor1.getColumnCount();i++){
                                try {
                                    LocalTime time2 = LocalTime.parse(cursor1.getString(i),formatter1);
                                    System.out.println(t.compareTo(time2));
                                    if (t.compareTo(time2) <= 0){
                                        rank.setText(ranks1[i]);
                                        break;
                                    }

                                } catch (DateTimeParseException e) {
                                    LocalTime time3 = LocalTime.parse(cursor1.getString(i),formatter2);
                                    System.out.println(t.compareTo(time3));
                                    if (t.compareTo(time3) <= 0){
                                        rank.setText(ranks1[i]);
                                        break;
                                    }
                                }

                            }
                        }

                        cursor1.close();

                        float seconds = t.getSecond();
                        float minutes = t.getMinute();
                        float milliseconds = t.get(ChronoField.MILLI_OF_SECOND);
                        float general_time= minutes*60 + seconds + milliseconds/1000;
                        double fina_points = 1000*Math.pow(cursor.getFloat(0)/general_time,3);
                        points.setText(String.valueOf((int)Math.floor(fina_points)));
                        cursor.close();
                    }
                    catch (DateTimeParseException exception) {
                        exception.printStackTrace();
                        Toast.makeText(ResultDetailActivity.this, "Введите корректные значения", Toast.LENGTH_LONG).show();
                        points.setText("");
                    }catch (NullPointerException exception1){
                        exception1.printStackTrace();
                    }

                    return true;
                }
                return false;
            }
        });


        select_date = findViewById(R.id.select_date);
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        if (getIntent().hasExtra(EXTRA_RESULT)){
            result = getIntent().getParcelableExtra(EXTRA_RESULT);
            time.setText(result.time);
            spinner_distance.setSelection(spinner_after(distance,result.distance));
            spinner_curse.setSelection(spinner_after(curse,result.curse));
            select_date.setText(result.date);
            rank.setText(result.rank);
            points.setText(result.points);

        }
        else{
          result = new Result();
        }

    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "." + month + "." + year;
        select_date.setText(date);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu_result_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                if (time.getText().toString().length()>0){
                    result.distance = spinner_distance.getSelectedItem().toString();
                    result.time = time.getText().toString();
                    result.date = select_date.getText().toString();
                    result.curse = spinner_curse.getSelectedItem().toString();
                    result.rank = rank.getText().toString();
                    result.points = points.getText().toString();
                    if (getIntent().hasExtra(EXTRA_RESULT)){
                        System.out.println("обновили");
                        App.getInstance().getResultDao().update(result);
                    } else{
                        System.out.println("добавили");
                        App.getInstance().getResultDao().insert(result);
                    }
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
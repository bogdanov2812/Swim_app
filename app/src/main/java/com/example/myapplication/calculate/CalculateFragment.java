package com.example.myapplication.calculate;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.RanksFragment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;

public class CalculateFragment extends Fragment {

    String[] distance = {"50 в/ст", "100 в/ст", "200 в/ст", "400 в/ст", "800 в/ст", "1500 в/ст","50 н/сп","100 н/сп","200 н/сп","50 бр","100 бр","200 бр","50 батт","100 батт","200 батт","100 к/пл","200 к/пл","400 к/пл"};
    ArrayList<String> distance_list = new ArrayList<String>();

    String[] gender = {"Мужчина", "Женщина"};
    int count_gender = gender.length;
    int position_gender = 0;

    String[] pool = {"25м", "50м"};
    int count_pool = pool.length;
    int position_pool = 0;

    DBHelper dbHelper;

    private Button new_calculate;

    ImageButton btPrevious_gender, btNext_gender,btPrevious_pool, btNext_pool;
    private TextSwitcher textSwitcher_gender,textSwitcher_pool;

    private Spinner spinner_dist;

    private String selected_curse, selected_distance, selected_gender;

    private TextView resText;

    private EditText time;



    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculate, container, false);

        distance_list.addAll(Arrays.asList(distance));

        spinner_dist = view.findViewById(R.id.spinner2);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter_dist = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, distance_list);
        // Определяем разметку для использования при выборе элемента
        adapter_dist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner_dist.setAdapter(adapter_dist);
//        selected_distance = spinner_dist.getSelectedItem().toString();

        btPrevious_gender = view.findViewById(R.id.bt_previos_gender);
        btNext_gender = view.findViewById(R.id.bt_next_gender);
        textSwitcher_gender = view.findViewById(R.id.text_switcher_gender);
        btPrevious_pool = view.findViewById(R.id.bt_previos_pool);
        btNext_pool = view.findViewById(R.id.bt_next_pool);
        textSwitcher_pool = view.findViewById(R.id.text_switcher_pool);

        textSwitcher_gender.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(CalculateFragment.this.getActivity());
                textView.setText(gender[0]);
                textView.setTextSize(15);
                textView.setGravity(Gravity.CENTER);
                return textView;
            }
        });


        textSwitcher_pool.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(CalculateFragment.this.getActivity());
                textView.setText(pool[0]);
                textView.setTextSize(15);
                textView.setGravity(Gravity.CENTER);
                return textView;
            }
        });

        btNext_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_gender++;
                if (position_gender==count_gender)
                    position_gender=0;
                textSwitcher_gender.setText(gender[position_gender]);

            }
        });

        btNext_pool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_pool++;
                if (position_pool==count_pool)
                    position_pool=0;
                textSwitcher_pool.setText(pool[position_pool]);

                if (position_pool == 1){
                    adapter_dist.remove("100 к/пл");
                    if(spinner_dist.getSelectedItemPosition()>=16)
                        spinner_dist.setSelection(spinner_dist.getSelectedItemPosition()-1);
                }else {
                    adapter_dist.insert("100 к/пл",15);
                    if(spinner_dist.getSelectedItemPosition()>=15)
                        spinner_dist.setSelection(spinner_dist.getSelectedItemPosition()+1);
                }

            }
        });


        btPrevious_gender.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSwitcher_gender.showPrevious();
                --position_gender;
                if(position_gender<0)
                    position_gender=gender.length-1;
                textSwitcher_gender.setText(gender[position_gender]);

            }
        }));

        btPrevious_pool.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSwitcher_pool.showPrevious();
                --position_pool;
                if(position_pool<0)
                    position_pool=pool.length-1;
                textSwitcher_pool.setText(pool[position_pool]);

                if (position_pool == 1){
                    adapter_dist.remove("100 к/пл");
                    if(spinner_dist.getSelectedItemPosition()>=16)
                        spinner_dist.setSelection(spinner_dist.getSelectedItemPosition()-1);
                }else {
                    adapter_dist.insert("100 к/пл",15);
                    if(spinner_dist.getSelectedItemPosition()>=15)
                        spinner_dist.setSelection(spinner_dist.getSelectedItemPosition()+1);
                }

            }
        }));

        dbHelper = new DBHelper(this.getActivity());
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        time = view.findViewById(R.id.editTextTime);
        resText = view.findViewById(R.id.textView);


        Button result = (Button) view.findViewById(R.id.button);
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor;

                TextView current_gender_view = (TextView) textSwitcher_gender.getCurrentView();
                selected_gender = current_gender_view.getText().toString();

                TextView current_pool_view = (TextView) textSwitcher_pool.getCurrentView();
                selected_curse = current_pool_view.getText().toString();

                selected_distance = spinner_dist.getSelectedItem().toString();
                if (selected_curse.equals("25м")) {
                    if (selected_gender.equals("Мужчина")) {
                        cursor = database.query(DBHelper.TABLE_SHORT_CURSE_M, new String[]{DBHelper.KEY_BASE_TIME}, "distance = ?", new String[]{selected_distance}, null, null, null);
                    } else {
                        cursor = database.query(DBHelper.TABLE_SHORT_CURSE_W, new String[]{DBHelper.KEY_BASE_TIME}, "distance = ?", new String[]{selected_distance}, null, null, null);
                    }
                }
                else {
                    if (selected_gender.equals("Мужчина")) {
                        cursor = database.query(DBHelper.TABLE_LONG_CURSE_M, new String[]{DBHelper.KEY_BASE_TIME}, "distance = ?", new String[]{selected_distance}, null, null, null);
                    } else {
                        cursor = database.query(DBHelper.TABLE_LONG_CURSE_W, new String[]{DBHelper.KEY_BASE_TIME}, "distance = ?", new String[]{selected_distance}, null, null, null);
                    }
                }

                if (cursor.moveToFirst()){
                    cursor.getString(0);
                }


                String time_or_points = time.getText().toString();

                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .appendPattern("mm:ss:SS")
                        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0) // optional, but you can set other value
                        .toFormatter();

                try {
                    LocalTime t = LocalTime.parse(time_or_points, formatter);
                    float seconds = t.getSecond();
                    float minutes = t.getMinute();
                    float milliseconds = t.get(ChronoField.MILLI_OF_SECOND);
                    float general_time= minutes*60 + seconds + milliseconds/1000;
                    double fina_points = 1000*Math.pow(cursor.getFloat(0)/general_time,3);
                    resText.setText(String.valueOf((int)Math.floor(fina_points)));
                }
                catch (DateTimeParseException exception){
                    exception.printStackTrace();
                    try {
                        double points = Double.parseDouble(time_or_points);
                        double base_time_in_pow = Math.pow(cursor.getDouble(0),3);

                        double time =  Math.pow((1000.0*base_time_in_pow)/points, 1.0/3.0);


                        double double_minutes,double_seconds,double_milli;

                        int minutes=0;
                        int seconds=0;
                        int milli=0;

                        String  string_seconds, string_milli;
                        String string_minutes = "00";
                        String final_time;
                        if (time>60) {

                            double_minutes = BigDecimal.valueOf(time / 60.0).setScale(3, RoundingMode.DOWN).doubleValue();
                            minutes = (int) double_minutes;

                            System.out.println(minutes);


                            double_seconds = BigDecimal.valueOf((double_minutes % 1) * 60.0).setScale(3, RoundingMode.DOWN).doubleValue();
                            seconds = (int) double_seconds;
                            System.out.println(seconds);

                            double_milli = BigDecimal.valueOf((double_seconds % 1) * 100.0).setScale(3, RoundingMode.UP).doubleValue();
                            milli = (int) double_milli;

                            System.out.println(milli);

                        }else{
                            System.out.println(time);
                            double_seconds = BigDecimal.valueOf(time).setScale(3, RoundingMode.DOWN).doubleValue();
                            seconds = (int) double_seconds;

                            System.out.println(seconds);

                            double_milli = BigDecimal.valueOf((double_seconds % 1) * 100.0).setScale(3, RoundingMode.UP).doubleValue();
                            milli = (int) double_milli;

                        }

                        if (minutes < 10){
                            string_minutes = "0" + minutes;
                        }else{
                            string_minutes = String.valueOf(minutes);
                        }

                        if(seconds<10){
                            string_seconds = "0"+seconds;
                        }else{
                            string_seconds = String.valueOf(seconds);
                        }

                        if (milli<10){
                            string_milli = "0"+milli;
                        }else{
                            string_milli = String.valueOf(milli);
                        }

                        final_time = string_minutes+":"+string_seconds+":"+string_milli;
                        System.out.println(final_time);

                        cursor.close();
                        resText.setText(final_time);

                    }catch (NumberFormatException exception1){
                        exception1.printStackTrace();
                        Toast.makeText(CalculateFragment.this.getContext(), "Введите корректные значения", Toast.LENGTH_LONG).show();
                        resText.setText("");
                    }
                }

                cursor.close();
            }
        });

        return view;
    }
}
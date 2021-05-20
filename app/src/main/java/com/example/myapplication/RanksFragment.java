package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.myapplication.calculate.DBHelper;

import java.util.ArrayList;
import java.util.Arrays;


public class RanksFragment extends Fragment {

    ImageButton btPrevious_gender, btNext_gender,btPrevious_pool, btNext_pool;
    TextSwitcher textSwitcher_gender,textSwitcher_pool;
    TextView wr, er, nr, msmk, ms, kms, first_r, second_r, third_r, first_rj, second_rj, third_rj;
    Spinner spinner_dist;
    String selected_distance;

    String current_gender, current_pool;

    TextView current_gender_view, current_pool_view;

    String[] distance = {"50 в/ст", "100 в/ст", "200 в/ст", "400 в/ст", "800 в/ст", "1500 в/ст","50 н/сп","100 н/сп","200 н/сп","50 бр","100 бр","200 бр","50 батт","100 батт","200 батт","100 к/пл","200 к/пл","400 к/пл"};
    ArrayList <String> distance_list = new ArrayList<String>();


    String[] gender = {"Мужчина", "Женщина"};
    int count_gender = gender.length;
    int position_gender = 0;

    String[] pool = {"25м", "50м"};
    int count_pool = pool.length;
    int position_pool = 0;

    DBHelper dbHelper;

    private void setRank(String[] rank, TextView... textViews) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setText(rank[i]);
        }
    }

    public String[] getData(){
        String[] data = new String[18];

        selected_distance = spinner_dist.getSelectedItem().toString();

        current_gender_view = (TextView) textSwitcher_gender.getCurrentView();
        current_gender = current_gender_view.getText().toString();

        current_pool_view = (TextView) textSwitcher_pool.getCurrentView();
        current_pool = current_pool_view.getText().toString();


        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;

        if (current_pool.equals("25м"))
            if(current_gender.equals("Мужчина"))
                cursor = database.query(DBHelper.TABLE_SHORT_CURSE_M, new String[]{DBHelper.KEY_WR,DBHelper.KEY_ER,DBHelper.KEY_NR,DBHelper.KEY_MSMK,DBHelper.KEY_MS,DBHelper.KEY_KMS,DBHelper.KEY_FIRST_R,DBHelper.KEY_SECOND_R,DBHelper.KEY_THIRD_R,DBHelper.KEY_FIRST_RJ,DBHelper.KEY_SECOND_RJ,DBHelper.KEY_THIRD_RJ}, "distance = ?", new String[]{selected_distance}, null, null, null);
            else
                cursor = database.query(DBHelper.TABLE_SHORT_CURSE_W, new String[]{DBHelper.KEY_WR,DBHelper.KEY_ER,DBHelper.KEY_NR,DBHelper.KEY_MSMK,DBHelper.KEY_MS,DBHelper.KEY_KMS,DBHelper.KEY_FIRST_R,DBHelper.KEY_SECOND_R,DBHelper.KEY_THIRD_R,DBHelper.KEY_FIRST_RJ,DBHelper.KEY_SECOND_RJ,DBHelper.KEY_THIRD_RJ}, "distance = ?", new String[]{selected_distance}, null, null, null);
        else
        if(current_gender.equals("Мужчина"))
            cursor = database.query(DBHelper.TABLE_LONG_CURSE_M, new String[]{DBHelper.KEY_WR,DBHelper.KEY_ER,DBHelper.KEY_NR,DBHelper.KEY_MSMK,DBHelper.KEY_MS,DBHelper.KEY_KMS,DBHelper.KEY_FIRST_R,DBHelper.KEY_SECOND_R,DBHelper.KEY_THIRD_R,DBHelper.KEY_FIRST_RJ,DBHelper.KEY_SECOND_RJ,DBHelper.KEY_THIRD_RJ}, "distance = ?", new String[]{selected_distance}, null, null, null);
        else
            cursor = database.query(DBHelper.TABLE_LONG_CURSE_W, new String[]{DBHelper.KEY_WR,DBHelper.KEY_ER,DBHelper.KEY_NR,DBHelper.KEY_MSMK,DBHelper.KEY_MS,DBHelper.KEY_KMS,DBHelper.KEY_FIRST_R,DBHelper.KEY_SECOND_R,DBHelper.KEY_THIRD_R,DBHelper.KEY_FIRST_RJ,DBHelper.KEY_SECOND_RJ,DBHelper.KEY_THIRD_RJ}, "distance = ?", new String[]{selected_distance}, null, null, null);

        if (cursor.moveToFirst())
            for (int i = 0; i<cursor.getColumnCount();i++) {
                data[i] = cursor.getString(i);
            }

        cursor.close();

        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranks, container, false);

        distance_list.addAll(Arrays.asList(distance));

        btPrevious_gender = view.findViewById(R.id.bt_previos_gender);
        btNext_gender = view.findViewById(R.id.bt_next_gender);
        textSwitcher_gender = view.findViewById(R.id.text_switcher_gender);
        btPrevious_pool = view.findViewById(R.id.bt_previos_pool);
        btNext_pool = view.findViewById(R.id.bt_next_pool);
        textSwitcher_pool = view.findViewById(R.id.text_switcher_pool);

        wr = view.findViewById(R.id.wr);
        er = view.findViewById(R.id.er);
        nr = view.findViewById(R.id.nr);
        msmk = view.findViewById(R.id.msmk);
        ms = view.findViewById(R.id.ms);
        kms = view.findViewById(R.id.kms);
        first_r = view.findViewById(R.id.first_r);
        second_r = view.findViewById(R.id.second_r);
        third_r = view.findViewById(R.id.third_r);
        first_rj = view.findViewById(R.id.first_rj);
        second_rj = view.findViewById(R.id.second_rj);
        third_rj = view.findViewById(R.id.third_rj);

        dbHelper = new DBHelper(this.getContext());



        textSwitcher_gender.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(RanksFragment.this.getActivity());
                textView.setText(gender[0]);
                textView.setTextSize(15);
                textView.setGravity(Gravity.CENTER);
                return textView;
            }
        });


        textSwitcher_pool.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(RanksFragment.this.getActivity());
                textView.setText(pool[0]);
                textView.setTextSize(15);
                textView.setGravity(Gravity.CENTER);
                return textView;
            }
        });

        spinner_dist = view.findViewById(R.id.spinner3);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter_dist = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, distance_list);
        // Определяем разметку для использования при выборе элемента
        adapter_dist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner_dist.setAdapter(adapter_dist);

        spinner_dist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setRank(getData(),wr,er,nr,msmk, ms, kms, first_r, second_r, third_r, first_rj, second_rj, third_rj);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btNext_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_gender++;
                if (position_gender==count_gender)
                    position_gender=0;
                textSwitcher_gender.setText(gender[position_gender]);

                setRank(getData(),wr,er,nr,msmk, ms, kms, first_r, second_r, third_r, first_rj, second_rj, third_rj);

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

                setRank(getData(),wr,er,nr,msmk, ms, kms, first_r, second_r, third_r, first_rj, second_rj, third_rj);

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

                setRank(getData(),wr,er,nr,msmk, ms, kms, first_r, second_r, third_r, first_rj, second_rj, third_rj);

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

                setRank(getData(),wr,er,nr,msmk, ms, kms, first_r, second_r, third_r, first_rj, second_rj, third_rj);

            }
        }));

        return view;
    }
}
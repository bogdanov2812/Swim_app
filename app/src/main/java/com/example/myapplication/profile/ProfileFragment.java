package com.example.myapplication.profile;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myapplication.MenuActivity;
import com.example.myapplication.R;
import com.example.myapplication.enter.EnterActivity;
import com.example.myapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private EditText email;
    private EditText password;
    private EditText last_name;
    private EditText first_name;
    private Button date_of_birth;
    private Spinner rank;
    private EditText height;
    private EditText weight;
    private Spinner gender;
    private Button exit;
    private Button save;

    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private DatabaseReference users;

    private User user;

    String[] ranks = {"ЗМС", "МСМК", "МС", "КМС", "1 разряд", "2 разряд","3 разряд", "1 юн. разряд", "2 юн. разряд", "3 юн. разряд", "Нет разряда"};
    String[] genders = {"Мужчина", "Женщина"};

    public static int spinner_after(String[] array, String string){

        for (int i = 0; i<array.length;i++){
            if (array[i].equals(string))
                return i;
        }

        return 0;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Инициализация всех компонентов
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        last_name = view.findViewById(R.id.last_name);
        first_name = view.findViewById(R.id.first_name);
        height = view.findViewById(R.id.height);
        weight = view.findViewById(R.id.weight);

        rank = view.findViewById(R.id.rank);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter_rank = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, ranks);
        // Определяем разметку для использования при выборе элемента
        adapter_rank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        rank.setAdapter(adapter_rank);

        gender = view.findViewById(R.id.gender);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter_gender = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, genders);
        // Определяем разметку для использования при выборе элемента
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        gender.setAdapter(adapter_gender);

        //Вставляем значения в эти компоненты
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        users = FirebaseDatabase.getInstance("https://swimapp-24f8a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                System.out.println(snapshot.child(CurrentUser.getUid()).getValue().toString());
                user = snapshot.child(CurrentUser.getUid()).child("User_info").getValue(User.class);
                email.setText(user.getEmail());
                password.setText(user.getPassword());
                last_name.setText(user.getLast_name());
                first_name.setText(user.getFirst_name());
                height.setText(user.getHeight());
                weight.setText(user.getWeight());
                rank.setSelection(spinner_after(ranks,user.getRank()));
                gender.setSelection(spinner_after(genders,user.getGender()));
                date_of_birth.setText(user.getDate_of_birth());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Кнопка дата рождения
        date_of_birth = view.findViewById(R.id.date_of_birth);
        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(date_of_birth.getText().toString());
            }
        });

        //Кнопка выход
        exit = view.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitWindow();
            }
        });

        //Кнопка сохранить изменения
        save = view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, Object> map = new HashMap<>();

                        user = snapshot.child(CurrentUser.getUid()).child("User_info").getValue(User.class);

                        if (email.getText().toString().length()==0)
                        {
                            Toast.makeText(ProfileFragment.this.getContext(), "Заполните поле Email",Toast.LENGTH_SHORT).show();
                        } else if(!user.getEmail().equals(email.getText().toString())){
                            CurrentUser.updateEmail(email.getText().toString());
                            map.put(snapshot.child(CurrentUser.getUid()).child("User_info").child("email").getKey(), email.getText().toString());
                        }

                        if (password.getText().toString().length()==0)
                        {
                            Toast.makeText(ProfileFragment.this.getContext(), "Заполните поле Password",Toast.LENGTH_SHORT).show();
                        } else if(!user.getPassword().equals(password.getText().toString())){
                            CurrentUser.updatePassword(password.getText().toString());
                            map.put(snapshot.child(CurrentUser.getUid()).child("User_info").child("password").getKey(), password.getText().toString());
                        }

                        if (last_name.getText().toString().length()==0)
                        {
                            Toast.makeText(getActivity(), "Заполните поле Фамилия",Toast.LENGTH_SHORT).show();
                        } else if(!user.getLast_name().equals(last_name.getText().toString())){
                            map.put(snapshot.child(CurrentUser.getUid()).child("User_info").child("last_name").getKey(), last_name.getText().toString());
                        }

                        if (first_name.getText().toString().length()==0)
                        {
                            Toast.makeText(ProfileFragment.this.getContext(), "Заполните поле Имя",Toast.LENGTH_SHORT).show();
                        } else if(!user.getFirst_name().equals(first_name.getText().toString())){
                            map.put(snapshot.child(CurrentUser.getUid()).child("User_info").child("first_name").getKey(), first_name.getText().toString());
                        }

                        if (height.getText().toString().length()==0)
                        {
                            Toast.makeText(ProfileFragment.this.getContext(), "Заполните поле Рост (см)",Toast.LENGTH_SHORT).show();
                        } else if(!user.getHeight().equals(height.getText().toString())){
                            map.put(snapshot.child(CurrentUser.getUid()).child("User_info").child("height").getKey(), height.getText().toString());
                        }

                        if (weight.getText().toString().length()==0)
                        {
                            Toast.makeText(ProfileFragment.this.getContext(), "Заполните поле Вес (кг)",Toast.LENGTH_SHORT).show();
                        } else if(!user.getWeight().equals(weight.getText().toString())){
                            map.put(snapshot.child(CurrentUser.getUid()).child("User_info").child("weight").getKey(), weight.getText().toString());
                        }

                        if (date_of_birth.getText().toString().length()==0)
                        {
                            Toast.makeText(ProfileFragment.this.getContext(), "Введите дату рождения",Toast.LENGTH_SHORT).show();
                        } else if(!user.getDate_of_birth().equals(date_of_birth.getText().toString())){
                            map.put(snapshot.child(CurrentUser.getUid()).child("User_info").child("date_of_birth").getKey(), date_of_birth.getText().toString());
                        }

                        if(!user.getRank().equals(rank.getSelectedItem().toString())) {
                            map.put(snapshot.child(CurrentUser.getUid()).child("User_info").child("rank").getKey(), rank.getSelectedItem().toString());
                        }

                        if(!user.getGender().equals(gender.getSelectedItem().toString())) {
                            map.put(snapshot.child(CurrentUser.getUid()).child("User_info").child("gender").getKey(), gender.getSelectedItem().toString());
                        }

                        System.out.println(map + " " + map.isEmpty());
                        if (!map.isEmpty()) {
                            snapshot.getRef().child(CurrentUser.getUid()).child("User_info").updateChildren(map);
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



        return view;
    }

    //Показ окна при выходе
    private void showExitWindow(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(),R.style.MyDialogTheme);
        builder.setTitle("Выход из профиля");
        builder.setMessage("Вы действительно хотите выйти из этого аккаунта? Вы будете перенаправлены на окно Входа/Регистрации");
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                startActivity(new Intent(ProfileFragment.this.getActivity(), EnterActivity.class));
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF0000"));
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF0000"));
    }

    //Показ окна выбора даты
    private void showDatePickerDialog(String dateString){

        DatePickerDialog datePickerDialog;

        if (dateString.length() >0) {
            String[] date = dateString.split("\\.");
            int year = Integer.parseInt(date[2]);
            int month = Integer.parseInt(date[1])+1;
            int day = Integer.parseInt(date[0]);

            datePickerDialog = new DatePickerDialog(this.getContext(), (DatePickerDialog.OnDateSetListener) this,
                    year,month,day);
        }else{
            datePickerDialog = new DatePickerDialog(this.getContext(), (DatePickerDialog.OnDateSetListener) this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH)+1,
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }

        datePickerDialog.show();
        }


    //Слушаем что выбрал пользователь
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "." + month + "." + year;
        date_of_birth.setText(date);
    }

}
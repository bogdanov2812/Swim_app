package com.example.myapplication.enter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.MenuActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class EnterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private DatabaseReference users;

    private EditText email;
    private EditText password;

    private Button signIn;
    private Button register;
    private Button add;

    //Поля в окне регистрации
    private EditText register_email;
    private EditText register_password;
    private EditText last_name;
    private EditText first_name;
    private Button date_of_birth;
    private Spinner rank;
    private EditText height;
    private EditText weight;
    private Spinner gender;

    String[] ranks = {"ЗМС", "МСМК", "МС", "КМС", "1 разряд", "2 разряд","3 разряд", "1 юн. разряд", "2 юн. разряд", "3 юн. разряд", "Нет разряда"};
    String[] genders = {"Мужчина", "Женщина"};

        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        signIn = findViewById(R.id.enter);
        register = findViewById(R.id.register);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Чтобы сразу заходить
//                email.setText("hom6@gmail.com");
//                password.setText("123456789");
                //
                if(email.getText().toString().length() != 0 & password.getText().toString().length() != 0)

                    signing(email.getText().toString(),password.getText().toString());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegistrationWindow();
            }
        });
        FirebaseUser user = mAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance("https://swimapp-24f8a-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        users = FirebaseDatabase.getInstance("https://swimapp-24f8a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");


    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            System.out.println("Какой-то пользователь есть");
            startActivity(new Intent(EnterActivity.this, MenuActivity.class));
            finish();
        } else
            System.out.println("Никого нет");
    }

    public void signing(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(EnterActivity.this, "Авторизация успешна", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EnterActivity.this, MenuActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EnterActivity.this, "Ошибка авторизации" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    //Ф-ция для показа окна регистрации
    private void showRegistrationWindow(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        builder.setTitle("Зарегистрироваться");
        builder.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.register_window, null);
        builder.setView(register_window);
        builder.setPositiveButton("Регистрация", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(register_email.getText().toString())){
                    Toast toast = Toast.makeText(register_window.getContext(),"Введите Ваш EMAIL", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(register_password.getText().toString().length() <5){
                    Toast toast = Toast.makeText(register_window.getContext(),"Придумайте пароль, содержащий более 5 символов", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(TextUtils.isEmpty(last_name.getText().toString())){
                    Toast toast = Toast.makeText(register_window.getContext(),"Введите Вашу фамилию", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(TextUtils.isEmpty(first_name.getText().toString())){
                    Toast toast = Toast.makeText(register_window.getContext(),"Введите Ваше имя", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(register_email.getText().toString(),register_password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(register_email.getText().toString());
                                user.setPassword(register_password.getText().toString());
                                user.setLast_name(last_name.getText().toString());
                                user.setFirst_name(first_name.getText().toString());
                                user.setDate_of_birth(date_of_birth.getText().toString());
                                user.setRank(rank.getSelectedItem().toString());
                                user.setGender(gender.getSelectedItem().toString());
                                user.setHeight(height.getText().toString());
                                user.setWeight(weight.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast toast = Toast.makeText(register_window.getContext(),"Пользователь добавлен!", Toast.LENGTH_SHORT);
                                                toast.show();
                                            }
                                        });
                            }
                        });
            }
        });


        register_email = register_window.findViewById(R.id.email_register);
        register_password = register_window.findViewById(R.id.password_register);
        last_name = register_window.findViewById(R.id.last_name);
        first_name = register_window.findViewById(R.id.first_name);
        height = register_window.findViewById(R.id.height);
        weight = register_window.findViewById(R.id.weight);

        if (email.getText().toString().length() > 0)
        {
            register_email.setText(email.getText().toString());
        }

        if (password.getText().toString().length() > 0)
        {
            register_password.setText(password.getText().toString());
        }

        rank = register_window.findViewById(R.id.rank);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter_rank = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ranks);
        // Определяем разметку для использования при выборе элемента
        adapter_rank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        rank.setAdapter(adapter_rank);
        rank.setSelection(3);

        gender = register_window.findViewById(R.id.gender);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter_gender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genders);
        // Определяем разметку для использования при выборе элемента
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        gender.setAdapter(adapter_gender);

        date_of_birth = register_window.findViewById(R.id.date_of_birth);
        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });


        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF0000"));
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF0000"));


    }

    //Показ окна выбора даты
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (DatePickerDialog.OnDateSetListener) this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    //Слушаем что выбрал пользователь
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "." + month + "." + year;
        date_of_birth.setText(date);
    }
    
}
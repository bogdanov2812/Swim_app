package com.example.myapplication.calendar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.App;
import com.example.myapplication.R;
import com.example.myapplication.RanksFragment;
import com.example.myapplication.models.Result;
import com.example.myapplication.models.Workout;
import com.example.myapplication.results.ResultDetailActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class WorkoutDetailActivity extends AppCompatActivity {

    private static final String EXTRA_WORKOUT = "WorkoutDetailActivity.EXTRA_WORKOUT";

    private Workout workout;

    private EditText workout_target, workout_program, workout_size;

    private TextView workout_date;

    private TextSwitcher workoutTime_switcher;

    private ImageButton btn_previous_time, btn_next_time;

    //Для фото
    private ImageButton add_photo,delete_photo;
    private ImageView photo;
    private final int Pick_image = 1;
    //

    final String[] workoutTime= {"Утро","День","Вечер"};
    int count_workoutTime = workoutTime.length;
    int position_workoutTime = 0;

    public static void start(Activity caller, Workout workout, String date, String month, String year){
        Intent intent = new Intent(caller, WorkoutDetailActivity.class);
        if (workout != null){
            intent.putExtra(EXTRA_WORKOUT, workout);
        }else{
            intent.putExtra("DATE", date);
            intent.putExtra("MONTH", month);
            intent.putExtra("YEAR", year);
        }

        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_workout);

        workoutTime_switcher = findViewById(R.id.text_switcher_time);
        btn_next_time = findViewById(R.id.bt_next_time);
        btn_previous_time = findViewById(R.id.bt_previos_time);

        workout_date = findViewById(R.id.workout_date);

        workout_program = findViewById(R.id.workout_program);
        workout_size = findViewById(R.id.workout_size);
        workout_target = findViewById(R.id.workout_target);

        //Для фото
        add_photo = findViewById(R.id.add_photo);
        delete_photo = findViewById(R.id.delete_photo);
        photo = findViewById(R.id.image_view);


        //

        if (getIntent().hasExtra(EXTRA_WORKOUT)){
            workout = getIntent().getParcelableExtra(EXTRA_WORKOUT);
            position_workoutTime = Arrays.asList(workoutTime).indexOf(workout.TIME);
            workout_program.setText(workout.PROGRAM);
            workout_size.setText(workout.SIZE);
            workout_date.setText(workout.DATE);
            workout_target.setText(workout.TARGET);
            //Для фото
            if (workout.PHOTO != null)
                try {
    //                grantUriPermission(workout.PHOTO,);
    //                Bitmap galleryPic = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(workout.PHOTO));
    //                photo.setImageBitmap(galleryPic);
                    final InputStream imageStream = getContentResolver().openInputStream(Uri.parse(workout.PHOTO));
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    photo.setImageBitmap(selectedImage);
                    workout_program.setVisibility(View.INVISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();}



            //
        }
        else{
            workout_date.setText(getIntent().getExtras().get("DATE").toString());
            workout = new Workout();
        }

        delete_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo.setImageDrawable(null);
                workout_program.setVisibility(View.VISIBLE);
                workout.PHOTO = null;
            }
        });

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionStatus = ContextCompat.checkSelfPermission(WorkoutDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                System.out.println(permissionStatus);
                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                    Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                    photoPickerIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    photoPickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //Тип получаемых объектов - image:
                    photoPickerIntent.setType("image/*");
                    //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                    startActivityForResult(photoPickerIntent, Pick_image);
                } else {
                    System.out.println("TYT");
                    ActivityCompat.requestPermissions(WorkoutDetailActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }

            }
        });
        workoutTime_switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(WorkoutDetailActivity.this);
                textView.setText(workoutTime[position_workoutTime]);
                textView.setTextSize(15);
                textView.setGravity(Gravity.CENTER);
                return textView;
            }
        });

        btn_next_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_workoutTime++;
                if (position_workoutTime==count_workoutTime)
                    position_workoutTime=0;
                workoutTime_switcher.setText(workoutTime[position_workoutTime]);

            }
        });

        btn_previous_time.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutTime_switcher.showPrevious();
                --position_workoutTime;
                if(position_workoutTime<0)
                    position_workoutTime=workoutTime.length-1;
                workoutTime_switcher.setText(workoutTime[position_workoutTime]);

            }
        }));





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                    Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                    photoPickerIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    photoPickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //Тип получаемых объектов - image:
                    photoPickerIntent.setType("image/*");
                    //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                    startActivityForResult(photoPickerIntent, Pick_image);
                } else {
                    // permission denied
                }
                return;
        }
    }

    //Для изображения
    //Обрабатываем результат выбора в галерее:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    try {
                        workout_program.setVisibility(View.INVISIBLE);
                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:

                        //
                        final int takeFlags = imageReturnedIntent.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        //

                        final Uri imageUri = imageReturnedIntent.getData();
                        workout.PHOTO = imageUri.toString();
                        ContentResolver resolver = getContentResolver();
                        resolver.takePersistableUriPermission(imageUri, takeFlags);
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        photo.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    //

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
                if (workout_program.getText().toString().length()>0|!hasNullOrEmptyDrawable(photo)){

                    TextView current_workoutTime_view = (TextView) workoutTime_switcher.getCurrentView();
                    workout.TIME = current_workoutTime_view.getText().toString();
                    workout.TARGET = workout_target.getText().toString();
                    workout.PROGRAM = workout_program.getText().toString();
                    workout.SIZE = workout_size.getText().toString();

                    //пока так
                    if(workout.DATE == null||workout.MONTH == null||workout.YEAR == null) {
                        workout.DATE = getIntent().getExtras().get("DATE").toString();
                        workout.MONTH = getIntent().getExtras().get("MONTH").toString();
                        workout.YEAR = getIntent().getExtras().get("YEAR").toString();
                    }
                    //

                    if (getIntent().hasExtra(EXTRA_WORKOUT)){
                        System.out.println("обновили");
                        App.getInstance().getWorkoutDao().update(workout);
                    } else{
                        System.out.println("добавили");
                        App.getInstance().getWorkoutDao().insert(workout);
                    }
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean hasNullOrEmptyDrawable(ImageView iv)
    {
        Drawable drawable = iv.getDrawable();
        BitmapDrawable bitmapDrawable = drawable instanceof BitmapDrawable ? (BitmapDrawable)drawable : null;
        System.out.println(bitmapDrawable == null || bitmapDrawable.getBitmap() == null);
        return bitmapDrawable == null || bitmapDrawable.getBitmap() == null;
    }
}



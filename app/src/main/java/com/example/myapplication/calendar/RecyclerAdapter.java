package com.example.myapplication.calendar;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.myapplication.App;
import com.example.myapplication.R;
import com.example.myapplication.models.Result;
import com.example.myapplication.models.Workout;
import com.example.myapplication.results.AdapterForResults;
import com.example.myapplication.results.ResultDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    Context context;
    ArrayList<Workout> arrayList;
    DBOpenHelper dbOpenHelper;

    private SortedList<Workout> sortedList;

    public RecyclerAdapter(Context context){
        this.context = context;
        sortedList = new SortedList<Workout>(Workout.class, new SortedList.Callback<Workout>() {
            @Override
            public int compare(Workout o1, Workout o2) {
                return 0;
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position,count);
            }

            @Override
            public boolean areContentsTheSame(Workout oldItem, Workout newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Workout item1, Workout item2) {
                return item1.ID == item2.ID;
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position,count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition,toPosition);
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.show_one_workout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(sortedList.get(position));
    }

//    public RecyclerAdapter(Context context, ArrayList<Workout> arrayList) {
//        this.context = context;
//        this.arrayList = arrayList;
//    }

    public void setItemsNew(List<Workout> workouts){
        sortedList.replaceAll(workouts);
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Size,Time;
        ImageView delete;

        Workout workout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Size = itemView.findViewById(R.id.workout_size);
            Time = itemView.findViewById(R.id.workout_time);
            delete = itemView.findViewById(R.id.delete_icon);

            //мой код
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WorkoutDetailActivity.start((Activity) itemView.getContext(),workout,null,null,null);
                }
            });
            //

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    App.getInstance().getWorkoutDao().delete(workout);
                }
            });
        }

        public void bind(Workout workout) {
            this.workout = workout;

            Size.setText(workout.SIZE);
            Time.setText(workout.TIME);
        }
    }



}

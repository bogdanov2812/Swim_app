package com.example.myapplication.results;

import android.app.Activity;
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

import java.util.List;

public class AdapterForResults extends RecyclerView.Adapter<AdapterForResults.ResultViewHolder> {

    private SortedList<Result> sortedList;

    public AdapterForResults(){
        sortedList = new SortedList<>(Result.class, new SortedList.Callback<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return 0;
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position,count);
            }

            @Override
            public boolean areContentsTheSame(Result oldItem, Result newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Result item1, Result item2) {
                return item1.uid == item2.uid;
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
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bind(sortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public void setItems(List<Result> results){
        sortedList.replaceAll(results);
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder{

        //Мой код
        TextView timeText;
        TextView dateText;
        TextView curseText;
        //

        TextView distanceText;
        ImageView delete;

        Result result;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            distanceText = itemView.findViewById(R.id.distance_text);
            delete = itemView.findViewById(R.id.delete_icon);

            //
            timeText = itemView.findViewById(R.id.time_text);
            dateText = itemView.findViewById(R.id.date_text);
            curseText = itemView.findViewById(R.id.curse_text);
            //

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ResultDetailActivity.start((Activity) itemView.getContext(),result);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    App.getInstance().getResultDao().delete(result);
                }
            });
        }

        public void bind(Result result){
            this.result = result;

            distanceText.setText(result.distance);
            timeText.setText(result.time);
            dateText.setText(result.date);
            curseText.setText(result.curse);
        }
    }
}

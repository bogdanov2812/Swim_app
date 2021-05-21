package com.example.myapplication.results;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.myapplication.App;
import com.example.myapplication.R;
import com.example.myapplication.models.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdapterForResults extends RecyclerView.Adapter<AdapterForResults.ResultViewHolder> implements Filterable {

    private SortedList<Result> sortedList;
    private SortedList<Result> sortedList_filter;

    List<Result> resultsList_all;
    List<Result> resultList;


    public AdapterForResults(List<Result> resultsList){
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
//        sortedList_filter = sortedList;
        this.resultList = resultsList;
        this.resultsList_all = new ArrayList<>(resultsList);
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
//        holder.bind(sortedList.get(position));
        holder.bind(resultList.get(position));
    }

    @Override
    public int getItemCount() {

//        return sortedList.size();
        return resultList.size();
    }

    public void setItems(List<Result> results){
        sortedList.replaceAll(results);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Result> filtered_list = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filtered_list.addAll(resultsList_all);
            }else{
                for(int i = 0;i<resultsList_all.size();i++){
                    System.out.println(constraint.toString() + "   " + resultsList_all.get(i).distance + "  "+resultsList_all.get(i).distance.toLowerCase().contains(constraint.toString().toLowerCase()));
                    if (resultsList_all.get(i).distance.toLowerCase().contains(constraint.toString().toLowerCase())){
                        filtered_list.add(resultsList_all.get(i));
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered_list;
            System.out.println(filterResults.values);

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            resultList.clear();
            resultList.addAll((Collection<? extends Result>) filterResults.values);
            System.out.println(resultList.size());
            notifyDataSetChanged();

        }
    };

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

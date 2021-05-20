package com.example.myapplication.results;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Result;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ResultsFragment extends Fragment {


    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), recyclerView.VERTICAL,false); //чтобы список был сверху вниз
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL)); // разделительные полосы

        AdapterForResults adapter = new AdapterForResults();
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResultDetailActivity.start(ResultsFragment.this.getActivity(),null);
            }
        });

        ResultViewModel resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        resultViewModel.getResultLiveData().observe(getViewLifecycleOwner(), new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                adapter.setItems(results);
            }
        });


        return view;
    }
}
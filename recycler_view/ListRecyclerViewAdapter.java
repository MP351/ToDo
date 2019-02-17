package com.example.maxpayne.mytodoapp.recycler_view;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Task;

import java.util.List;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    List<Task> data;

    public ListRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
        Task task = data.get(i);
        recyclerViewHolder.tvNumber.setText(String.valueOf(task._id));
        recyclerViewHolder.chtvTask.setText(task.task);
        recyclerViewHolder.chtvTask.setChecked(task.complete == DbContract.ToDoEntry.COMPLETE_CODE);
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    public void setData(List<Task> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}

package com.example.maxpayne.mytodoapp.recycler_view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.maxpayne.mytodoapp.DetailTaskDialog;
import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Task;

import java.util.List;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> implements ItemTouchHelperAdapter {
    List<Task> data;
    Activity activity;
    FragmentManager fm;
    dbWorkListener mListener;

    public ListRecyclerViewAdapter(Activity activity, FragmentManager fm) {
        this.activity = activity;
        this.fm = fm;

        try {
            mListener = (dbWorkListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
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

        recyclerViewHolder.itemView.setOnClickListener(view -> {
            DetailTaskDialog detailTaskDialog = new DetailTaskDialog();
            detailTaskDialog.setTask(task);
            detailTaskDialog.show(fm, "showDetailDialog");
        });
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

    @Override
    public void onLeftSwipe(int position) {
        Task task = data.get(position);

        if (task.archived == DbContract.ToDoEntry.NOT_ARCHIVED_CODE) {
            switch (task.complete) {
                case DbContract.ToDoEntry.INCOMPLETE_CODE:
                    task.complete = DbContract.ToDoEntry.CANCEL_CODE;
                    mListener.updateTask(task);
                    break;
                case DbContract.ToDoEntry.COMPLETE_CODE:
                    task.archived = DbContract.ToDoEntry.ARCHIVED_CODE;
                    mListener.updateTask(task);
                    break;
            }
        }
    }

    public interface dbWorkListener {
        void deleteTask(Task task);
        void updateTask(Task task);
    }
}

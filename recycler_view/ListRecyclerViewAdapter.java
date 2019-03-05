package com.example.maxpayne.mytodoapp.recycler_view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.maxpayne.mytodoapp.ui.DetailTaskDialog;
import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Task;

import java.util.ArrayList;
import java.util.List;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> implements ItemTouchHelperAdapter {
    private List<Task> data = new ArrayList<>();
    private List<Task> cache = new ArrayList<>();
    private FragmentManager fm;
    private dbWorkListener mDbWorkListener;
    private swipeListener mSwipeListener;

    public final int ACTION_CODE_CANCEL = 0;
    public final int ACTION_CODE_TO_ARCHIVE = 1;

    public ListRecyclerViewAdapter(Activity activity, FragmentManager fm) {
        this.fm = fm;

        try {
            mDbWorkListener = (dbWorkListener) activity;
            mSwipeListener = (swipeListener) activity;
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
        this.cache = data;
        setSelection(0);
        notifyDataSetChanged();
    }

    @Override
    public void onLeftSwipe(int position) {
        Task task = data.get(position);

        if (task.archived == DbContract.ToDoEntry.NOT_ARCHIVED_CODE) {
            switch (task.complete) {
                case DbContract.ToDoEntry.INCOMPLETE_CODE:
                    task.complete = DbContract.ToDoEntry.CANCEL_CODE;
                    mDbWorkListener.archiveOrCancelTask(task, ACTION_CODE_CANCEL);
                    data.remove(task);
                    break;
                case DbContract.ToDoEntry.COMPLETE_CODE:
                    task.archived = DbContract.ToDoEntry.ARCHIVED_CODE;
                    mDbWorkListener.archiveOrCancelTask(task, ACTION_CODE_TO_ARCHIVE);
                    data.remove(task);
                    break;
            }
        }
        notifyItemChanged(position);
    }



    public void setSelection(int selection) {
        ArrayList<Task> selected = new ArrayList<>();

        for (Task task : cache) {
            switch (selection) {
                case 0:
                    mSwipeListener.setSwapEnable(true);
                    if (task.archived != DbContract.ToDoEntry.ARCHIVED_CODE &&
                        task.complete != DbContract.ToDoEntry.CANCEL_CODE)
                        selected.add(task);
                    break;
                case 1:
                    mSwipeListener.setSwapEnable(true);
                    if (task.archived != DbContract.ToDoEntry.ARCHIVED_CODE &&
                        task.complete == DbContract.ToDoEntry.INCOMPLETE_CODE)
                        selected.add(task);
                    break;
                case 2:
                    mSwipeListener.setSwapEnable(true);
                    if (task.archived != DbContract.ToDoEntry.ARCHIVED_CODE &&
                    task.complete == DbContract.ToDoEntry.COMPLETE_CODE)
                        selected.add(task);
                    break;
                case 3:
                    mSwipeListener.setSwapEnable(false);
                    if (task.archived != DbContract.ToDoEntry.ARCHIVED_CODE &&
                    task.complete == DbContract.ToDoEntry.CANCEL_CODE)
                        selected.add(task);
                    break;
                case 4:
                    mSwipeListener.setSwapEnable(false);
                    if (task.archived == DbContract.ToDoEntry.ARCHIVED_CODE)
                        selected.add(task);
                    break;
            }
        }

        data = selected;
        notifyDataSetChanged();
    }

    public interface dbWorkListener {
        void deleteTask(Task task);
        void updateTask(Task task);
        void archiveOrCancelTask(Task task, int CODE);
    }

    public interface swipeListener{
        void setSwapEnable(boolean enabled);
    }
}

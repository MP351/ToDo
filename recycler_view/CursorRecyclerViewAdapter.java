package com.example.maxpayne.mytodoapp.recycler_view;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maxpayne.mytodoapp.DetailTaskDialog;
import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.Database;
import com.example.maxpayne.mytodoapp.db.DbContract;

import java.util.ArrayList;

public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.CursorViewHolder> implements ItemTouchHelperAdapter{
    private Cursor cursor;
    private Context context;
    private FragmentManager fm;
    private Database db;

    private ArrayList<Integer> pend = new ArrayList<>();

    private int selectionCode = 0;
    private final int SELECTION_ALL_CODE = 0;
    private final int SELECTION_INCOMPLETE_CODE = 1;
    private final int SELECTION_COMPLETE_CODE = 2;
    private final int SELECTION_CANCEL_CODE = 3;
    private final int SELECTION_ARCHIVED_CODE = 4;

    CursorRecyclerViewAdapter(Context context, FragmentManager fm) {
        this.context = context;
        this.fm = fm;

        db = new Database(context);
        cursor = db.getAllTasks();
        this.cursor.moveToFirst();
    }

    @NonNull
    @Override
    public CursorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CursorViewHolder cvh;
        if (viewGroup.getTag() == null) {
            //View view = LayoutInflater.from(viewGroup.getContext())
            //        .inflate(R.layout.item, viewGroup, false);
            //cvh = new CursorViewHolder(view);
            //viewGroup.setTag(cvh);
        } else {
            cvh = (CursorViewHolder) viewGroup.getTag();
        }
        return new CursorViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final CursorViewHolder cursorViewHolder, int i) {
        cursor.moveToPosition(i);
        final String _id = cursor.getString(
                cursor.getColumnIndexOrThrow(DbContract.ToDoEntry._ID));
        final String taskN = cursor.getString(
                cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_TASK));
        final long addDate = cursor.getLong(
                cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_ADD_DATE));
        final long endDate = cursor.getLong(
                cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_END_DATE));
        final String descr = cursor.getString(
                cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_DESCRIPTION));

        if (pend.contains(Integer.valueOf(_id))) {
            cursorViewHolder.viewForeground.setVisibility(View.GONE);
            cursorViewHolder.viewBackground.setVisibility(View.VISIBLE);
        } else {
            cursorViewHolder.viewForeground.setVisibility(View.VISIBLE);
            cursorViewHolder.viewBackground.setVisibility(View.GONE);
            cursorViewHolder.tvNumber.setText(_id);
            cursorViewHolder.chtvTask.setText(taskN);
            cursorViewHolder.chtvTask.setChecked(cursor.getInt(
                    cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_COMPLETE))
                    == DbContract.ToDoEntry.COMPLETE_CODE);
            cursorViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailTaskDialog dtd = new DetailTaskDialog();
                    dtd.set_id(Integer.parseInt(_id));
                    dtd.setTaskHead(taskN);
                    dtd.setAddDate(addDate);
                    dtd.setEndDate(endDate);
                    dtd.setTaskDescription(descr);
                    dtd.show(fm, "detailDialog");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    static class CursorViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;
        CheckedTextView chtvTask;

        TextView tvUdo;
        Button btnUdo;

        ConstraintLayout viewBackground;
        CardView viewForeground;

        CursorViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false));

            tvNumber = itemView.findViewById(R.id.tv);
            chtvTask = itemView.findViewById(R.id.ctv);

            tvUdo = itemView.findViewById(R.id.tvUdo);
            btnUdo = itemView.findViewById(R.id.btnUdo);

            viewForeground = itemView.findViewById(R.id.view_foreground);
            viewBackground = itemView.findViewById(R.id.view_background);
        }
    }

    @Override
    public void onLeftSwipe(int position) {
        cursor.moveToPosition(position);
        int complete_code = cursor.getInt(
                cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_COMPLETE));

        if (complete_code == DbContract.ToDoEntry.COMPLETE_CODE) {
            //archiveTask(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry._ID)));
            pend.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry._ID)));
            notifyItemChanged(position);
        } else {
            pend.add(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry._ID)));
            notifyItemChanged(position);
            //cancelTask(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry._ID)));
        }
    }

    public void completeTask(int id) {
        db.completeTask(id);
        renewList();
    }

    public void cancelTask(int id) {
        db.cancelTask(id);
        renewList();
    }

    public void archiveTask(int id) {
        db.archiveTask(id);
        renewList();
    }

    public void addTask(String tn, String dc) {
        db.addTask(tn, dc);
        renewList();
    }

    public void renewList() {
        switch (selectionCode) {
            case SELECTION_ALL_CODE:
                cursor = db.getAllTasks();
                break;
            case SELECTION_INCOMPLETE_CODE:
                cursor = db.getTaskByState(DbContract.ToDoEntry.INCOMPLETE_CODE);
                break;
            case SELECTION_COMPLETE_CODE:
                cursor = db.getTaskByState(DbContract.ToDoEntry.COMPLETE_CODE);
                break;
            case SELECTION_CANCEL_CODE:
                cursor = db.getTaskByState(DbContract.ToDoEntry.CANCEL_CODE);
                break;
            case SELECTION_ARCHIVED_CODE:
                cursor = db.getTaskByArchivedState(DbContract.ToDoEntry.ARCHIVED_CODE);
                break;
        }
        notifyDataSetChanged();
    }

    public void setSelectionCode(int code) {
        selectionCode = code;
        renewList();
    }

    private void pendingToCancel(final int id) {
        pendingOperation(() -> cancelTask(id));
    }

    private void pendingToArchive(final int id) {
        pendingOperation(() -> archiveTask(id));
    }

    private void pendingOperation(Runnable task) {
        final int PENDING_TIMER = 3000; // 3 sec

        Handler handler = new Handler();
        handler.postDelayed(task, PENDING_TIMER);
    }
}

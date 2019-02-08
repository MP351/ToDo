package com.example.maxpayne.mytodoapp.recycler_view;

import android.content.Context;
import android.database.Cursor;
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
import android.widget.TextView;

import com.example.maxpayne.mytodoapp.DetailTaskDialog;
import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.Database;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.DbObserver;

import java.util.ArrayList;

public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.CursorViewHolder> implements ItemTouchHelperAdapter, DbObserver {
    private Cursor cursor;
    private Context context;
    private FragmentManager fm;
    private Database db;

    private ArrayList<Integer> pend = new ArrayList<>();

    CursorRecyclerViewAdapter(Context context, Database db, FragmentManager fm) {
        this.context = context;
        this.fm = fm;

        this.db = db;
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

    public void changeCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public void notifyObserver() {
        cursor.requery();
        notifyDataSetChanged();
    }
}

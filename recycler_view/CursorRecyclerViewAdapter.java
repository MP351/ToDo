package com.example.maxpayne.mytodoapp.recycler_view;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.maxpayne.mytodoapp.DetailTaskDialog;
import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;

public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.CursorViewHolder> {
    private Cursor cursor;
    private Context context;
    private FragmentManager fm;

    CursorRecyclerViewAdapter(Context context, Cursor cursor, FragmentManager fm) {
        this.context = context;
        this.cursor = cursor;
        this.fm = fm;
        this.cursor.moveToFirst();
    }

    @NonNull
    @Override
    public CursorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CursorViewHolder cvh;
        if (viewGroup.getTag() == null) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item, viewGroup, false);
            cvh = new CursorViewHolder(view);
            viewGroup.setTag(cvh);
        } else {
            cvh = (CursorViewHolder) viewGroup.getTag();
        }
        return new CursorViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false));
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

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class CursorViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNumber;
        public CheckedTextView chtvTask;

        public CursorViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumber = itemView.findViewById(R.id.tv);
            chtvTask = itemView.findViewById(R.id.ctv);
        }
    }

    public void changeCursor(Cursor cursor) {
        this.cursor = cursor;
        this.notifyDataSetChanged();
    }
}

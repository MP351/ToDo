package com.example.maxpayne.mytodoapp.recycler_view;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;

public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.CursorViewHolder> {
    private Context context;
    private Cursor cursor;
    private LayoutInflater inflater;

    public CursorRecyclerViewAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        this.cursor.moveToFirst();
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CursorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CursorViewHolder cvh;
        if (viewGroup.getTag() == null) {
            inflater.inflate(R.layout.item, viewGroup, false);
            cvh = new CursorViewHolder(viewGroup);
            viewGroup.setTag(cvh);
        } else {
            cvh = (CursorViewHolder) viewGroup.getTag();
        }
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CursorViewHolder cursorViewHolder, int i) {
        cursorViewHolder.tvNumber.setText(cursor.getInt(
                cursor.getColumnIndexOrThrow(DbContract.ToDoEntry._ID)));
        cursorViewHolder.chtvTask.setText(cursor.getString(
                cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_TASK)));
        cursorViewHolder.chtvTask.setChecked(cursor.getInt(
                cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_COMPLETE))
                == DbContract.ToDoEntry.COMPLETE_CODE);
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
}

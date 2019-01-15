package com.example.maxpayne.mytodoapp.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;

public class CursorAdapter extends SimpleCursorAdapter {
    Cursor cursor;
    TextView tvId;
    CheckedTextView ctvName;

    public CursorAdapter(Context context, Cursor c, String[] from, int[] to, int flags) {
        super(context, R.layout.item, c, from, to, flags);

        this.cursor = c;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        ctvName = view.findViewById(R.id.ctv);
        if (cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_COMPLETE))
                == DbContract.ToDoEntry.COMPLETE_CODE) {
            ctvName.setChecked(true);
        } else {
            ctvName.setChecked(false);
        }
    }

    void printCursor(Cursor cursor) {
        System.out.println("========================");
        //cursor.moveToFirst();;
        //do {
            System.out.println(cursor.getString(cursor.getColumnIndex(DbContract.ToDoEntry._ID)) +
                    " = " + cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_TASK)) +
                    " = " + cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_COMPLETE)));

        //} while (cursor.moveToNext());
    }
}

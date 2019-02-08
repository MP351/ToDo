package com.example.maxpayne.mytodoapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, DbContract.DB_NAME, null, DbContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.ToDoEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.beginTransaction();
            try {
                db.execSQL("ALTER TABLE " + DbContract.ToDoEntry.TABLE_NAME + " ADD COLUMN " +
                        DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " INTEGER DEFAULT "
                        + DbContract.ToDoEntry.NOT_ARCHIVED_CODE + ";");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
}

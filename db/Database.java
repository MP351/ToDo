package com.example.maxpayne.mytodoapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database implements DbObservable{
    private Context context;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase db;

    public Database(Context context) {
        this.context = context;
        sqLiteOpenHelper = new DbHelper(context);
        db = sqLiteOpenHelper.getWritableDatabase();
    }

    //Gets all but cancelled or archived tasks
    public Cursor getAllTasks() {
        String selection = DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " = ?" +
                " AND " + DbContract.ToDoEntry.COLUMN_NAME_COMPLETE + "<> ?";
        String selArgs[] = { String.valueOf(DbContract.ToDoEntry.NOT_ARCHIVED_CODE),
                String.valueOf(DbContract.ToDoEntry.CANCEL_CODE) };

        return db.query(DbContract.ToDoEntry.TABLE_NAME, null,
                selection, selArgs, null, null, null);
    }

    public Cursor getTaskById(long _id) {
        String selection = DbContract.ToDoEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(_id) };
        return db.query(DbContract.ToDoEntry.TABLE_NAME, null, selection, selectionArgs,
                null, null, null);
    }

    //Gets only NOT archived tasks
    public Cursor getTaskByState(int state) {
        String selection = DbContract.ToDoEntry.COLUMN_NAME_COMPLETE + " = ?" +
                " AND " + DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " = ?";
        String[] selArgs = { String.valueOf(state), String.valueOf(DbContract.ToDoEntry.NOT_ARCHIVED_CODE) };
        return db.query(DbContract.ToDoEntry.TABLE_NAME, null, selection, selArgs,
                null, null, null);
    }

    public Cursor getTaskByArchivedState(int state) {
        String selection = DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " = ?";
        String[] selArgs = { String.valueOf(state) };
        return db.query(DbContract.ToDoEntry.TABLE_NAME, null, selection, selArgs,
                null, null, null);
    }

    public long addTask(String task, String description) {
        ContentValues cv = new ContentValues();
        cv.put(DbContract.ToDoEntry.COLUMN_NAME_TASK, task);
        cv.put(DbContract.ToDoEntry.COLUMN_NAME_ADD_DATE, System.currentTimeMillis());
        cv.put(DbContract.ToDoEntry.COLUMN_NAME_COMPLETE, DbContract.ToDoEntry.INCOMPLETE_CODE);
        cv.put(DbContract.ToDoEntry.COLUMN_NAME_DESCRIPTION, description);
        long count = db.insert(DbContract.ToDoEntry.TABLE_NAME, null, cv);
        notifyObservers();
        return count;
    }

    public long completeTask(long id) {
        ContentValues cv = new ContentValues();
        cv.put(DbContract.ToDoEntry.COLUMN_NAME_COMPLETE, DbContract.ToDoEntry.COMPLETE_CODE);
        cv.put(DbContract.ToDoEntry.COLUMN_NAME_END_DATE, System.currentTimeMillis());
        String[] where = { String.valueOf(id) };
        long count = db.update(DbContract.ToDoEntry.TABLE_NAME, cv, DbContract.ToDoEntry._ID + " = ?", where);
        notifyObservers();
        return count;
    }

    public long cancelTask(long id) {
        ContentValues cv = new ContentValues();
        cv.put(DbContract.ToDoEntry.COLUMN_NAME_COMPLETE, DbContract.ToDoEntry.CANCEL_CODE);
        String[] where = { String.valueOf(id) };
        long count = db.update(DbContract.ToDoEntry.TABLE_NAME, cv, DbContract.ToDoEntry._ID + " = ?", where);
        notifyObservers();
        return count;
    }

    public long archiveTask(long id) {
        ContentValues cv = new ContentValues();
        cv.put(DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED, DbContract.ToDoEntry.ARCHIVED_CODE);
        String[] where = { String.valueOf(id) };
        long count = db.update(DbContract.ToDoEntry.TABLE_NAME, cv, DbContract.ToDoEntry._ID + " = ?", where);
        notifyObservers();
        return count;
    }

    @Override
    public boolean addObserver(DbObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeObserver(DbObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void notifyObservers() {
        for (DbObserver observer : observers) {
            observer.notifyObserver();
        }
    }
}

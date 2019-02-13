package com.example.maxpayne.mytodoapp.db.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.maxpayne.mytodoapp.db.DbContract;

@Entity(tableName = DbContract.ToDoEntry.TABLE_NAME)
public class Task {

    public Task(int _id, String task, long add_date, long end_date, int complete, String description, int archived) {
        this._id = _id;
        this.task = task;
        this.add_date = add_date;
        this.end_date = end_date;
        this.complete = complete;
        this.description = description;
        this.archived = archived;
    }

    @Ignore
    public Task(String task, long add_date, long end_date, int complete, String description, int archived) {
        this.task = task;
        this.add_date = add_date;
        this.end_date = end_date;
        this.complete = complete;
        this.description = description;
        this.archived = archived;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DbContract.ToDoEntry._ID)
    public int _id;

    @ColumnInfo(name = DbContract.ToDoEntry.COLUMN_NAME_TASK)
    public String task;

    @ColumnInfo(name = DbContract.ToDoEntry.COLUMN_NAME_ADD_DATE)
    public long add_date;

    @ColumnInfo(name = DbContract.ToDoEntry.COLUMN_NAME_END_DATE)
    public long end_date;

    @ColumnInfo(name = DbContract.ToDoEntry.COLUMN_NAME_COMPLETE)
    public int complete;

    @ColumnInfo(name = DbContract.ToDoEntry.COLUMN_NAME_DESCRIPTION)
    public String description;

    @ColumnInfo(name = DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED)
    public int archived;
}

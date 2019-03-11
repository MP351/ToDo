package com.example.maxpayne.mytodoapp.db.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.maxpayne.mytodoapp.db.DbContract;

import java.util.Objects;

@Entity(tableName = DbContract.ToDoEntry.TABLE_NAME)
public class Task {

    public Task(@NonNull Integer _id, String task, @NonNull Long add_date, @Nullable Long end_date,
                @NonNull Integer complete, String description, @NonNull Integer archived) {
        this._id = _id;
        this.task = task;
        this.add_date = add_date;
        this.end_date = end_date;
        this.complete = complete;
        this.description = description;
        this.archived = archived;
    }

    @Ignore
    public Task(String task, @NonNull Long add_date, @Nullable Long end_date,
                @NonNull Integer complete, String description, @NonNull Integer archived) {
        this.task = task;
        this.add_date = add_date;
        this.end_date = end_date;
        this.complete = complete;
        this.description = description;
        this.archived = archived;
    }

    @Ignore
    public Task(String task, String description) {
        this.task = task;
        this.add_date = System.currentTimeMillis();
        this.complete = DbContract.ToDoEntry.INCOMPLETE_CODE;
        this.description = description;
        this.archived = DbContract.ToDoEntry.NOT_ARCHIVED_CODE;
    }

    @Ignore
    public Task(Task task) {
        this._id = task._id;
        this.task = task.task;
        this.add_date = task.add_date;
        this.end_date = task.end_date;
        this.complete = task.complete;
        this.description = task.description;
        this.archived = task.archived;
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = DbContract.ToDoEntry._ID)
    public Integer _id;

    @ColumnInfo(name = DbContract.ToDoEntry.COLUMN_NAME_TASK)
    public String task;

    @NonNull
    @ColumnInfo(name = DbContract.ToDoEntry.COLUMN_NAME_ADD_DATE)
    public Long add_date;

    @Nullable
    @ColumnInfo(name = DbContract.ToDoEntry.COLUMN_NAME_END_DATE)
    public Long end_date;

    @NonNull
    @ColumnInfo(name = DbContract.ToDoEntry.COLUMN_NAME_COMPLETE)
    public Integer complete;

    @ColumnInfo(name = DbContract.ToDoEntry.COLUMN_NAME_DESCRIPTION)
    public String description;

    @NonNull
    @ColumnInfo(name = DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED)
    public Integer archived;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task1 = (Task) o;
        return Objects.equals(_id, task1._id) &&
                Objects.equals(task, task1.task) &&
                Objects.equals(add_date, task1.add_date) &&
                Objects.equals(end_date, task1.end_date) &&
                Objects.equals(complete, task1.complete) &&
                Objects.equals(description, task1.description) &&
                Objects.equals(archived, task1.archived);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, task, add_date, end_date, complete, description, archived);
    }
}

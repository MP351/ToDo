package com.example.maxpayne.mytodoapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.maxpayne.mytodoapp.db.DbContract;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TaskDao {
    String GET_All_SQL = ("SELECT * FROM " + DbContract.ToDoEntry.TABLE_NAME);
    String GET_ACTIVE_SQL = ("SELECT * FROM " + DbContract.ToDoEntry.TABLE_NAME + " WHERE " +
            DbContract.ToDoEntry.COLUMN_NAME_COMPLETE + " <> " + DbContract.ToDoEntry.CANCEL_CODE +
            " AND " +
            DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " <> " + DbContract.ToDoEntry.ARCHIVED_CODE);

    String GET_NOT_ARCHIVED = "SELECT * FROM " + DbContract.ToDoEntry.TABLE_NAME + " WHERE " +
            DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " = " + DbContract.ToDoEntry.NOT_ARCHIVED_CODE +
            " AND " +
            DbContract.ToDoEntry.COLUMN_NAME_COMPLETE + " = :completeCode";

    String GET_ARCHIVED = "SELECT * FROM " + DbContract.ToDoEntry.TABLE_NAME + " WHERE " +
            DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " = " + DbContract.ToDoEntry.ARCHIVED_CODE;

    String GET_TEMPLATE = "SELECT * FROM " + DbContract.ToDoEntry.TABLE_NAME + " WHERE " +
            DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " = :archivedCode AND " +
            DbContract.ToDoEntry.COLUMN_NAME_COMPLETE + " = :completeCode";

    @Query(GET_All_SQL)
    LiveData<List<Task>> getAllAsLiveData();

    @Query(GET_TEMPLATE)
    LiveData<List<Task>> getTasks(int archivedCode, int completeCode);

    @Query(GET_ACTIVE_SQL)
    LiveData<List<Task>> getActiveTasks();

    @Query(GET_ARCHIVED)
    LiveData<List<Task>> getArchived();

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}

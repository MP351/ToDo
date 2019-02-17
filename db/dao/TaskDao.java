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

    @Query(GET_All_SQL)
    List<Task> getAllAsList();

    @Query(GET_All_SQL)
    LiveData<List<Task>> getAllAsLiveData();
/*
    @Query(GET_All_SQL)
    <T> T getAll(Class<T> returnType);

    @Query(GET_ACTIVE_SQL)
    <T> T getActive(Class<T> returnType);*/

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);


}

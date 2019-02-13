package com.example.maxpayne.mytodoapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.maxpayne.mytodoapp.db.DbContract;

import java.util.List;

@Dao
public interface TaskDao {
    String GET_All_SQL = ("SELECT * FROM " + DbContract.ToDoEntry.TABLE_NAME);

    @Query(GET_All_SQL)
    List<Task> getAllAsList();

    @Query(GET_All_SQL)
    LiveData<List<Task>> getAllAsLiveData();

    @Query(GET_All_SQL)
    <T> T getAll(Class<T> returnType);

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}

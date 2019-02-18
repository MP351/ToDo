package com.example.maxpayne.mytodoapp.recycler_view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.maxpayne.mytodoapp.db.dao.Database;
import com.example.maxpayne.mytodoapp.db.dao.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends ViewModel {
    LiveData<List<Task>> tasks;
    Database db;

    public LiveData<List<Task>> getTasks() {
        if (tasks == null) {
            tasks = db.taskDao().getAllAsLiveData();
        }
        return tasks;
    }

    public void setDb(Database db) {
        this.db = db;
    }


    private void loadData() {

    }


}

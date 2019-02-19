package com.example.maxpayne.mytodoapp.db.dao;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TaskRepository{
    private Database db;

    public TaskRepository(Application application) {
        db = Database.getInstance(application);
    }

    public LiveData<List<Task>> getTasks() {
        return db.taskDao().getAllAsLiveData();
    }

    public void addTask(Task task) {
        new insertAsyncTask(db.taskDao()).execute(task);
    }

    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        insertAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }
}

package com.example.maxpayne.mytodoapp.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Database;
import com.example.maxpayne.mytodoapp.db.dao.Task;
import com.example.maxpayne.mytodoapp.db.dao.TaskDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskRepository{
    private Database db;

    private final int INSERT_CODE = 0;
    private final int DELETE_CODE = 1;
    private final int UPDATE_CODE = 2;


    public TaskRepository(Application application) {
        db = Database.getInstance(application);
    }

    public LiveData<List<Task>> getTasks() {
        return db.taskDao().getAllAsLiveData();
    }

    public LiveData<List<Task>> getActive() {
        return db.taskDao().getActiveTasks();
    }

    public LiveData<List<Task>> getArchived() {
        return db.taskDao().getArchived();
    }

    public LiveData<List<Task>> getTasks(int archiveCode, int completeCode) {
        return db.taskDao().getTasks(archiveCode, completeCode);
    }

    public void addTask(Task task) {
        new asyncDbTask(db.taskDao(), INSERT_CODE).execute(task);
    }

    public void deleteTask(Task task) {
        new asyncDbTask(db.taskDao(), DELETE_CODE).execute(task);
    }

    public void updateTask(Task task) {
        new asyncDbTask(db.taskDao(), UPDATE_CODE).execute(task);
    }

    private static class asyncDbTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        private int code;

        asyncDbTask(TaskDao taskDao, int operCode) {
            this.taskDao = taskDao;
            this.code = operCode;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            switch (code) {
                case 0:
                    taskDao.insert(tasks[0]);
                    break;
                case 1:
                    taskDao.delete(tasks[0]);
                    break;
                case 2:
                    taskDao.update(tasks[0]);
                    break;
            }

            return null;
        }
    }
}

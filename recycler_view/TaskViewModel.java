package com.example.maxpayne.mytodoapp.recycler_view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.maxpayne.mytodoapp.db.dao.Task;
import com.example.maxpayne.mytodoapp.db.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private LiveData<List<Task>> tasks;
    private TaskRepository taskRepository;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        taskRepository = new TaskRepository(application);
        tasks = taskRepository.getTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        taskRepository.addTask(task);
    }

    public void deleteTask(Task task) {
        taskRepository.deleteTask(task);
    }

    public void updateTask(Task task) {
        taskRepository.updateTask(task);
    }
}

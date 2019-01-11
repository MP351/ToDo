package com.example.maxpayne.mytodoapp;

public class Task {
    private int num;
    private String taskName;
    private String taskDescription;
    private boolean isComplete;
    private long startDate;
    private long endDate;

    public Task(int num, String taskName, String taskDescription, boolean isComplete, long startDate, long endDate) {
        this.num = num;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.isComplete = isComplete;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getNum() {
        return num;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }
}

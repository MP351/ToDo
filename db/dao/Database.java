package com.example.maxpayne.mytodoapp.db.dao;

import android.arch.persistence.room.RoomDatabase;

@android.arch.persistence.room.Database(entities = {Task.class}, version = 3)
public abstract class Database extends RoomDatabase {
    private static Database INSTANCE;
    public abstract TaskDao taskDao();
}

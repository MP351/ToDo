package com.example.maxpayne.mytodoapp.db;

import android.provider.BaseColumns;

public class DbContract {
    public static final int DB_VERSION = 3;
    public static final String DB_NAME = "database.db";

    public static class ToDoEntry implements BaseColumns {
        public static final int INCOMPLETE_CODE = 0;
        public static final int COMPLETE_CODE = 1;
        public static final int CANCEL_CODE = 2;

        public static final int NOT_ARCHIVED_CODE = 0;
        public static final int ARCHIVED_CODE = 1;

        public static final String TABLE_NAME = "todo";
        public static final String COLUMN_NAME_TASK = "task";
        public static final String COLUMN_NAME_ADD_DATE = "add_date";
        public static final String COLUMN_NAME_END_DATE = "end_date";
        public static final String COLUMN_NAME_COMPLETE = "complete";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_ARCHIVED = "archived";


        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY NOT NULL," +
                COLUMN_NAME_TASK + " TEXT," +
                COLUMN_NAME_ADD_DATE + " INTEGER NOT NULL," +
                COLUMN_NAME_END_DATE + " INTEGER," +
                COLUMN_NAME_COMPLETE + " INTEGER NOT NULL," +
                COLUMN_NAME_DESCRIPTION + " TEXT," +
                COLUMN_NAME_ARCHIVED + " INTEGER NOT NULL" + ")";

        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
                TABLE_NAME;
    }
}

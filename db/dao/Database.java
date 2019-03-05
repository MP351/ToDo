package com.example.maxpayne.mytodoapp.db.dao;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.maxpayne.mytodoapp.db.DbContract;

@android.arch.persistence.room.Database(entities = {Task.class}, version = DbContract.DB_VERSION)
public abstract class Database extends RoomDatabase {
    private static Database INSTANCE;
    public abstract TaskDao taskDao();


    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            final String _old = "_old";
            try {
                database.beginTransaction();

                //Dev device migration error correction
                /*
                database.execSQL("ALTER TABLE " + DbContract.ToDoEntry.TABLE_NAME +
                        " RENAME TO " + DbContract.ToDoEntry.TABLE_NAME + _old);
                database.execSQL(DbContract.ToDoEntry.SQL_CREATE_ENTRIES);
                database.execSQL("INSERT INTO todo(_id,task,add_date,end_date,complete,description,archived) SELECT _id,task,add_date,end_date,complete,description,archivedINTEGER from todo_old;");
                database.execSQL("DROP TABLE todo_old");*/


                database.execSQL("UPDATE " + DbContract.ToDoEntry.TABLE_NAME +
                        " SET " + DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + "=0" +
                        " WHERE " + DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " IS NULL");
                database.execSQL("ALTER TABLE " + DbContract.ToDoEntry.TABLE_NAME +
                        " RENAME TO " + DbContract.ToDoEntry.TABLE_NAME + _old);
                database.execSQL(DbContract.ToDoEntry.SQL_CREATE_ENTRIES);
                database.execSQL("INSERT INTO " + DbContract.ToDoEntry.TABLE_NAME +
                        " SELECT * FROM " + DbContract.ToDoEntry.TABLE_NAME + _old);

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }

        }
    };

    public static Database getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, DbContract.DB_NAME)
                    .addMigrations(MIGRATION_2_3)
                    .build();
        }
        return INSTANCE;
    }
}

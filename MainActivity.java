package com.example.maxpayne.mytodoapp;

import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.example.maxpayne.mytodoapp.db.Database;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.ui.CursorAdapter;

public class MainActivity extends AppCompatActivity implements AddDialog.NoticeDialogListener, DetailTaskDialog.NoticeDialogListener {
    ListView rv;
    Database db;
    final int OPTIONS_MENU_ADD = 0;
    CursorAdapter sca;
    Cursor allTasks;
    FloatingActionButton fab;
    Toolbar myTb;

    final int MENU_ADD_CODE = 0;
    final int MENU_ARCHIVE_CODE = 1;
    final int MENU_CANCEL_CODE = 2;
    final int MENU_COMPLETE_CODE = 3;

    private int selectionCode = 0;
    final int SELECTION_ALL_CODE = 0;
    final int SELECTION_INCOMPLETE_CODE = 1;
    final int SELECTION_COMPLETE_CODE = 2;
    final int SELECTION_CANCEL_CODE = 3;
    final int SELECTION_ARCHIVED_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.mipmap.icon_launcher);

        db = new Database(this);
        rv = findViewById(R.id.lv);
        fab = findViewById(R.id.fab);
        myTb = findViewById(R.id.tb);

        myTb.setNavigationIcon(R.mipmap.icon_launcher);
        setSupportActionBar(myTb);

        String[] from = { DbContract.ToDoEntry._ID, DbContract.ToDoEntry.COLUMN_NAME_TASK };
        int[] to = { R.id.tv, R.id.ctv };

        allTasks = db.getAllTasks();

        sca = new CursorAdapter(this, allTasks, from, to, 1);
        rv.addHeaderView(getLayoutInflater().inflate(R.layout.header, null));
        rv.setAdapter(sca);

        registerForContextMenu(rv);

        rv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    DetailTaskDialog dtd = new DetailTaskDialog();
                    Cursor cursor = db.getTaskById(id);
                    printCursor(cursor);
                    cursor.moveToFirst();
                    dtd.set_id(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry._ID)));
                    dtd.setTaskHead(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_TASK)));
                    dtd.setAddDate(cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_ADD_DATE)));
                    dtd.setEndDate(cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_END_DATE)));
                    dtd.setTaskDescription(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_DESCRIPTION)));
                    dtd.show(getSupportFragmentManager(), "detailDialog");
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddingDialog();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, MENU_ADD_CODE, 0, R.string.add_dialog_head);
        AdapterView.AdapterContextMenuInfo info
                = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Cursor cursor = db.getTaskById(info.id);
        cursor.moveToFirst();
        boolean archived = cursor.getInt(cursor.getColumnIndexOrThrow(
                DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED)) == DbContract.ToDoEntry.ARCHIVED_CODE;
        boolean cancelled = cursor.getInt(cursor.getColumnIndexOrThrow(
                DbContract.ToDoEntry.COLUMN_NAME_COMPLETE)) == DbContract.ToDoEntry.CANCEL_CODE;
        boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(
                DbContract.ToDoEntry.COLUMN_NAME_COMPLETE)) == DbContract.ToDoEntry.COMPLETE_CODE;

        if (archived)
            return;
        if (cancelled || completed)
            menu.add(0, MENU_ARCHIVE_CODE, 0, R.string.to_archive);
        if (!completed && !cancelled && !archived) {
            menu.add(0, MENU_COMPLETE_CODE, 0, R.string.complete_task);
            menu.add(0, MENU_CANCEL_CODE, 0, R.string.to_cancelled);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info
                = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case MENU_ADD_CODE:
                openAddingDialog();
                break;
            case MENU_ARCHIVE_CODE:
                db.archiveTask(info.id);
                renewList();
                break;
            case MENU_CANCEL_CODE:
                db.cancelTask(info.id);
                renewList();
                break;
            case MENU_COMPLETE_CODE:
                db.completeTask(info.id);
                renewList();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final String[] array = getResources().getStringArray(R.array.groups);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                array);

        AppCompatSpinner spinner = findViewById(R.id.ab_menu_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectionCode = SELECTION_ALL_CODE;
                        renewList();
                        break;
                    case 1:
                        selectionCode = SELECTION_INCOMPLETE_CODE;
                        renewList();
                        break;
                    case 2:
                        selectionCode = SELECTION_COMPLETE_CODE;
                        renewList();
                        break;
                    case 3:
                        selectionCode = SELECTION_CANCEL_CODE;
                        renewList();
                        break;
                    case 4:
                        selectionCode = SELECTION_ARCHIVED_CODE;
                        renewList();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case OPTIONS_MENU_ADD:
                openAddingDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(String taskName, String description) {
        db.addTask(taskName, description);
        renewList();
    }

    @Override
    public void closeTask(int id) {
        db.completeTask(id);
        renewList();
    }

    void printCursor(Cursor cursor) {
        System.out.println("========================");
        cursor.moveToFirst();;
        do {
            System.out.println(cursor.getString(cursor.getColumnIndex(DbContract.ToDoEntry._ID)) +
                " = " + cursor.getString(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_TASK)) +
                " = " + cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.ToDoEntry.COLUMN_NAME_COMPLETE)));

        } while (cursor.moveToNext());
    }

    public void openAddingDialog() {
        AddDialog addDialog = new AddDialog();
        addDialog.show(getSupportFragmentManager(), "AddDiag");
    }

    public void renewList() {
        switch (selectionCode) {
            case SELECTION_ALL_CODE:
                sca.changeCursor(db.getAllTasks());
                break;
            case SELECTION_INCOMPLETE_CODE:
                sca.changeCursor(db.getTaskByState(DbContract.ToDoEntry.INCOMPLETE_CODE));
                break;
            case SELECTION_COMPLETE_CODE:
                sca.changeCursor(db.getTaskByState(DbContract.ToDoEntry.COMPLETE_CODE));
                break;
            case SELECTION_CANCEL_CODE:
                sca.changeCursor(db.getTaskByState(DbContract.ToDoEntry.CANCEL_CODE));
                break;
            case SELECTION_ARCHIVED_CODE:
                sca.changeCursor(db.getTaskByArchivedState(DbContract.ToDoEntry.ARCHIVED_CODE));
                break;

        }
    }
}

package com.example.maxpayne.mytodoapp.recycler_view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.maxpayne.mytodoapp.AddDialog;
import com.example.maxpayne.mytodoapp.DetailTaskDialog;
import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.Database;
import com.example.maxpayne.mytodoapp.db.DbContract;

public class RVMainActivity extends AppCompatActivity implements AddDialog.NoticeDialogListener, DetailTaskDialog.NoticeDialogListener {
    RecyclerView rv;
    Database db;
    CursorRecyclerViewAdapter cra;
    final int OPTIONS_MENU_ADD = 0;
    Cursor cursor;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rv);

        db = new Database(this);
        rv = findViewById(R.id.rvList);
        myTb = findViewById(R.id.RvTb);
        fab = findViewById(R.id.RvFab);

        cra = new CursorRecyclerViewAdapter(this, db.getAllTasks(), getSupportFragmentManager());
        myTb.setNavigationIcon(R.mipmap.icon_launcher);
        setSupportActionBar(myTb);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(cra);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddingDialog();
            }
        });

        registerForContextMenu(rv);
    }

    public void openAddingDialog() {
        AddDialog addDialog = new AddDialog();
        addDialog.show(getSupportFragmentManager(), "AddDiag");
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

    public void renewList() {
        switch (selectionCode) {
            case SELECTION_ALL_CODE:
                cra.changeCursor(db.getAllTasks());
                break;
            case SELECTION_INCOMPLETE_CODE:
                cra.changeCursor(db.getTaskByState(DbContract.ToDoEntry.INCOMPLETE_CODE));
                break;
            case SELECTION_COMPLETE_CODE:
                cra.changeCursor(db.getTaskByState(DbContract.ToDoEntry.COMPLETE_CODE));
                break;
            case SELECTION_CANCEL_CODE:
                cra.changeCursor(db.getTaskByState(DbContract.ToDoEntry.CANCEL_CODE));
                break;
            case SELECTION_ARCHIVED_CODE:
                cra.changeCursor(db.getTaskByArchivedState(DbContract.ToDoEntry.ARCHIVED_CODE));
                break;

        }
    }
}

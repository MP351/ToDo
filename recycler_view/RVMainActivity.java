package com.example.maxpayne.mytodoapp.recycler_view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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

public class RVMainActivity extends AppCompatActivity implements AddDialog.NoticeDialogListener,
        DetailTaskDialog.NoticeDialogListener {
    Database db;
    RecyclerView rv;
    CursorRecyclerViewAdapter cra;
    final int OPTIONS_MENU_ADD = 0;
    FloatingActionButton fab;
    Toolbar myTb;

    private int selectionCode = 0;
    private final int SELECTION_ALL_CODE = 0;
    private final int SELECTION_INCOMPLETE_CODE = 1;
    private final int SELECTION_COMPLETE_CODE = 2;
    private final int SELECTION_CANCEL_CODE = 3;
    private final int SELECTION_ARCHIVED_CODE = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rv);

        db = new Database(this);

        rv = findViewById(R.id.rvList);
        myTb = findViewById(R.id.RvTb);
        fab = findViewById(R.id.RvFab);

        cra = new CursorRecyclerViewAdapter(this, db, getSupportFragmentManager());
        db.addObserver(cra);

        myTb.setNavigationIcon(R.mipmap.icon_launcher);
        setSupportActionBar(myTb);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(cra);

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(cra));
        touchHelper.attachToRecyclerView(rv);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddingDialog();
            }
        });
    }

    public void openAddingDialog() {
        AddDialog addDialog = new AddDialog();
        addDialog.show(getSupportFragmentManager(), "AddDiag");
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
                selectionCode = position;
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
    }

    @Override
    public void closeTask(int id) {
        db.completeTask(id);
    }
}

package com.example.maxpayne.mytodoapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.maxpayne.mytodoapp.recycler_view.ItemTouchHelperCallback;
import com.example.maxpayne.mytodoapp.recycler_view.ListRecyclerViewAdapter;
import com.example.maxpayne.mytodoapp.recycler_view.TaskViewModel;
import com.example.maxpayne.mytodoapp.ui.AddDialog;
import com.example.maxpayne.mytodoapp.ui.DetailTaskDialog;
import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Task;

public class MainActivity extends AppCompatActivity implements AddDialog.NoticeDialogListener,
        DetailTaskDialog.NoticeDialogListener, ListRecyclerViewAdapter.dbWorkListener, ListRecyclerViewAdapter.swipeListener {
    RecyclerView rv;
    ListRecyclerViewAdapter lrva;
    FloatingActionButton fab;
    Toolbar myTb;
    TaskViewModel tvm;
    ItemTouchHelperCallback ithc;
    ConstraintLayout cl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rv);
        initViews();

        myTb.setNavigationIcon(R.mipmap.icon_launcher);
        setSupportActionBar(myTb);

        ithc = new ItemTouchHelperCallback(lrva);
        ItemTouchHelper touchHelper = new ItemTouchHelper(ithc);
        touchHelper.attachToRecyclerView(rv);

        tvm = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(TaskViewModel.class);

        tvm.getTasks().observe(this, (tasks) -> lrva.setData(tasks));


        fab.setOnClickListener((view -> openAddingDialog()));
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
                //TODO:
                lrva.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDialogPositiveClick(String taskName, String description) {
        tvm.addTask(new Task(taskName, description));
    }

    @Override
    public void closeTask(Task task) {
        task.end_date = System.currentTimeMillis();
        task.complete = DbContract.ToDoEntry.COMPLETE_CODE;
        tvm.updateTask(task);
    }

    void initViews() {
        rv = findViewById(R.id.rvList);
        myTb = findViewById(R.id.RvTb);
        fab = findViewById(R.id.RvFab);
        cl = findViewById(R.id.cl_main);

        lrva = new ListRecyclerViewAdapter(this, getSupportFragmentManager());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(lrva);
    }

    @Override
    public void deleteTask(Task task) {
        tvm.deleteTask(task);
    }

    @Override
    public void updateTask(Task task) {
        tvm.updateTask(task);
    }

    @Override
    public void archiveOrCancelTask(Task task, int code) {
        StringBuilder sb = new StringBuilder("Task ");
        Snackbar snb;
        if (code == lrva.ACTION_CODE_CANCEL) {
            snb = Snackbar.make(cl, sb.append(" cancelled.").toString(), Snackbar.LENGTH_SHORT);
            snb.setDuration(3000);
            updateTask(task);
        } else {
            snb = Snackbar.make(cl, sb.append(" archived.").toString(), Snackbar.LENGTH_SHORT);
            snb.setDuration(3000);
            updateTask(task);
        }


        snb.setAction("UNDO", (view -> {
            if (code == lrva.ACTION_CODE_CANCEL) {
                task.complete = DbContract.ToDoEntry.INCOMPLETE_CODE;
                updateTask(task);
            } else {
                task.archived = DbContract.ToDoEntry.NOT_ARCHIVED_CODE;
                updateTask(task);
            }
            lrva.notifyDataSetChanged();
        }));
        snb.show();
    }

    @Override
    public void setSwapEnable(boolean enabled) {
        ithc.setSwipeEnabled(enabled);
    }
}

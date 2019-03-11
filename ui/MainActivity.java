package com.example.maxpayne.mytodoapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;

import com.example.maxpayne.mytodoapp.recycler_view.ItemTouchHelperCallback;
import com.example.maxpayne.mytodoapp.recycler_view.ListRecyclerViewAdapter;
import com.example.maxpayne.mytodoapp.recycler_view.TaskViewModel;
import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Task;

public class MainActivity extends AppCompatActivity implements AddDialog.NoticeDialogListener,
        DetailTaskDialog.NoticeDialogListener, ListRecyclerViewAdapter.dbWorkListener {
    RecyclerView rv;
    ListRecyclerViewAdapter lrva;
    FloatingActionButton fab;
    Toolbar myTb;
    TaskViewModel tvm;
    ItemTouchHelperCallback ithc;
    ConstraintLayout cl;
    DrawerLayout dl;
    AppCompatActivity activity = this;
    Integer lastNavAction;
    final String LAST_NAV_ACTION_CODE = "LAST_ACTION";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rv);
        initViews();

        ithc = new ItemTouchHelperCallback(lrva);
        ItemTouchHelper touchHelper = new ItemTouchHelper(ithc);
        touchHelper.attachToRecyclerView(rv);

        tvm = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(TaskViewModel.class);

        fab.setOnClickListener((view -> openAddingDialog()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryData(lastNavAction == null ? R.id.nav_active : lastNavAction);

        tvm.getTasks().observe(this, (tasks) -> lrva.setData(tasks));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openAddingDialog() {
        AddDialog addDialog = new AddDialog();
        addDialog.show(getSupportFragmentManager(), "AddDiag");
    }

    @Override
    public void onDialogPositiveClick(String taskName, String description) {
        tvm.addTask(new Task(taskName, description));
    }

    @Override
    public void closeTask(Task task) {
        Task t1 = new Task(task);
        t1.end_date = System.currentTimeMillis();
        t1.complete = DbContract.ToDoEntry.COMPLETE_CODE;
        tvm.updateTask(t1);
    }

    private void initViews() {
        rv = findViewById(R.id.rvList);
        myTb = findViewById(R.id.RvTb);
        fab = findViewById(R.id.RvFab);
        cl = findViewById(R.id.cl_main);
        dl = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener((menuItem -> {
            menuItem.setChecked(true);
            queryData(menuItem.getItemId());

            dl.closeDrawers();
            return true;
        }));

        lrva = new ListRecyclerViewAdapter(this, getSupportFragmentManager());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(lrva);

        setSupportActionBar(myTb);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    private void queryData(Integer action) {
        tvm.getTasks().removeObservers(activity);
        lastNavAction = action;
        switch (action) {
            case R.id.nav_active:
                tvm.queryActive();
                ithc.setSwipeEnabled(true);
                break;
            case R.id.nav_incomplete:
                tvm.queryIncomplete();
                ithc.setSwipeEnabled(true);
                break;
            case R.id.nav_complete:
                tvm.queryComplete();
                ithc.setSwipeEnabled(false);
                break;
            case R.id.nav_cancelled:
                tvm.queryCancelled();
                ithc.setSwipeEnabled(false);
                break;
            case R.id.nav_archive:
                tvm.queryArchived();
                ithc.setSwipeEnabled(false);
                break;
        }

        tvm.getTasks().observe(this, tasks -> lrva.setData(tasks));
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
        Snackbar snb;
        if (code == lrva.ACTION_CODE_CANCEL) {
            snb = Snackbar.make(cl, R.string.task_cancelled, Snackbar.LENGTH_SHORT);
            snb.setDuration(3000);
            updateTask(task);
        } else {
            snb = Snackbar.make(cl, R.string.task_arcvhived, Snackbar.LENGTH_SHORT);
            snb.setDuration(3000);
            updateTask(task);
        }


        snb.setAction(R.string.undo, (view -> {
            if (code == lrva.ACTION_CODE_CANCEL) {
                task.complete = DbContract.ToDoEntry.INCOMPLETE_CODE;
                updateTask(task);
            } else {
                task.archived = DbContract.ToDoEntry.NOT_ARCHIVED_CODE;
                updateTask(task);
            }
        }));
        snb.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAST_NAV_ACTION_CODE, lastNavAction);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastNavAction = savedInstanceState.getInt(LAST_NAV_ACTION_CODE);
    }
}

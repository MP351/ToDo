package com.example.maxpayne.mytodoapp.recycler_view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.maxpayne.mytodoapp.AddDialog;
import com.example.maxpayne.mytodoapp.DetailTaskDialog;
import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.dao.Database;
import com.example.maxpayne.mytodoapp.db.dao.Task;

import java.util.List;
import java.util.concurrent.Executor;

public class RVMainActivity extends AppCompatActivity implements AddDialog.NoticeDialogListener,
        DetailTaskDialog.NoticeDialogListener {
    RecyclerView rv;
    ListRecyclerViewAdapter lrva;
    CursorRecyclerViewAdapter cra;
    final int OPTIONS_MENU_ADD = 0;
    FloatingActionButton fab;
    Toolbar myTb;
    TaskViewModel tvm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rv);

        rv = findViewById(R.id.rvList);
        myTb = findViewById(R.id.RvTb);
        fab = findViewById(R.id.RvFab);

        /*
        cra = new CursorRecyclerViewAdapter(this, getSupportFragmentManager());
        myTb.setNavigationIcon(R.mipmap.icon_launcher);
        setSupportActionBar(myTb);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(cra);

        ItemTouchHelperCallback ithc = new ItemTouchHelperCallback(cra);
        ItemTouchHelper touchHelper = new ItemTouchHelper(ithc);
        touchHelper.attachToRecyclerView(rv);*/

        lrva = new ListRecyclerViewAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(lrva);

        tvm = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(TaskViewModel.class);

        tvm.getTasks().observe(this, (tasks) -> lrva.setData(tasks));


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
                cra.setSelectionCode(position);
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
        //cra.addTask(taskName, description);
        tvm.addTask(new Task(taskName, description));
    }

    @Override
    public void closeTask(int id) {
        cra.completeTask(id);
    }
}

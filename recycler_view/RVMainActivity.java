package com.example.maxpayne.mytodoapp.recycler_view;

import android.support.v7.app.AppCompatActivity;

import com.example.maxpayne.mytodoapp.AddDialog;
import com.example.maxpayne.mytodoapp.DetailTaskDialog;

public class RVMainActivity extends AppCompatActivity implements AddDialog.NoticeDialogListener, DetailTaskDialog.NoticeDialogListener {
    @Override
    public void onDialogPositiveClick(String taskName, String description) {

    }

    @Override
    public void closeTask(int id) {

    }
}

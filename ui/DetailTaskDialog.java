package com.example.maxpayne.mytodoapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.dao.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailTaskDialog extends DialogFragment {
    Task task;
    private TextView tvTask;
    private TextView tvAddDate;
    private TextView tvEndDate;
    private TextView tvDescription;
    View view;
    NoticeDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        prepareDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.detail_head)
                .setView(view)
                .setNeutralButton(R.string.close, (dialogInterface, i) -> dismiss());

        return builder.create();
    }

    public void setTask(Task task) {
        this.task = task;
    }

    private void prepareDialog() {
        view = getActivity().getLayoutInflater().inflate(R.layout.detail_task, null);
        tvTask = view.findViewById(R.id.tvTaskName);
        tvAddDate = view.findViewById(R.id.tvAddDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        tvDescription = view.findViewById(R.id.tvDescription);

        tvTask.setText(task.task);
        tvDescription.setText(task.description);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        tvAddDate.setText(sdf.format(new Date(task.add_date)));
        if (task.end_date != null) {
            tvEndDate.setText(sdf.format(new Date(task.end_date)));
            tvEndDate.setTextColor(Color.BLACK);
        } else {
            tvEndDate.setText(R.string.close_task);
            tvEndDate.setTextColor(Color.BLUE);
        }

        tvEndDate.setOnClickListener(view -> {
                mListener.closeTask(task);
                dismiss();
            });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (NoticeDialogListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    public interface NoticeDialogListener {
        void closeTask(Task task);
    }
}

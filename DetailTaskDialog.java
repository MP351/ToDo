package com.example.maxpayne.mytodoapp;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailTaskDialog extends DialogFragment {
    int _id;
    long addDate;
    long endDate;
    String taskHead;
    String taskDescription;
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
                .setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setAddDate(long addDate) {
        this.addDate = addDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public void setTaskHead(String taskHead) {
        this.taskHead = taskHead;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    private void prepareDialog() {
        view = getActivity().getLayoutInflater().inflate(R.layout.detail_task, null);
        tvTask = view.findViewById(R.id.tvTaskName);
        tvAddDate = view.findViewById(R.id.tvAddDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        tvDescription = view.findViewById(R.id.tvDescription);

        tvTask.setText(taskHead);
        tvDescription.setText(taskDescription);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        tvAddDate.setText(sdf.format(new Date(addDate)));
        if (endDate > 0) {
            tvEndDate.setText(sdf.format(new Date(endDate)));
            tvEndDate.setTextColor(Color.BLACK);
        } else {
            tvEndDate.setText(R.string.close_task);
            tvEndDate.setTextColor(Color.BLUE);
        }

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.closeTask(_id);
                dismiss();
            }
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
        void closeTask(int id);
    }
}

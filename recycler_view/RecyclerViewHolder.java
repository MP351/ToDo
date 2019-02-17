package com.example.maxpayne.mytodoapp.recycler_view;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.maxpayne.mytodoapp.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;
        CheckedTextView chtvTask;

        TextView tvUdo;
        Button btnUdo;

        ConstraintLayout viewBackground;
        CardView viewForeground;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumber = itemView.findViewById(R.id.tv);
            chtvTask = itemView.findViewById(R.id.ctv);

            tvUdo = itemView.findViewById(R.id.tvUdo);
            btnUdo = itemView.findViewById(R.id.btnUdo);

            viewForeground = itemView.findViewById(R.id.view_foreground);
            viewBackground = itemView.findViewById(R.id.view_background);
        }
}

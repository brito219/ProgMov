package com.example.ToDo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ToDo.R;
import com.example.ToDo.entity.Task;

import java.text.SimpleDateFormat;
import java.util.*;

public class TaskSectionAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_TASK   = 1;

    // item genérico: pode ser String (header) ou Task
    private final List<Object> items;
    private final SimpleDateFormat dtFmt =
            new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private final OnTaskClickListener listener;

    // Novo construtor recebe listener
    public TaskSectionAdapter(List<Object> items,
                              OnTaskClickListener listener) {
        this.items    = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int pos) {
        return items.get(pos) instanceof String
                ? TYPE_HEADER : TYPE_TASK;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {

        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            View v = inf.inflate(R.layout.item_section_header, parent, false);
            return new SectionHolder(v);
        } else {
            View v = inf.inflate(R.layout.item_task, parent, false);
            return new TaskHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int pos) {
        Object o = items.get(pos);
        if (vh instanceof SectionHolder) {
            ((SectionHolder) vh).tvSection.setText((String)o);
        } else {
            Task t = (Task)o;
            TaskHolder th = (TaskHolder) vh;
            th.tvTitle.setText(t.getTitle());
            th.tvDateTime.setText(dtFmt.format(
                    new Date(t.getDateTime())
            ));
            // clique abre detalhe via callback
            th.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaskClick(t);
                }
            });
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class SectionHolder extends RecyclerView.ViewHolder {
        final TextView tvSection;
        SectionHolder(View v) {
            super(v);
            tvSection = v.findViewById(R.id.tvSection);
        }
    }

    static class TaskHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvDateTime;
        TaskHolder(View v) {
            super(v);
            tvDateTime = v.findViewById(R.id.tvDateTime);
            tvTitle    = v.findViewById(R.id.tvTitle);
        }
    }
}

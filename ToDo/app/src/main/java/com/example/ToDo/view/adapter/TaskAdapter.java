package com.example.ToDo.view.adapter;

import android.graphics.Color;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ToDo.R;
import com.example.ToDo.entity.Task;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.*;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> lista = new ArrayList<>();

    public void atualizar(List<Task> novaLista) {
        lista.clear();
        lista.addAll(novaLista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarefa, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int pos) {
        Task t = lista.get(pos);
        holder.txtTitulo.setText(t.getTitle());

        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
        holder.txtHora.setText(fmt.format(new Date(t.getDateTime())));

        // 🎨 Aplica cor de fundo com base na prioridade
        String prioridade = t.getPriority() != null ? t.getPriority().toLowerCase(Locale.ROOT) : "";
        int cor;

        switch (prioridade) {
            case "alta":
                cor = Color.parseColor("#FFCDD2"); // vermelho claro
                break;
            case "média":
            case "media":
                cor = Color.parseColor("#FFF9C4"); // amarelo claro
                break;
            case "baixa":
                cor = Color.parseColor("#C8E6C9"); // verde claro
                break;
            default:
                cor = Color.LTGRAY;
        }

        holder.container.setCardBackgroundColor(cor);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtHora;
        MaterialCardView container;

        public TaskViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtHora = itemView.findViewById(R.id.txtHora);
            container = itemView.findViewById(R.id.containerTarefa);
        }
    }
}
package com.example.ToDo.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ToDo.R;
import com.example.ToDo.database.AppDatabase;
import com.example.ToDo.entity.Task;
import com.example.ToDo.view.TaskDetailActivity;
import com.example.ToDo.view.adapter.TaskSectionAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskListFragment extends Fragment {

    private static final int REQ_EDIT = 500;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private final ExecutorService exe = Executors.newSingleThreadExecutor();

    @Nullable @Override
    public View onCreateView(
            @NonNull LayoutInflater inf, ViewGroup c, Bundle b
    ) {
        View v = inf.inflate(R.layout.fragment_task_list, c, false);
        rv  = v.findViewById(R.id.rvTasks);
        fab = v.findViewById(R.id.fabAdd);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        fab.setOnClickListener(x -> startForEdit(null));

        return v;
    }

    private void startForEdit(Task t) {
        Intent i = new Intent(getContext(), TaskDetailActivity.class);
        if (t != null) i.putExtra("task", t);
        startActivityForResult(i, REQ_EDIT);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAndDisplay();
    }

    @Override
    public void onActivityResult(
            int req, int res, @Nullable Intent data
    ) {
        super.onActivityResult(req, res, data);
        if (req == REQ_EDIT && res == Activity.RESULT_OK) {
            loadAndDisplay();
        }
    }

    private void loadAndDisplay() {
        exe.execute(() -> {
            long now = System.currentTimeMillis();
            List<Task> all = AppDatabase
                    .getInstance(requireContext())
                    .taskDao()
                    .getAll();

            // filtra não completos
            List<Task> pendentes = new ArrayList<>();
            for (Task t : all) {
                if (!t.isCompleted()) pendentes.add(t);
            }

            // separa vencidas e futuras
            List<Task> venc = new ArrayList<>();
            List<Task> prox = new ArrayList<>();
            for (Task t : pendentes) {
                if (t.getDateTime() < now) venc.add(t);
                else prox.add(t);
            }

            // ordena futuras por data
            Collections.sort(prox, Comparator.comparingLong(Task::getDateTime));

            // prepara lista de itens (String header ou Task)
            List<Object> sectionItems = new ArrayList<>();

            if (!venc.isEmpty()) {
                sectionItems.add("Tarefas Vencidas");
                sectionItems.addAll(venc);
            }

            if (!prox.isEmpty()) {
                sectionItems.add("Próximas Tarefas");
                // agrupa por dia
                String lastDate = "";
                for (Task t : prox) {
                    String dia = new java.text.SimpleDateFormat(
                            "dd/MM/yyyy", Locale.getDefault()
                    ).format(new Date(t.getDateTime()));
                    if (!dia.equals(lastDate)) {
                        lastDate = dia;
                        sectionItems.add(dia);
                    }
                    sectionItems.add(t);
                }
            }

            requireActivity().runOnUiThread(() -> {
                // aqui passamos o listener que abre o detail para edição
                TaskSectionAdapter adapter =
                        new TaskSectionAdapter(
                                sectionItems,
                                task -> startForEdit(task)
                        );
                rv.setAdapter(adapter);
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        exe.shutdownNow();
    }
}

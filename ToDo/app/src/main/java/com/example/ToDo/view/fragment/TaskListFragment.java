package com.example.ToDo.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
    private Spinner spinnerFilter;
    private final ExecutorService exe = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inf, ViewGroup c, Bundle b
    ) {
        View v = inf.inflate(R.layout.fragment_task_list, c, false);
        rv  = v.findViewById(R.id.rvTasks);
        fab = v.findViewById(R.id.fabAdd);
        spinnerFilter = v.findViewById(R.id.spinnerTaskFilter);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        fab.setOnClickListener(x -> startForEdit(null));

        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Todas", "Atrasadas", "Próximas", "Concluídas", "Favoritas"}
        );
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(filterAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadAndDisplay(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

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
        loadAndDisplay(spinnerFilter.getSelectedItemPosition());
    }

    @Override
    public void onActivityResult(
            int req, int res, @Nullable Intent data
    ) {
        super.onActivityResult(req, res, data);
        if (req == REQ_EDIT && res == Activity.RESULT_OK) {
            loadAndDisplay(spinnerFilter.getSelectedItemPosition());
        }
    }

    private void loadAndDisplay(int filter) {
        exe.execute(() -> {
            long userId = requireContext()
                    .getSharedPreferences("APP_PREF", requireContext().MODE_PRIVATE)
                    .getLong("USER_ID_LOGADO", -1);
            if (userId == -1) return;

            long now = System.currentTimeMillis();
            List<Object> resultado = new ArrayList<>();

            switch (filter) {
                case 1: // Atrasadas
                    for (Task t : AppDatabase.getInstance(requireContext()).taskDao().getPending(userId)) {
                        if (t.getDateTime() < now) resultado.add(t);
                    }
                    break;
                case 2: // Próximas
                    for (Task t : AppDatabase.getInstance(requireContext()).taskDao().getPending(userId)) {
                        if (t.getDateTime() >= now) resultado.add(t);
                    }
                    break;
                case 3: // Concluídas
                    resultado.addAll(AppDatabase.getInstance(requireContext()).taskDao().getCompleted(userId));
                    break;
                case 4: // Favoritas
                    resultado.addAll(AppDatabase.getInstance(requireContext()).taskDao().getFavorites(userId));
                    break;
                default: // Todas (pendentes, separadas por seção)
                    List<Task> pendentes = AppDatabase.getInstance(requireContext()).taskDao().getPending(userId);
                    List<Task> atrasadas = new ArrayList<>();
                    List<Task> proximas  = new ArrayList<>();

                    for (Task t : pendentes) {
                        if (t.getDateTime() < now) atrasadas.add(t);
                        else proximas.add(t);
                    }

                    if (!atrasadas.isEmpty()) {
                        resultado.add("Atrasadas");
                        resultado.addAll(atrasadas);
                    }
                    if (!proximas.isEmpty()) {
                        resultado.add("Próximas");
                        resultado.addAll(proximas);
                    }
            }

            requireActivity().runOnUiThread(() -> {
                TaskSectionAdapter adapter = new TaskSectionAdapter(resultado, this::startForEdit);
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
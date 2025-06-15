package com.example.ToDo.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ToDo.R;
import com.example.ToDo.database.AppDatabase;
import com.example.ToDo.entity.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FiltersFragment extends Fragment {

    private Spinner spinnerFilter;
    private ListView listFiltered;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private List<Task> tasks = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout do fragment de filtros
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerFilter = view.findViewById(R.id.spinnerFilter);
        listFiltered = view.findViewById(R.id.listFiltered);

        // Adapter do Spinner
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Todas", "Pendentes", "Favoritas"}
        );
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(filterAdapter);

        // Define seleção inicial como “Todas”
        spinnerFilter.setSelection(0);

        // Listener de seleção para aplicar filtros
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View itemView,
                                       int position,
                                       long id) {
                applyFilter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não faz nada
            }
        });
    }

    private void applyFilter(int position) {
        executor.execute(() -> {
            switch (position) {
                case 1:
                    tasks = AppDatabase.getInstance(requireContext())
                            .taskDao()
                            .getPending();
                    break;
                case 2:
                    tasks = AppDatabase.getInstance(requireContext())
                            .taskDao()
                            .getFavorites();
                    break;
                default:
                    tasks = AppDatabase.getInstance(requireContext())
                            .taskDao()
                            .getAll();
                    break;
            }

            requireActivity().runOnUiThread(() -> {
                // Popula a ListView com as tarefas filtradas
                ArrayAdapter<Task> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        tasks
                );
                listFiltered.setAdapter(adapter);
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        executor.shutdownNow();
    }
}

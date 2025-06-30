package com.example.ToDo.view.fragment;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.ToDo.R;
import com.example.ToDo.view.adapter.TaskAdapter;
import com.example.ToDo.database.AppDatabase;
import com.example.ToDo.entity.Task;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;

public class FiltersFragment extends Fragment {

    private TextView txtSelectedDate, txtSemTarefas, txtTituloPendentes, txtTituloConcluidas;
    private CalendarView calendarView;
    private RecyclerView recyclerPendentes, recyclerConcluidas;
    private TaskAdapter adapterPendentes = new TaskAdapter();
    private TaskAdapter adapterConcluidas = new TaskAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtSelectedDate = view.findViewById(R.id.txtSelectedDate);
        txtSemTarefas = view.findViewById(R.id.txtSemTarefas);
        txtTituloPendentes = view.findViewById(R.id.txtTarefasPendentes);
        txtTituloConcluidas = view.findViewById(R.id.txtTarefasConcluidas);
        calendarView = view.findViewById(R.id.calendarView);
        recyclerPendentes = view.findViewById(R.id.recyclerPendentes);
        recyclerConcluidas = view.findViewById(R.id.recyclerConcluidas);

        recyclerPendentes.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerConcluidas.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerPendentes.setAdapter(adapterPendentes);
        recyclerConcluidas.setAdapter(adapterConcluidas);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDay = eventDay.getCalendar();
                SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                txtSelectedDate.setText("Selecionado: " + fmt.format(clickedDay.getTime()));
                carregarTarefasDoDia(clickedDay);
            }
        });

        carregarDiasComTarefas();
    }

    private void carregarDiasComTarefas() {
        Executors.newSingleThreadExecutor().execute(() -> {
            long userId = requireContext()
                    .getSharedPreferences("APP_PREF", requireContext().MODE_PRIVATE)
                    .getLong("USER_ID_LOGADO", -1);

            if (userId == -1) return;

            List<Task> tarefas = AppDatabase.getInstance(requireContext())
                    .taskDao()
                    .getPending(userId);

            long agora = System.currentTimeMillis();
            List<EventDay> eventos = new ArrayList<>();

            for (Task t : tarefas) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(t.getDateTime());

                int cor = (t.getDateTime() < agora)
                        ? R.drawable.red_dot
                        : R.drawable.green_dot;

                eventos.add(new EventDay(cal, cor));
            }

            requireActivity().runOnUiThread(() -> calendarView.setEvents(eventos));
        });
    }

    private void carregarTarefasDoDia(Calendar diaSelecionado) {
        Executors.newSingleThreadExecutor().execute(() -> {
            long userId = requireContext()
                    .getSharedPreferences("APP_PREF", requireContext().MODE_PRIVATE)
                    .getLong("USER_ID_LOGADO", -1);

            if (userId == -1) return;

            Calendar inicio = (Calendar) diaSelecionado.clone();
            inicio.set(Calendar.HOUR_OF_DAY, 0);
            inicio.set(Calendar.MINUTE, 0);
            inicio.set(Calendar.SECOND, 0);
            inicio.set(Calendar.MILLISECOND, 0);

            Calendar fim = (Calendar) inicio.clone();
            fim.set(Calendar.HOUR_OF_DAY, 23);
            fim.set(Calendar.MINUTE, 59);
            fim.set(Calendar.SECOND, 59);
            fim.set(Calendar.MILLISECOND, 999);

            List<Task> tarefas = AppDatabase.getInstance(requireContext())
                    .taskDao()
                    .getByDateRange(userId,
                            inicio.getTimeInMillis(),
                            fim.getTimeInMillis());

            requireActivity().runOnUiThread(() -> {
                List<Task> pendentes = new ArrayList<>();
                List<Task> concluidas = new ArrayList<>();

                for (Task t : tarefas) {
                    if (t.isCompleted()) {
                        concluidas.add(t);
                    } else {
                        pendentes.add(t);
                    }
                }

                adapterPendentes.atualizar(pendentes);
                adapterConcluidas.atualizar(concluidas);

                txtTituloPendentes.setVisibility(pendentes.isEmpty() ? View.GONE : View.VISIBLE);
                recyclerPendentes.setVisibility(pendentes.isEmpty() ? View.GONE : View.VISIBLE);

                txtTituloConcluidas.setVisibility(concluidas.isEmpty() ? View.GONE : View.VISIBLE);
                recyclerConcluidas.setVisibility(concluidas.isEmpty() ? View.GONE : View.VISIBLE);

                txtSemTarefas.setVisibility((pendentes.isEmpty() && concluidas.isEmpty()) ? View.VISIBLE : View.GONE);
            });
        });
    }
}
package com.example.ToDo.view;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ToDo.R;
import com.example.ToDo.database.AppDatabase;
import com.example.ToDo.entity.Task;
import com.example.ToDo.util.NotificationUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskDetailActivity extends AppCompatActivity {

    private EditText edtTitle, edtDesc, edtTags;
    private TextView txtDateTime;
    private CheckBox cbFavorite, cbCompleted;
    private Button btnPickDateTime, btnSave, btnDelete;
    private Task task;
    private final Calendar cal = Calendar.getInstance();
    private final SimpleDateFormat fmt =
            new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // 1) inicializa views
        edtTitle        = findViewById(R.id.edtTitle);
        edtDesc         = findViewById(R.id.edtDesc);
        edtTags         = findViewById(R.id.edtTags);
        txtDateTime     = findViewById(R.id.txtDateTime);
        cbFavorite      = findViewById(R.id.cbFavorite);
        cbCompleted     = findViewById(R.id.cbCompleted);
        btnPickDateTime = findViewById(R.id.btnPickDateTime);
        btnSave         = findViewById(R.id.btnSave);
        btnDelete       = findViewById(R.id.btnDelete);

        // 2) determina se é edição ou criação
        if (getIntent() != null && getIntent().hasExtra("task")) {
            task = (Task) getIntent().getSerializableExtra("task");
            bindData();
            btnDelete.setEnabled(true);
        } else {
            task = new Task();
            btnDelete.setEnabled(false);
        }

        // 3) listeners
        btnPickDateTime.setOnClickListener(v -> pickDateTime());
        btnSave.setOnClickListener(v -> saveTask());
        btnDelete.setOnClickListener(v -> deleteTask());
    }

    private void bindData() {
        edtTitle.setText(task.getTitle());
        edtDesc.setText(task.getDescription());
        edtTags.setText(task.getTags());
        cal.setTimeInMillis(task.getDateTime());
        txtDateTime.setText(fmt.format(cal.getTime()));
        cbFavorite.setChecked(task.isFavorite());
        cbCompleted.setChecked(task.isCompleted());
    }

    private void pickDateTime() {
        new DatePickerDialog(this,
                (picker, year, month, day) -> {
                    cal.set(year, month, day);
                    new TimePickerDialog(this,
                            (tp, hour, minute) -> {
                                cal.set(Calendar.HOUR_OF_DAY, hour);
                                cal.set(Calendar.MINUTE, minute);
                                txtDateTime.setText(fmt.format(cal.getTime()));
                            },
                            cal.get(Calendar.HOUR_OF_DAY),
                            cal.get(Calendar.MINUTE),
                            true
                    ).show();
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void saveTask() {
        task.setTitle(edtTitle.getText().toString().trim());
        task.setDescription(edtDesc.getText().toString().trim());
        task.setTags(edtTags.getText().toString().trim());
        task.setDateTime(cal.getTimeInMillis());
        task.setFavorite(cbFavorite.isChecked());
        task.setCompleted(cbCompleted.isChecked());

        executor.execute(() -> {
            long id;
            if (task.getId() > 0) {
                AppDatabase.getInstance(this).taskDao().update(task);
                NotificationUtils.cancel(this, task.getId());
                id = task.getId();
            } else {
                id = AppDatabase.getInstance(this).taskDao().insert(task);
                task.setId(id);
            }
            NotificationUtils.schedule(this, task.getDateTime(), id, task.getTitle());

            runOnUiThread(() -> {
                Toast.makeText(this, "Tarefa salva", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);      // <<< ADICIONADO
                finish();
            });
        });
    }

    private void deleteTask() {
        if (task.getId() > 0) {
            executor.execute(() -> {
                AppDatabase.getInstance(this).taskDao().delete(task);
                NotificationUtils.cancel(this, task.getId());
                runOnUiThread(() -> {
                    Toast.makeText(this, "Tarefa excluída", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);  // <<< ADICIONADO
                    finish();
                });
            });
        } else {
            Toast.makeText(this, "Nada para excluir", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}

package com.example.ToDo.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ToDo.R;
import com.example.ToDo.database.AppDatabase;
import com.example.ToDo.entity.User;
import com.example.ToDo.util.PasswordUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditProfileActivity extends AppCompatActivity {

    private EditText edtNewName, edtNewEmail, edtNewPassword;
    private Button btnSaveChanges;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private long userId;
    private int editMode; // 0 = nome, 1 = email, 2 = senha

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edtNewName = findViewById(R.id.edtNewName);
        edtNewEmail = findViewById(R.id.edtNewEmail);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        userId = getIntent().getLongExtra("USER_ID", -1);
        editMode = getIntent().getIntExtra("EDIT_MODE", 0);

        switch (editMode) {
            case 0:
                edtNewEmail.setVisibility(View.GONE);
                edtNewPassword.setVisibility(View.GONE);
                setTitle("Editar nome");
                break;
            case 1:
                edtNewName.setVisibility(View.GONE);
                edtNewPassword.setVisibility(View.GONE);
                setTitle("Editar e-mail");
                break;
            case 2:
                edtNewName.setVisibility(View.GONE);
                edtNewEmail.setVisibility(View.GONE);
                setTitle("Editar senha");
                break;
        }

        btnSaveChanges.setOnClickListener(view -> {
            executor.execute(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                User user = db.userDao().findById(userId);

                if (user != null) {
                    switch (editMode) {
                        case 0:
                            String newName = edtNewName.getText().toString().trim();
                            if (!newName.isEmpty()) user.setName(newName);
                            break;
                        case 1:
                            String newEmail = edtNewEmail.getText().toString().trim();
                            if (!newEmail.isEmpty()) {
                                user.setEmail(newEmail);
                                getSharedPreferences("APP_PREF", MODE_PRIVATE)
                                        .edit()
                                        .putString("EMAIL_LOGADO", newEmail)
                                        .apply();
                            }
                            break;
                        case 2:
                            String newPass = edtNewPassword.getText().toString().trim();
                            if (!newPass.isEmpty()) {
                                String hashed = PasswordUtils.hash(newPass); // ✅ Corrigido
                                user.setPasswordHash(hashed);
                            }
                            break;
                    }

                    db.userDao().update(user);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show()
                    );
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
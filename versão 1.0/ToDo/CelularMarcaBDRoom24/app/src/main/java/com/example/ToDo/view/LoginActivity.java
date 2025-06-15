package com.example.ToDo.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;  // ← adicionado
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

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button   btnLogin, btnRegister;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail    = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin    = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void attemptLogin() {
        String email = edtEmail.getText().toString().trim().toLowerCase(); // ← lowercase
        String pwd   = edtPassword.getText().toString();

        if (email.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(this,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            User user = AppDatabase
                    .getInstance(getApplicationContext())
                    .userDao()
                    .findByEmail(email);

            // log para verificar o que está sendo lido
            Log.d("USUARIO_LOGIN",
                    "inputEmail=" + email +
                            "  inputPwd=" + pwd +
                            "  foundUser=" + (user != null) +
                            "  storedHash=" + (user == null ? "null" : user.getPasswordHash())
            );

            boolean ok = user != null
                    && PasswordUtils.verify(pwd, user.getPasswordHash());
            Log.d("USUARIO_LOGIN", "verifyResult=" + ok);

            runOnUiThread(() -> {
                if (ok) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Credenciais incorretas",
                            Toast.LENGTH_SHORT).show();
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

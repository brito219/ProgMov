package com.example.ToDo.view;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ToDo.R;
import com.example.ToDo.database.AppDatabase;
import com.example.ToDo.entity.User;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private TextView txtProfileName, txtProfileEmail;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_profile);

        imgProfile      = findViewById(R.id.imgProfile);
        txtProfileName  = findViewById(R.id.txtProfileName);
        txtProfileEmail = findViewById(R.id.txtProfileEmail);

        loadUser();
    }

    private void loadUser() {
        executor.execute(() -> {
            String email = getSharedPreferences("APP_PREF", MODE_PRIVATE)
                    .getString("EMAIL_LOGADO", null);

            if (email == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
                    finish();
                });
                return;
            }

            User user = AppDatabase.getInstance(this)
                    .userDao()
                    .findByEmail(email);

            if (user != null) {
                Log.d("PROFILE", "Nome: " + user.getName());
                Log.d("PROFILE", "E-mail: " + user.getEmail());
                Log.d("PROFILE", "Foto URI: " + user.getPhotoUri());

                runOnUiThread(() -> {
                    txtProfileName.setText(user.getName());
                    txtProfileEmail.setText(user.getEmail());

                    try {
                        Uri uri = Uri.parse(user.getPhotoUri());
                        File f = new File(uri.getPath());
                        if (f.exists()) {
                            imgProfile.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
                        } else {
                            Log.d("PROFILE", "Arquivo da foto não encontrado: " + f.getAbsolutePath());
                        }
                    } catch (Exception e) {
                        Log.e("PROFILE", "Erro ao carregar imagem: " + e.getMessage());
                    }
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}


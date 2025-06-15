package com.example.ToDo.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;            // ← adicionado
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ToDo.R;
import com.example.ToDo.database.AppDatabase;
import com.example.ToDo.entity.User;
import com.example.ToDo.util.PasswordUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA      = 1001;
    private static final int REQUEST_CAMERA_PERM  = 1002;

    private ImageView imgPhoto;
    private Uri       photoUri;
    private EditText  edtName, edtEmail, edtPassword;
    private Button    btnSave;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imgPhoto    = findViewById(R.id.imgPhoto);
        edtName     = findViewById(R.id.edtName);
        edtEmail    = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSave     = findViewById(R.id.btnSave);

        imgPhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERM
                );
            } else {
                dispatchTakePictureIntent();
            }
        });

        btnSave.setOnClickListener(v -> onSaveClicked());
    }

    private void dispatchTakePictureIntent() {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePic, REQUEST_CAMERA);
        } else {
            Toast.makeText(this,
                    "Nenhum app de câmera disponível",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void onSaveClicked() {
        String name  = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim().toLowerCase();  // ← lowercase
        String pwd   = edtPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || pwd.isEmpty() || photoUri == null) {
            Toast.makeText(this,
                    "Todos os campos e a foto são obrigatórios",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // gera hash + salt
        String hash = PasswordUtils.hash(pwd);
        // log para verificar o que está gravando
        Log.d("USUARIO_REGISTRO",
                "email(lower)=" + email +
                        "  rawPwd=" + pwd +
                        "  generatedHash=" + hash
        );

        executor.execute(() -> {
            User u = new User(name, email, hash, photoUri.toString());
            AppDatabase
                    .getInstance(getApplicationContext())
                    .userDao()
                    .insert(u);
            runOnUiThread(() -> {
                Toast.makeText(this,
                        "Usuário cadastrado com sucesso",
                        Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERM
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            Toast.makeText(this,
                    "Permissão de câmera negada",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA
                && resultCode  == Activity.RESULT_OK
                && data        != null
                && data.getExtras() != null) {

            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            imgPhoto.setImageBitmap(bmp);

            File f = new File(getFilesDir(),
                    "user_" + System.currentTimeMillis() + ".jpg");
            try (FileOutputStream out = new FileOutputStream(f)) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                photoUri = Uri.fromFile(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}

package com.example.ToDo.view.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ToDo.R;
import com.example.ToDo.database.AppDatabase;
import com.example.ToDo.entity.User;
import com.example.ToDo.view.EditProfileActivity;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private ImageView imgProfile;
    private TextView txtName, txtEmail;
    private Button btnEditProfile;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private long userId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        imgProfile = root.findViewById(R.id.imgProfile);
        txtName = root.findViewById(R.id.txtProfileName);
        txtEmail = root.findViewById(R.id.txtProfileEmail);
        btnEditProfile = root.findViewById(R.id.btnEditProfile);

        btnEditProfile.setOnClickListener(v -> {
            if (userId == -1) return;

            String[] opcoes = {"Nome de usuário", "E-mail", "Senha"};

            new AlertDialog.Builder(getContext())
                    .setTitle("Editar perfil")
                    .setItems(opcoes, (dialog, which) -> {
                        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                        intent.putExtra("USER_ID", userId);
                        intent.putExtra("EDIT_MODE", which);
                        startActivity(intent);
                    })
                    .show();
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarUsuario(); // sempre recarrega ao voltar
    }

    private void carregarUsuario() {
        executor.execute(() -> {
            if (getActivity() == null) return;

            String email = getActivity()
                    .getSharedPreferences("APP_PREF", getActivity().MODE_PRIVATE)
                    .getString("EMAIL_LOGADO", null);

            if (email == null) return;

            User u = AppDatabase.getInstance(getActivity())
                    .userDao()
                    .findByEmail(email);

            if (u != null && getActivity() != null) {
                userId = u.getId();

                getActivity().runOnUiThread(() -> {
                    txtName.setText(u.getName());
                    txtEmail.setText(u.getEmail());
                    try {
                        Uri uri = Uri.parse(u.getPhotoUri());
                        File f = new File(uri.getPath());
                        if (f.exists()) {
                            imgProfile.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
                        }
                    } catch (Exception e) {
                        Log.e("PROFILE", "Erro ao carregar imagem: " + e.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
package com.example.ToDo.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.ToDo.R;

public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout do perfil do usuário
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}

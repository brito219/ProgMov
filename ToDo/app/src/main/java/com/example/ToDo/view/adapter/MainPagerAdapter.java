package com.example.ToDo.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ToDo.view.fragment.FiltersFragment;
import com.example.ToDo.view.fragment.FiltersFragment;
import com.example.ToDo.view.fragment.ProfileFragment;
import com.example.ToDo.view.fragment.TaskListFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new TaskListFragment();   // ← substitua por seu fragmento real
            case 1: return new FiltersFragment();     // ← idem
            case 2: return new ProfileFragment();    // ← perfil
            default: return new TaskListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // número de abas
    }
}
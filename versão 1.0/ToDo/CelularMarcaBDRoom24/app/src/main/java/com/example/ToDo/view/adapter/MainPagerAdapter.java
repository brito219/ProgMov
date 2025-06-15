package com.example.ToDo.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.ToDo.view.fragment.FiltersFragment;
import com.example.ToDo.view.fragment.ProfileFragment;
import com.example.ToDo.view.fragment.TaskListFragment;

public class MainPagerAdapter extends FragmentStateAdapter {
    public MainPagerAdapter(@NonNull FragmentActivity fa) { super(fa); }
    @NonNull @Override
    public Fragment createFragment(int pos) {
        switch(pos) {
            case 1: return new FiltersFragment();
            case 2: return new ProfileFragment();
            default: return new TaskListFragment();
        }
    }
    @Override public int getItemCount() { return 3; }
}

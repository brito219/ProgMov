package com.example.ToDo.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.ToDo.R;
import com.example.ToDo.view.adapter.MainPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.ToDo.view.MainActivity;
import com.example.ToDo.R;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_main);

        ViewPager2 vp = findViewById(R.id.viewPager);
        TabLayout tabs = findViewById(R.id.tabs);
        vp.setAdapter(new MainPagerAdapter(this));
        new TabLayoutMediator(tabs, vp,
                (tab, pos) -> {
                    switch(pos) {
                        case 0: tab.setText("Tarefas"); break;
                        case 1: tab.setText("Filtros"); break;
                        case 2: tab.setText("Perfil"); break;
                    }
                }).attach();
    }
}

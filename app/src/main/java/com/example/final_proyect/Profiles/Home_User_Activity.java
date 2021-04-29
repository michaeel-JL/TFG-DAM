package com.example.final_proyect.Profiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.final_proyect.Adapters.Page_User_Adapter;
import com.example.final_proyect.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Home_User_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new Page_User_Adapter(this));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case 0: {
                        tab.setText("Noticias");
                        tab.setIcon(R.drawable.ic_noticias);
                        break;
                    }

                    case 1: {
                        tab.setText("Consultas");
                        tab.setIcon(R.drawable.ic_consultas);
                        break;
                    }

                    case 2: {
                        tab.setText("Mapa");
                        tab.setIcon(R.drawable.ic_mapa);
                        break;

                    }

                    case 3: {
                        tab.setText("Perfil");
                        tab.setIcon(R.drawable.ic_perfil);
                        break;
                    }

                }

            }
        });
        tabLayoutMediator.attach();


    }
}
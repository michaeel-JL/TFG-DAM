package com.example.final_proyect.Profiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.final_proyect.Adapters.Page_Doctor_Adapter;
import com.example.final_proyect.Adapters.Page_User_Adapter;
import com.example.final_proyect.Models.Estado;
import com.example.final_proyect.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_Doctor_Activity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference ref_estado = database.getReference("Estado").child(user.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new Page_Doctor_Adapter(this));

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
                        tab.setText("Perfil");
                        tab.setIcon(R.drawable.ic_perfil);
                        break;
                    }

                }

            }
        });
        tabLayoutMediator.attach();
    }

    private void estadoUsuario(String estado) {

        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Estado est = new Estado("", "", estado);
                ref_estado.setValue(est);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        estadoUsuario("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        estadoUsuario("offline");
    }

}
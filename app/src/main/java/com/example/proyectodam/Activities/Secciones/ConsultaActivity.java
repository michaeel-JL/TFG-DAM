package com.example.proyectodam.Activities.Secciones;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.proyectodam.Adapter.PageAdapter;
import com.example.proyectodam.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConsultaActivity extends AppCompatActivity {


    //OBJETO PARA LA BASE DE DATOS
    private DatabaseReference mDataBase;
    private StorageReference mStorage;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Uri uri;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consulta_activity);

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new PageAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){

                    case 0: {
                        tab.setText("Users");
                        tab.setIcon(R.drawable.ic_usuarios);
                        break;
                    }
                    case 1: {
                        tab.setText("Chats");
                        tab.setIcon(R.drawable.ic_chats);
                        break;
                    }

                }

            }
        });
        tabLayoutMediator.attach();

        final FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();


        //Obtenemos el usuario cuya sesión está abierta
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.usuarios);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                        switch (item.getItemId()) {
                            case R.id.perfil:
                                startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                                overridePendingTransition(0, 0);
                                return true;

                            case R.id.usuarios:
                                return true;

                            case R.id.chat:
                                startActivity(new Intent(getApplicationContext(), InicioActivity.class));
                                overridePendingTransition(0, 0);
                                return true;

                            case R.id.mapa:
                                return true;
                        }
                        return false;
                    }
                });
    }
}
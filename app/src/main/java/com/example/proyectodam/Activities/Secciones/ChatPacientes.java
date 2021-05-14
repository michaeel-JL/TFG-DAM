package com.example.proyectodam.Activities.Secciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChatPacientes extends AppCompatActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_chats_activity);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.chat);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                        switch (item.getItemId()) {

                            case R.id.inicio:
                                startActivity(new Intent(getApplicationContext(), NoticiasActivity.class));
                                overridePendingTransition(0,0);
                                return true;

                            case R.id.usuarios:
                                //Falta  activity***
                                startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                                overridePendingTransition(0,0);
                                return true;

                            case R.id.chat:
                                //Falta activity***
                                startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                                overridePendingTransition(0,0);
                                return true;

                            /*case R.id.notasItem:
                                startActivity(new Intent(getApplicationContext(), BoardActivity.class));
                                overridePendingTransition(0,0);
                                return true;*/

                            case R.id.perfil:
                                startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                                overridePendingTransition(0,0);
                                return true;
                        }

                        return false;
                    }
                });
    }
}

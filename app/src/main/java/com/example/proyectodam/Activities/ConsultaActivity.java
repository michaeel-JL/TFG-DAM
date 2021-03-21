package com.example.proyectodam.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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


        //Obtenemos el usuario cuya sesión está abierta
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.consultaItem);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                        switch (item.getItemId()) {
                            case R.id.perfilItem:
                                startActivity(new Intent(getApplicationContext(), PacienteActivity.class));
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.consultaItem:

                                return true;
                            case R.id.foroItem:
                                startActivity(new Intent(getApplicationContext(), ForoActivity.class));
                                overridePendingTransition(0, 0);
                                return true;
                        }

                        return false;
                    }
                });
    }
}

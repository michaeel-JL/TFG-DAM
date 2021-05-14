package com.example.proyectodam.Activities.Secciones;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam.Activities.MostrarNoticiasActivity;
import com.example.proyectodam.Activities.MostrarUsuariosActivity;
import com.example.proyectodam.Activities.Registro.LoginActivity;
import com.example.proyectodam.Models.Usuarios;
import com.example.proyectodam.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {

    private CircleImageView imageProfile;
    private StorageReference mStorage;
    private TextView  textViewUsuario, textViewemail, textViewEdad, textViewRol;
    private String foto, email, usuario, edad;
    private String rol = "";
    private Button logout;
    private Uri uri;
    private Usuarios usuarioData;
    BottomNavigationView bottomNavigationView;

    //BBDD
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private SharedPreferences prefs;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cargamos el activity correspondiente
        setContentView(R.layout.perfil_activity);

        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        imageProfile = (CircleImageView) findViewById(R.id.imgProfile);
        mStorage = FirebaseStorage.getInstance().getReference();
        textViewemail=findViewById(R.id.textEmail);
        textViewUsuario=findViewById(R.id.textUsuario);
        textViewEdad=findViewById(R.id.textEdad);
        textViewRol=findViewById(R.id.textRolPaciente);
        logout=findViewById(R.id.logout);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference();

        // Agregamos un listener a la referencia
        ref.child("Usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    //email
                    email = dataSnapshot.child("email").getValue(String.class);
                    textViewemail.setText(email);
                    //usuario
                    usuario = dataSnapshot.child("nombreUsuario").getValue(String.class);
                    textViewUsuario.setText(usuario);
                    //edad
                    edad = dataSnapshot.child("edad").getValue(String.class);
                    textViewEdad.setText(edad);
                    rol = dataSnapshot.child("rol").getValue(String.class);
                    textViewRol.setText("Rol --> " + rol);
                    //Foto perfil
                    foto = dataSnapshot.child("foto").getValue(String.class).toString();
                    cargarImagen(foto);

                    if (rol.equals("paciente")) {
                        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationViewPaciente);
                        comprobarRol(bottomNavigationView, "paciente");

                    } else if (rol.equals("medico")) {
                        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationViewDoctor);
                        comprobarRol(bottomNavigationView, "medico");

                    }else if (rol.equals("admin")){
                        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationViewAdmin);
                        comprobarRol(bottomNavigationView, "admin");
                    }

                    bottomNavigationView.setSelectedItemId(R.id.perfilItem);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error de lectura: " + databaseError.getCode());
            }
        });


        //Obtenemos la info del usuario
        getUsuarioInfo(user);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        //Para cerrar sesión
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(PerfilActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void comprobarRol(BottomNavigationView nav, String rol) {

        if(rol.equals("paciente")){

            nav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.inicio:
                                startActivity(new Intent(getApplicationContext(), InicioActivity.class));
                                overridePendingTransition(0, 0);
                                return true;

                            case R.id.usuarios:
                                //IMPLEMENTACION ****
                                startActivity(new Intent(getApplicationContext(), ConsultaActivity.class));
                                overridePendingTransition(0, 0);
                                return true;

                            case R.id.chat:
                                //IMPLEMENTACION*****
                                startActivity(new Intent(getApplicationContext(), InicioActivity.class));
                                overridePendingTransition(0, 0);
                                return true;

                            case R.id.mapa:
                                //IMPLEMENTACION****
                                startActivity(new Intent(getApplicationContext(), InicioActivity.class));
                                overridePendingTransition(0, 0);
                                return true;
                        }
                        return false;
                    }
                });
        }else if(rol.equals("doctor")){
            nav.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.inicio:
                                    startActivity(new Intent(getApplicationContext(), InicioActivity.class));
                                    overridePendingTransition(0, 0);
                                    return true;

                                case R.id.usuarios:
                                    startActivity(new Intent(getApplicationContext(), ChatPacientes.class));
                                    overridePendingTransition(0,0);
                                    return true;

                                case R.id.chat:
                                    startActivity(new Intent(getApplicationContext(), ChatPacientes.class));
                                    overridePendingTransition(0,0);
                                    return true;
                            }
                            return false;
                        }
                    });
        }else if(rol.equals("admin")){
            nav.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.inicio:
                                    startActivity(new Intent(getApplicationContext(), InicioActivity.class));
                                    overridePendingTransition(0, 0);
                                    return true;

                                case R.id.usuarios:
                                    startActivity(new Intent(getApplicationContext(), MostrarUsuariosActivity.class));
                                    overridePendingTransition(0,0);
                                    return true;
                                case R.id.noticias:
                                    startActivity(new Intent(getApplicationContext(), MostrarNoticiasActivity.class));
                                    overridePendingTransition(0,0);
                                    return true;
                            }
                            return false;
                        }
                    });
        }
    }

    // Inflamos el layout del menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }
    // Manejamos eventos click en el menu de opciones
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ajustes:
                return true;
            default:
                return false;
        }
    }

    private void cargarImagen(String link) {
        Picasso.get()
                .load(link)
                .fit()
                .into(imageProfile);
    }

    private void getUsuarioInfo(final FirebaseUser user) {
        //Ruta donde buscaremos la información asociada al usuario
        mDataBase.child("Usuarios").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    usuarioData = new Usuarios();
                    usuarioData.setEdad(dataSnapshot.child("edad").getValue().toString());
                    usuarioData.setEmail(dataSnapshot.child("email").getValue().toString());
                    usuarioData.setFoto(dataSnapshot.child("foto").getValue().toString());
                    usuarioData.setId(dataSnapshot.getKey());
                    usuarioData.setNombreUsuario(dataSnapshot.child("nombreUsuario").getValue().toString());
                    usuarioData.setPassword(dataSnapshot.child("password").getValue().toString());
                    usuarioData.setRol(dataSnapshot.child("rol").getValue().toString());

                    //Se obtiene el url de ubicación de la foto en caso de estar guardado

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
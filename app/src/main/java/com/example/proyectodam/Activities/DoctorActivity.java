package com.example.proyectodam.Activities;

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

public class DoctorActivity extends AppCompatActivity {

    private CircleImageView imageProfile;
    private TextView textViewUsuario, textViewemail, textViewEdad, textViewRol;
    private String foto, email, usuario, edad, rol;
    private Button logout;
    private Uri uri;


    //BBDD
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private SharedPreferences prefs;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_doc_activity);


        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance().getReference();


        imageProfile = (CircleImageView) findViewById(R.id.imgProfile);
        mStorage = FirebaseStorage.getInstance().getReference();
        textViewemail=findViewById(R.id.textEmail);
        textViewUsuario=findViewById(R.id.textUsuario);
        textViewEdad=findViewById(R.id.textEdad);
        logout=findViewById(R.id.logout);
        textViewRol=findViewById(R.id.textRol);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference();

        // Agregamos un listener a la referencia
        ref.child("Usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    //email
                    email= dataSnapshot.child("email").getValue(String.class);
                    textViewemail.setText(email);

                    //usuario
                    usuario= dataSnapshot.child("nombreUsuario").getValue(String.class);
                    textViewUsuario.setText(usuario);

                    //edad
                    edad= dataSnapshot.child("edad").getValue(String.class);
                    textViewEdad.setText(edad);

                    rol= dataSnapshot.child("rol").getValue(String.class);
                    textViewRol.setText("Rol --> "+rol);

                    //Foto perfil
                    foto = dataSnapshot.child("foto").getValue(String.class).toString();
                    cargarImagen(foto);
                    //imageProfile.setImageURI(Uri.parse(foto));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Fallo la lectura: " + databaseError.getCode());
            }
        });




        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.perfilItem);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                        switch (item.getItemId()) {
                            case R.id.perfilItem:

                                return true;
                            case R.id.chatPacientesItem:
                                startActivity(new Intent(getApplicationContext(), ChatPacientes.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.foroItem:
                                startActivity(new Intent(getApplicationContext(), ForoDoctorActivity.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.notasItem:
                                startActivity(new Intent(getApplicationContext(), BoardActivity.class));
                                overridePendingTransition(0,0);
                                return true;
                        }

                        return false;
                    }
                });


        //Obtenemos la info del usuario
        //getUsuarioInfo(user);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        //Para cerrar sesión
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(DoctorActivity.this, LoginActivity.class));
                finish();
            }
        });


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


}

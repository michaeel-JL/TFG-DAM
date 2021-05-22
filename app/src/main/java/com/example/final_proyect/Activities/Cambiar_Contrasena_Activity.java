package com.example.final_proyect.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.final_proyect.R;
import com.example.final_proyect.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Cambiar_Contrasena_Activity extends AppCompatActivity {

    private String password, email_N, password_N;
    private EditText password1, password2;
    private Button btn_cambiar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        //configuración Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_cambiar_contrasena);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.toolbar_cambiar_password);

        showAlertForChanginigPassword();

        //Elementos
        password1=findViewById(R.id.pass1);
        password2=findViewById(R.id.pass2);
        btn_cambiar=findViewById(R.id.boton_change_password);



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
                    email_N = dataSnapshot.child("email").getValue(String.class);
                    password_N = dataSnapshot.child("password").getValue(String.class);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error de lectura: " + databaseError.getCode());
            }
        });



        btn_cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password1.getText().toString().equals(password2.getText().toString()) ){
                    if(password1.getText().toString().length()>=6 && password2.getText().toString().length()>=6){



                        FirebaseUser user_ = FirebaseAuth.getInstance().getCurrentUser();


                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email_N, password_N);

                        user_.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(password1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                    } else {
                                                    }
                                                }
                                            });
                                        } else {
                                        }
                                    }
                                });


                        Toast.makeText(Cambiar_Contrasena_Activity.this, "CONTRASEÑA CAMBIADA CON EXITO", Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(Cambiar_Contrasena_Activity.this, "CONTRASEÑA DEMASIADO CORTA", Toast.LENGTH_LONG).show();

                    }
                }else{
                    Toast.makeText(Cambiar_Contrasena_Activity.this, "NO COINCIDEN LOS CAMPOS", Toast.LENGTH_LONG).show();

                }


            }
        });






    }


    //** Dialogs **//
    private void showAlertForChanginigPassword() {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);

        builder.setTitle("Indica tu contraseña actual");

        final EditText contraseña_actual = new EditText(this);
        builder.setView(contraseña_actual);

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
                    password = dataSnapshot.child("password").getValue(String.class);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error de lectura: " + databaseError.getCode());
            }
        });

        builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {

            if(password.equals(contraseña_actual.getText().toString())){


                Toast.makeText(Cambiar_Contrasena_Activity.this, "Contraseña correcta", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(Cambiar_Contrasena_Activity.this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                onBackPressed();
            }

        });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {
            onBackPressed();

        });



        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }





}
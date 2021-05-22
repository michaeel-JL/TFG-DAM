package com.example.final_proyect.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.final_proyect.R;
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

public class Cambiar_Email_Activity extends AppCompatActivity {

    private String  email_N, password_N;
    private EditText nuevo_email;
    private Button btn_cambiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_email);

        //configuración Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_cambiar_email);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.toolbar_cambiar_email);


        //Elementos
        nuevo_email=findViewById(R.id.new_email);
        btn_cambiar=findViewById(R.id.boton_change_password_email);



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

                if(nuevo_email.getText().toString()!=null){



                    FirebaseUser user_ = FirebaseAuth.getInstance().getCurrentUser();


                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email_N, password_N);

                    user_.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        user.updateEmail(nuevo_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
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

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference noticiasRef = ref.child("Usuarios").child(uid);

                    noticiasRef.child("email").setValue(nuevo_email.getText().toString());

                    Toast.makeText(Cambiar_Email_Activity.this, "EMAIL CAMBIADA CON EXITO", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Cambiar_Email_Activity.this, "CAMPO VACÍO", Toast.LENGTH_LONG).show();

                }

            }
        });

    }


}
package com.example.final_proyect.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.final_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Ver_Perfil_Activity extends AppCompatActivity {

    private CircleImageView imageProfile;
    private String nombre, apellidos, email, edad, foto, sexo;
    private TextView name_profile, apellidos_profile, email_profile, edad_profile, sexo_profile;
    private ImageButton btn_alergias, btn_enfermades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        //Cogemos el id del paciente
        String id_paciente = getIntent().getExtras().getString("id_paciente");

        name_profile = findViewById(R.id.d_nombre);
        apellidos_profile = findViewById(R.id.d_apellidos);
        email_profile = findViewById(R.id.d_email);
        edad_profile = findViewById(R.id.d_edad);
        sexo_profile = findViewById(R.id.d_sexo);
        imageProfile = findViewById(R.id.d_img);

        btn_alergias = findViewById(R.id.d_btn_alergias);
        btn_enfermades = findViewById(R.id.d_btn_enfermedades);

        //Click en boton alergia va a sus alergias enviando el id del paciente
        btn_alergias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ver_Perfil_Activity.this, Alergias_Activity.class);
                intent.putExtra("id_paciente", id_paciente); //Mandamos el id del paciente
                startActivity(intent);
            }
        });

        //Click en boton enfermedades va asus enfermedades enviando el id del paciente.
        btn_enfermades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ver_Perfil_Activity.this, Enfermedades_Activity.class);
                intent.putExtra("id_paciente", id_paciente); //Mandamos el id del paciente
                startActivity(intent);
            }
        });

        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference();

        // Mostramos los datos del paciente PERFIL
        ref.child("Usuarios").child(id_paciente).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    //email
                    email = dataSnapshot.child( "email").getValue(String.class);
                    email_profile.setText(email);
                    //usuario
                    nombre = dataSnapshot.child("nombre").getValue(String.class);
                    name_profile.setText(nombre);
                    //edad
                    edad = dataSnapshot.child("edad").getValue(String.class);
                    edad_profile.setText(edad);

                    //Apellidos
                    apellidos =  dataSnapshot.child("apellidos").getValue(String.class);
                    apellidos_profile.setText(apellidos);

                    //sexo
                    sexo = dataSnapshot.child("sexo").getValue(String.class);
                    sexo_profile.setText(sexo);

                    //Foto perfil
                    foto = dataSnapshot.child("foto").getValue(String.class);
                    cargarImagen(foto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error de lectura: " + databaseError.getCode());
            }
        });

    }

    //Cargamos la imagen del PACIENTE
    private void cargarImagen(String link) {
        Picasso.get()
                .load(link)
                .fit()
                .into(imageProfile);
    }
}
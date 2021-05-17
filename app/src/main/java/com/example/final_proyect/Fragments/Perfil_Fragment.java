package com.example.final_proyect.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.final_proyect.Activities.Login_Activity;
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


public class Perfil_Fragment extends Fragment {

    private CircleImageView imageProfile;
    String nombre, email, edad, foto, sexo;
    TextView name_profile, email_profile, rol_profile, edad_profile, sexo_profile;
    private Button logout;

    //BBDD
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private SharedPreferences prefs;

    public Perfil_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Perfil");


        mAuth = FirebaseAuth.getInstance();
        //user=mAuth.getCurrentUser();


        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        imageProfile = (CircleImageView) view.findViewById(R.id.img_perfil);
        name_profile = view.findViewById(R.id.nombre_perfil);
        email_profile = view.findViewById(R.id.email_perfil);
        edad_profile = view.findViewById(R.id.edad_perfil);
        sexo_profile=view.findViewById(R.id.sexo);

        logout = view.findViewById(R.id.cerrar_sesion);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });




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
                    email_profile.setText(email);
                    //usuario
                    nombre = dataSnapshot.child("nombre").getValue(String.class);
                    name_profile.setText(nombre);
                    //edad
                    edad = dataSnapshot.child("edad").getValue(String.class);
                    edad_profile.setText(edad);


                    //sexo
                    sexo = dataSnapshot.child("sexo").getValue(String.class);
                    sexo_profile.setText(sexo);

                    //Foto perfil
                    foto = dataSnapshot.child("foto").getValue(String.class).toString();
                    cargarImagen(foto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error de lectura: " + databaseError.getCode());
            }
        });
        return view;
    }


    private void logOut() {
        Intent intent = new Intent(getContext(), Login_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void removeSharedPreferences() {
        prefs.edit().clear().apply();
    }

    private void cargarImagen(String link) {
        Picasso.get()
                .load(link)
                .fit()

                .into(imageProfile);
    }

}
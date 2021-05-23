package com.example.final_proyect.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.final_proyect.Activities.Ajustes_Activity;
import com.example.final_proyect.Activities.Alergias_Activity;
import com.example.final_proyect.Activities.Enfermedades_Activity;
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

import static com.example.final_proyect.R.id.btn_alergias;
import static com.example.final_proyect.R.id.invisible;


public class Perfil_Fragment extends Fragment {

    private CircleImageView imageProfile;
    private String nombre, apellidos, email, edad, foto, sexo, rol;
    private TextView name_profile, apellidos_profile, email_profile, edad_profile, sexo_profile;
    private Button logout;
    private ImageButton btn_alergias, btn_enfermades, btn_ajustes;
    private LinearLayout ly_enfermedades, ly_alergias;

    //BBDD
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private SharedPreferences prefs;

    public Perfil_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_perfil_f);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.toolbar_perfil);
        mAuth = FirebaseAuth.getInstance();


        imageProfile = (CircleImageView) view.findViewById(R.id.img_perfil);
        name_profile = view.findViewById(R.id.nombre_perfil);
        apellidos_profile = view.findViewById(R.id.apellidos_perfil);
        email_profile = view.findViewById(R.id.email_perfil);
        edad_profile = view.findViewById(R.id.edad_perfil);
        sexo_profile=view.findViewById(R.id.sexo);
        ly_enfermedades=view.findViewById(R.id.ly_enfermedades);
        ly_alergias=view.findViewById(R.id.ly_alergias);

        btn_alergias = view.findViewById(R.id.btn_alergias);
        btn_enfermades = view.findViewById(R.id.btn_enfermedades);
        btn_ajustes = view.findViewById(R.id.btn_ajustes);

        logout = view.findViewById(R.id.cerrar_sesion);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });


        btn_alergias.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), Alergias_Activity.class);
            startActivity(intent);
        });

        btn_enfermades.setOnClickListener(view12 -> {
            Intent intent = new Intent(getContext(), Enfermedades_Activity.class);
            startActivity(intent);

        });

        btn_ajustes.setOnClickListener(view13 -> {
            Intent intent = new Intent(getContext(), Ajustes_Activity.class);
            startActivity(intent);

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
                    foto = dataSnapshot.child("foto").getValue(String.class).toString();
                    cargarImagen(foto);


                    rol =dataSnapshot.child("rol").getValue(String.class).toString();
                    if(rol.equals("admin")|| rol.equals("medico")){
                        ly_enfermedades.setVisibility(View.GONE);
                        ly_alergias.setVisibility(View.GONE);

                    }else{

                    }
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
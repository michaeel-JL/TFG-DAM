package com.example.final_proyect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.example.final_proyect.Models.Alergia;
import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Add_Alergia_Activity extends AppCompatActivity {

    private FloatingActionButton fab; //Boton flotante
    private EditText alergia_name, alergia_details;
    private DatabaseReference mDataBase; //BBDD
    private Spinner spn_gravedad;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alergia);

        alergia_name = findViewById(R.id.add_alergia_nombre);
        alergia_details = findViewById(R.id.add_alergia_detalles);
        spn_gravedad = (Spinner) findViewById(R.id.add_alergia_spn_gravedad);

        String uid = user.getUid();


        //Boton guardar

        FloatingActionButton fab = findViewById(R.id.add_alergia_btn_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataBase = FirebaseDatabase.getInstance().getReference();
                FirebaseDatabase databse = FirebaseDatabase.getInstance();


                DatabaseReference ref_alergias = databse.getReference("Alergias").child(uid);
                ref_alergias.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String id = ref_alergias.push().getKey();

                        Alergia alergia = new Alergia();
                        alergia.setNombre(alergia_name.getText().toString());
                        alergia.setDescripcion(alergia_details.getText().toString());
                        alergia.setGravedad(spn_gravedad.getSelectedItem().toString());
                        alergia.setId(id);
                        ref_alergias.child(alergia.getId()).setValue(alergia);
                        onBackPressed();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

            }
        });


    }

}
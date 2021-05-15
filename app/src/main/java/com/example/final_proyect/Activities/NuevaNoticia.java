package com.example.final_proyect.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_proyect.Models.Noticia;
import com.example.final_proyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NuevaNoticia extends AppCompatActivity {

    private FloatingActionButton fab; //Boton flotante
    private EditText titulo, descripción, imagenPrincipal, imagenSecundaria, texto;
    private DatabaseReference mDataBase; //BBDD
    private int contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_noticia_activity);

        titulo = findViewById(R.id.new_titulo);
        descripción = findViewById(R.id.new_description);
        imagenPrincipal = findViewById(R.id.new_imagenPrincipal);
        imagenSecundaria = findViewById(R.id.new_imagenSecundaria);
        texto = findViewById(R.id.new_texto);




        //boton guardar
        FloatingActionButton fab = findViewById(R.id.fabSave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mDataBase = FirebaseDatabase.getInstance().getReference();
                FirebaseDatabase databse = FirebaseDatabase.getInstance();

                DatabaseReference ref_noticias = databse.getReference("Noticias");
                ref_noticias.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String id_noticias = mDataBase.getKey();
                        //Noticia
                        Noticia noticia = new Noticia();
                        noticia.setTitulo(titulo.getText().toString());
                        noticia.setDescription(descripción.getText().toString());
                        noticia.setImagePrincipal(imagenPrincipal.getText().toString());
                        noticia.setImagenSecundaria(imagenSecundaria.getText().toString());
                        noticia.setTextoNoticia(texto.getText().toString());


                        ref_noticias.child(noticia.getTitulo()).setValue(noticia);
                        onBackPressed();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }}


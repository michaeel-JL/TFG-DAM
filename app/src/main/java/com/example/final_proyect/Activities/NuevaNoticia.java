package com.example.final_proyect.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
    private EditText titulo, descripci贸n, imagenPrincipal, imagenSecundaria, texto;
    private DatabaseReference mDataBase; //BBDD
    private int contador;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_noticia);

        titulo = findViewById(R.id.new_titulo);
        descripci贸n = findViewById(R.id.new_description);
        imagenPrincipal = findViewById(R.id.new_imagenPrincipal);
        imagenSecundaria = findViewById(R.id.new_imagenSecundaria);
        texto = findViewById(R.id.new_texto);
        spinner = (Spinner) findViewById(R.id.spn);



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

                        if (!titulo.getText().toString().isEmpty() && !descripci贸n.getText().toString().isEmpty() &&  !imagenPrincipal.getText().toString().isEmpty() && !imagenSecundaria.getText().toString().isEmpty() &&
                                !spinner.getSelectedItem().toString().isEmpty() ) {


                            String id_noticias = mDataBase.getKey();
                            //Noticia
                            Noticia noticia = new Noticia();
                            noticia.setTitulo(titulo.getText().toString());
                            noticia.setDescription(descripci贸n.getText().toString());
                            noticia.setImagePrincipal(imagenPrincipal.getText().toString());
                            noticia.setImagenSecundaria(imagenSecundaria.getText().toString());
                            noticia.setTextoNoticia(texto.getText().toString());
                            noticia.setEtiqueta(spinner.getSelectedItem().toString());
                            ref_noticias.child(noticia.getTitulo()).setValue(noticia);
                            onBackPressed();

                        }else{
                            Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }}


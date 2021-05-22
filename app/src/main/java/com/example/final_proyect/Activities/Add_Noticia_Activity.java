package com.example.final_proyect.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.final_proyect.Models.Noticia;
import com.example.final_proyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Add_Noticia_Activity extends AppCompatActivity {

    private FloatingActionButton fab; //Boton flotante
    private EditText titulo, descripcion, imagenPrincipal, imagenSecundaria, texto;
    private DatabaseReference mDataBase; //BBDD
    private int contador;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_noticia);

        //configuración Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_add_noticia);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.toolbar_add_noticia);

        titulo = findViewById(R.id.add_noticia_titulo);
        descripcion = findViewById(R.id.add_noticia_descripcion);
        imagenPrincipal = findViewById(R.id.add_noticia_img1);
        imagenSecundaria = findViewById(R.id.add_noticia_img2);
        texto = findViewById(R.id.add_noticia_texto);
        spinner = (Spinner) findViewById(R.id.add_noticia_spn);



        //boton guardar
        FloatingActionButton fab = findViewById(R.id.add_noticia_btn_save);
        fab.setOnClickListener(view -> {


            mDataBase = FirebaseDatabase.getInstance().getReference();
            FirebaseDatabase databse = FirebaseDatabase.getInstance();

            DatabaseReference ref_likes = databse.getReference("Likes");
            DatabaseReference ref_noticias = databse.getReference("Noticias");
            ref_noticias.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!titulo.getText().toString().isEmpty() && !descripcion.getText().toString().isEmpty() &&  !imagenPrincipal.getText().toString().isEmpty() && !imagenSecundaria.getText().toString().isEmpty() &&
                            !spinner.getSelectedItem().toString().isEmpty() ) {

                        String id_noticia = ref_noticias.push().getKey();

                        //Noticia
                        final Calendar c = Calendar.getInstance();
                        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        Noticia noticia = new Noticia();
                        noticia.setTitulo(titulo.getText().toString());
                        noticia.setDescription(descripcion.getText().toString());
                        noticia.setImagePrincipal(imagenPrincipal.getText().toString());
                        noticia.setImagenSecundaria(imagenSecundaria.getText().toString());
                        noticia.setTextoNoticia(texto.getText().toString());
                        noticia.setEtiqueta(spinner.getSelectedItem().toString());
                        noticia.setMegustas(0);
                        noticia.setComentarios(0);
                        noticia.setFecha(dateFormat.format(c.getTime()));
                        noticia.setHora(timeFormat.format(c.getTime()));

                        ref_noticias.child(id_noticia).setValue(noticia);

                        onBackPressed();

                    }else{
                        Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }}


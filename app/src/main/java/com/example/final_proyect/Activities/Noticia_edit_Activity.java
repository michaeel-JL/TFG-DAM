package com.example.final_proyect.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.final_proyect.Adapters.Noticias_Adapter;
import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Noticia_edit_Activity extends AppCompatActivity {

    private FloatingActionButton fab; //Boton flotante
    private Noticias_Adapter adapter; //Adaptar

    private DatabaseReference mDataBase; //BBDD
    private FirebaseAuth mAuth; //Firebase
    private SharedPreferences prefs;
    private ImageView imagenNoticia;
    private EditText tituloNoticia, textoNoticia;

    private String titulo, texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia_edit);

        Chat c=new Chat();
        tituloNoticia=findViewById(R.id.titulo_noticia_edit);
        textoNoticia=findViewById(R.id.texto_noticia_edit);

        imagenNoticia=(ImageView) findViewById(R.id.img_noticia_edit);

        //Instanciamos Firebase
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        //Toolbar
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        titulo= bundle.getString("name");
        texto=bundle.getString("textoNoticia");

        tituloNoticia.setText(titulo);
        textoNoticia.setText(texto);
        Picasso.get().load(bundle.getString("imagenSecundaria")).fit().into(imagenNoticia);


    }
}
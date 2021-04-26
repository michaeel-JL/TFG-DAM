package com.example.proyectodam.Activities.Secciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam.Adapter.ForoAdapter;
import com.example.proyectodam.Models.Chats;
import com.example.proyectodam.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NoticiasActivity extends AppCompatActivity {

    private FloatingActionButton fab; //Boton flotante
    private ForoAdapter adapter; //Adaptar

    private DatabaseReference mDataBase; //BBDD
    private FirebaseAuth mAuth; //Firebase
    private SharedPreferences prefs;
    private ImageView imagenNoticia;
    private TextView tituloNoticia, textoNoticia;


    private String titulo, texto;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);

        Chats c=new Chats();
        tituloNoticia=findViewById(R.id.tituloNoticia);
        textoNoticia=findViewById(R.id.textoNoticia);

        imagenNoticia=(ImageView) findViewById(R.id.imagenNoticia);

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
package com.example.final_proyect.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.final_proyect.Adapters.Noticias_Adapter;
import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class Noticia_Activity extends AppCompatActivity {

    private ImageView imagenNoticia;
    private TextView tituloNoticia, textoNoticia;
    private ImageButton ibtn_comentario;


    private String titulo, texto, id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);

        Chat c=new Chat();
        tituloNoticia = findViewById(R.id.titulo_noticia);
        textoNoticia = findViewById(R.id.texto_noticia);
        imagenNoticia = (ImageView) findViewById(R.id.imagen_noticia);
        ibtn_comentario = findViewById(R.id.comentarios_noticia);


        //Recuperamos los datos pasados
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        titulo = bundle.getString("name");
        texto = bundle.getString("textoNoticia");
        id = bundle.getString("id");

        tituloNoticia.setText(titulo);
        textoNoticia.setText(texto);
        Picasso.get().load(bundle.getString("imagenSecundaria")).fit().into(imagenNoticia);

        ibtn_comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Noticia_Activity.this, Comentarios_Activity.class);
                intent.putExtra("id_noticia", id);
                startActivity(intent);

            }
        });


    }
}
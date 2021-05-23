package com.example.final_proyect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_proyect.Adapters.Noticias_Adapter;
import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.Models.Noticia;
import com.example.final_proyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Noticia_Activity extends AppCompatActivity {

    private ImageView imagenNoticia;
    private TextView tituloNoticia, textoNoticia;
    private ImageButton ibtn_comentario, ibtn_megusta1, ibtn_megusta2;

    String uid;

    //Firebase
    FirebaseDatabase databse = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    private String titulo, texto, id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);

        //configuración Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_noticia);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.toolbar_edit_noticia);

        uid = user.getUid();

        Chat c=new Chat();
        tituloNoticia = findViewById(R.id.titulo_noticia);
        textoNoticia = findViewById(R.id.texto_noticia);
        imagenNoticia = (ImageView) findViewById(R.id.imagen_noticia);
        ibtn_comentario = findViewById(R.id.comentarios_noticia);
        ibtn_megusta1 = findViewById(R.id.megusta1_noticia);
        ibtn_megusta2 = findViewById(R.id.megusta2_noticia);

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

        //Comprobamos si ha dado like el usuario actual
        DatabaseReference ref_likes = databse.getReference("Likes").child(id);
        ref_likes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(uid).exists()){
                    ibtn_megusta2.setVisibility(View.VISIBLE); //dado like NEGRO
                    ibtn_megusta1.setVisibility(View.INVISIBLE); //no dado like BLANCO
                }else{
                    ibtn_megusta2.setVisibility(View.INVISIBLE); //dado like NEGRO
                    ibtn_megusta1.setVisibility(View.VISIBLE); //no dado like BLANCO
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //botono si ya ha dado like, quita su like
        ibtn_megusta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLike();
            }
        });

        //Boton para dar like
        ibtn_megusta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLike();
            }
        });

    }

    private void deleteLike() {
        //Buscamos cuantos likes tiene la noticia actual
        DatabaseReference ref_noticia = databse.getReference("Noticias").child(id);
        ref_noticia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int likes = snapshot.getValue(Noticia.class).getMegustas();
                ref_noticia.child("megustas").setValue(likes - 1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //Anadimos un like al dar al boton
        DatabaseReference ref_likes = databse.getReference("Likes").child(id);
        ref_likes.child(uid).removeValue();

    }

    private void addLike() {

        //Buscamos cuantos likes tiene la noticia actual
        DatabaseReference ref_noticia = databse.getReference("Noticias").child(id);
        ref_noticia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int likes = snapshot.getValue(Noticia.class).getMegustas();
                ref_noticia.child("megustas").setValue(likes + 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Anadimos un like al dar al boton
        DatabaseReference ref_likes = databse.getReference("Likes").child(id);
        ref_likes.child(uid).setValue(1);

    }

    //Botón atrás
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
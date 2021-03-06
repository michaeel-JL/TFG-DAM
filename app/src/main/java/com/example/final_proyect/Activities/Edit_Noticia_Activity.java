package com.example.final_proyect.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.final_proyect.Adapters.Noticias_Adapter;
import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.Profiles.Home_Admin_Activity;
import com.example.final_proyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Edit_Noticia_Activity extends AppCompatActivity {

    private FloatingActionButton fab; //Boton flotante
    private Noticias_Adapter adapter; //Adaptar
    private DatabaseReference mDataBase; //BBDD
    private FirebaseAuth mAuth; //Firebase
    private SharedPreferences prefs;
    private ImageView imagenNoticia;
    private EditText tituloNoticia, textoNoticia;

    private String titulo, texto, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia_edit);

        //configuración Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_edit_user);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.toolbar_edit_noticia);

        tituloNoticia= (EditText) findViewById(R.id.titulo_noticia_edit);
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
        id = bundle.getString("id");

        tituloNoticia.setText(titulo);
        textoNoticia.setText(texto);
        Picasso.get().load(bundle.getString("imagenSecundaria")).fit().into(imagenNoticia);

        //al clicar la imagen
        imagenNoticia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showAlertForCreatingNote();
            }
        });

        //boton guardar
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                DatabaseReference noticiasRef = ref.child("Noticias").child(id);
                noticiasRef.child("titulo").setValue(tituloNoticia.getText().toString());
                noticiasRef.child("textoNoticia").setValue(textoNoticia.getText().toString());
                onBackPressed();
            }
        });
    }
    //** Dialogs **/
    private void showAlertForCreatingNote() {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);

        builder.setTitle("Introduzca la URL de la imagen a cambiar");

        final EditText URL = new EditText(this);
        builder.setView(URL);


        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                DatabaseReference noticiasRef = ref.child("Noticias").child(id);

                if(URL.getText().length()==0){
                    Toast.makeText(getApplication(), "PROPORCIONE UN LINK VÁLIDO", Toast.LENGTH_SHORT).show();
                }else if(URL.getText().length()>0){
                    noticiasRef.child("imagenSecundaria").setValue(URL.getText().toString());
                    Picasso.get().load(URL.getText().toString()).fit().into(imagenNoticia);

                }

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_noticia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ic_delete:
                DatabaseReference ref_noticias = FirebaseDatabase.getInstance().getReference().child("Noticias").child(id);
                ref_noticias.removeValue();

                DatabaseReference ref_like = FirebaseDatabase.getInstance().getReference().child("Likes").child(id);
                ref_like.removeValue();

                DatabaseReference ref_comentarios = FirebaseDatabase.getInstance().getReference().child("Comentarios").child(id);
                ref_comentarios.removeValue();
                startActivity(new Intent(Edit_Noticia_Activity.this, Home_Admin_Activity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Botón atrás
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
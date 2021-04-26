package com.example.proyectodam.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodam.Activities.MostrarUsuariosActivity;
import com.example.proyectodam.Activities.Perfiles.AdminActivity;
import com.example.proyectodam.Activities.Secciones.NoticiasEditActivity;
import com.example.proyectodam.Adapter.ForoAdapter;
import com.example.proyectodam.Adapter.NoticiasAdapter;
import com.example.proyectodam.Models.Chats;
import com.example.proyectodam.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MostrarNoticiasActivity extends AppCompatActivity {

    private NoticiasAdapter adapter; //Adaptar
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Chats> chats;

    private DatabaseReference mDataBase; //BBDD
    private FirebaseAuth mAuth; //Firebase
    private SharedPreferences prefs;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticias_activity);

        //Instanciamos Firebase
        mAuth = FirebaseAuth.getInstance();
        mDataBase= FirebaseDatabase.getInstance().getReference();

        chats = new ArrayList<Chats>();
        layoutManager = new LinearLayoutManager(this);


        //Redimensionamos el recycler
        recyclerView= (RecyclerView) findViewById(R.id.recyclerAdminNoticias);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());






        //Metodo para cargar las tarjeta de los chats
        getChatsFromFirebase();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.noticiasItem);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                        switch (item.getItemId()) {
                            case R.id.perfilItemAdmin:
                                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.usuariosItem:
                                startActivity(new Intent(getApplicationContext(), MostrarUsuariosActivity.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.noticiasItem:

                                return true;

                        }

                        return false;
                    }
                });
    }








    //Lee los chats de Firebase
    private void getChatsFromFirebase() {

        mDataBase.child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ForoAdapter.OnItemClickListener itemListener = null;
                ForoAdapter.OnButtonClickListener btnListener = null;

                //Si hay datos...
                if (dataSnapshot.exists()) {
                    //Actuliza la lista
                    chats.clear();
                    //Busca todos los datos
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String titulo=ds.child("titulo").getValue().toString();
                        String description=ds.child("description").getValue().toString();
                        String imagePrincipal = ds.child("imagePrincipal").getValue().toString();
                        String imageSecundaria = ds.child("imagenSecundaria").getValue().toString();
                        String decNoticia = ds.child("textoNoticia").getValue().toString();

                        String id = ds.getKey();
                        //Creamos un chat
                        Chats chat = new Chats(id, titulo, description, imagePrincipal, imageSecundaria, decNoticia);
                        //Añadimos la ciudad al List
                        chats.add(chat);
                    }
                }

                //Adaptador
                adapter = new NoticiasAdapter(chats, R.layout.recicler_foro_admin, new NoticiasAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Chats chat, int position) {



                    }


                    //Boton editar
                }, new NoticiasAdapter.OnButtonClickListener() {
                    @Override
                    public void onButtonClick(Chats chat, int position) {
                        Intent intent = new Intent(MostrarNoticiasActivity.this, NoticiasEditActivity.class);
                        intent.putExtra("id", chats.get(position).getId());
                        intent.putExtra("name", chats.get(position).getTitulo());
                        intent.putExtra("description", chats.get(position).getDescription());
                        intent.putExtra("imagePrincipal", chats.get(position).getImagePrincipal());
                        intent.putExtra("imagenSecundaria", chats.get(position).getImagenSecundaria());
                        intent.putExtra("textoNoticia", chats.get(position).getTextoNoticia());
                        startActivity(intent);
                    }


                });

                //Cargamos el recycler metiendo el adaptador
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //Mensaje para confirmar la eliminación de borrar un user
    private void alertDelete(String nombreUser, String message, int position) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(nombreUser)
                .setMessage(message)
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(position); //Borramos la ciudad
                        //Mensaje
                        Toast.makeText(MostrarNoticiasActivity.this, "Ha sido borrado exitosamente.", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancelar", null).show();
    }


    //Metodo eliminar usuario de firebase
    private void deleteUser(int position) {
        String id  = chats.get(position).getId(); //Cogemos el ID de la tarjeta seleccionada
        mDataBase.child("chats").child(id).removeValue(); //Borramos a través del ID
        chats.clear(); //Limpiamos cada que eliminamos
    }

}
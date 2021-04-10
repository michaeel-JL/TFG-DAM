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

import com.example.proyectodam.Adapter.UserAdapter;
import com.example.proyectodam.Models.Usuarios;
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

public class MostrarUsuariosActivity  extends AppCompatActivity {

    private List<Usuarios> users;
    private UserAdapter adapter; //Adaptar
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference mDataBase; //BBDD
    private FirebaseAuth mAuth; //Firebase
    private SharedPreferences prefs;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuarios_activity);

        //Instanciamos Firebase
        mAuth = FirebaseAuth.getInstance();
        mDataBase= FirebaseDatabase.getInstance().getReference();

        users = new ArrayList<Usuarios>();
        layoutManager = new LinearLayoutManager(this);


        //Redimensionamos el recycler
        recyclerView= (RecyclerView) findViewById(R.id.recyclerAdmin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //Metodo para cargar las tarjeta de los usuarios
        getUsersFromFirebase();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.usuariosItem);

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

                                return true;

                        }

                        return false;
                    }
                });
    }







    //Lee las ciudades de Firebase
    private void getUsersFromFirebase() {

        mDataBase.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAdapter.OnItemClickListener itemListener = null;
                UserAdapter.OnButtonClickListener btnListener = null;

                //Si hay datos...
                if (dataSnapshot.exists()) {
                    //Actuliza la lista
                    users.clear();
                    //Busca todos los datos
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String nombreUsuario = ds.child("nombreUsuario").getValue().toString();
                        String edad = ds.child("edad").getValue().toString();
                        String linkImagen = ds.child("foto").getValue().toString();
                        String email = ds.child("email").getValue().toString();
                        String rol = ds.child("rol").getValue().toString();
                        String password = ds.child("password").getValue().toString();
                        System.out.println(rol);
                        String id  = ds.getKey();

                        //Creamos un ususario
                        Usuarios usuario = new Usuarios(id, nombreUsuario, edad, email, password, linkImagen, rol);
                        //Añadimos la ciudad al List
                        users.add(usuario);
                    }
                }

                //Adaptador
                adapter = new UserAdapter(users, R.layout.recicler_view_item, new UserAdapter.OnItemClickListener() {


                    @Override
                    public void onItemClick(Usuarios users, int position) {

                    }



                    //Boton eliminar
                }, new UserAdapter.OnButtonClickListener() {
                    @Override
                    public void onButtonClick(Usuarios users, int position) {
                        //Alerta para confirma borrar una ciudad
                        alertDelete("Borrar", "Estas seguro que quieres eliminar al usuario  " + users.getNombreUsuario()+ "?", position);
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
                        Toast.makeText(MostrarUsuariosActivity.this, "Ha sido borrado exitosamente.", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancelar", null).show();
    }


    //Metodo eliminar usuario de firebase
    private void deleteUser(int position) {
        String id  = users.get(position).getId(); //Cogemos el ID de la tarjeta seleccionada
        mDataBase.child("Usuarios").child(id).removeValue(); //Borramos a través del ID
        users.clear(); //Limpiamos cada que eliminamos
    }
}

package com.example.final_proyect.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.final_proyect.Adapters.User_Adapter;
import com.example.final_proyect.Models.Usuario;
import com.example.final_proyect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Usuarios_Fragment extends Fragment {

    private List<Usuario> users;
    private User_Adapter adapter; //Adaptar
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference mDataBase; //BBDD
    private FirebaseAuth mAuth; //Firebase
    private SharedPreferences prefs;
    private Context context;


    public Usuarios_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usuarios_, container, false);

        //Instanciamos Firebase
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        users = new ArrayList<Usuario>();
        layoutManager = new LinearLayoutManager(context);

        //Redimensionamos el recycler
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerAdmin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //Metodo para cargar las tarjeta de los usuarios
        getUsersFromFirebase();

        return view;

    }



    //Lee las ciudades de Firebase
    private void getUsersFromFirebase() {

        mDataBase.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User_Adapter.OnItemClickListener itemListener = null;
                User_Adapter.OnButtonClickListener btnListener = null;

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
                        Usuario usuario = new Usuario(id, nombreUsuario, edad, email, password, linkImagen, rol);
                        //Añadimos la ciudad al List
                        users.add(usuario);
                    }
                }

                //Adaptador
                adapter = new User_Adapter(users, R.layout.user_cardview, new User_Adapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(Usuario users, int position) {

                    }

                    //Boton eliminar
                }, new User_Adapter.OnButtonClickListener() {
                    @Override
                    public void onButtonClick(Usuario users, int position) {
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
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(nombreUser)
                .setMessage(message)
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(position); //Borramos la ciudad
                        //Mensaje
                        Toast.makeText(getActivity(), "Ha sido borrado exitosamente.", Toast.LENGTH_SHORT).show();

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
package com.example.final_proyect.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_proyect.Activities.Add_Dcotor_Activity;
import com.example.final_proyect.Activities.Edit_User_Activity;
import com.example.final_proyect.Adapters.User_Adapter;
import com.example.final_proyect.Models.Usuario;
import com.example.final_proyect.R;
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

        getActivity().setTitle("Usuarios");

        View view = inflater.inflate(R.layout.fragment_usuarios_, container, false);

        setHasOptionsMenu(true);


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


    //Lee los Usuarios de Firebase
    private void getUsersFromFirebase() {

        mDataBase.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User_Adapter.OnItemClickListener itemListener = null;

                //Si hay datos...
                if (dataSnapshot.exists()) {
                    //Actuliza la lista
                    users.clear();
                    //Busca todos los datos
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String nombreUsuario = ds.child("nombre").getValue().toString();
                        String edad = ds.child("edad").getValue().toString();
                        String linkImagen = ds.child("foto").getValue().toString();
                        String email = ds.child("email").getValue().toString();
                        String rol = ds.child("rol").getValue().toString();
                        String password = ds.child("password").getValue().toString();
                        String apellidos = ds.child("apellidos").getValue().toString();
                        String sexo = ds.child("sexo").getValue().toString();
                        System.out.println(rol);
                        String id = ds.getKey();

                        //Creamos un ususario
                        Usuario usuario = new Usuario(id, nombreUsuario, apellidos, sexo, edad, email, password, linkImagen, rol);
                        //AÃ±adimos la ciudad al List
                        users.add(usuario);
                    }
                }

                //Adaptador
                adapter = new User_Adapter(users, R.layout.cardview_user, new User_Adapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(Usuario user, int position) {

                        Intent intent = new Intent(getActivity(), Edit_User_Activity.class);
                        intent.putExtra("id", users.get(position).getId());
                        intent.putExtra("nombre", users.get(position).getNombre());
                        intent.putExtra("apellidos", users.get(position).getApellidos());
                        intent.putExtra("email", users.get(position).getEmail());
                        intent.putExtra("edad", users.get(position).getEdad());
                        intent.putExtra("foto", users.get(position).getFoto());
                        intent.putExtra("rol", users.get(position).getRol());
                        intent.putExtra("sexo", users.get(position).getSexo());
                        intent.putExtra("password", users.get(position).getPassword());

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.ic_add_user).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_add_user:
                Intent intent = new Intent(getActivity(), Add_Dcotor_Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
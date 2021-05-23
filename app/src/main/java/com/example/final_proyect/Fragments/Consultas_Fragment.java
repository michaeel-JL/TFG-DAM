package com.example.final_proyect.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.example.final_proyect.Adapters.User_List_Adapter;
import com.example.final_proyect.Models.Usuario;
import com.example.final_proyect.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Consultas_Fragment extends Fragment implements SearchView.OnQueryTextListener {

    User_List_Adapter adapter;
    ArrayList<Usuario> usersArrayList;
    SearchView svSearch;
    String rol, id_actual;
    RecyclerView rv;

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public Consultas_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Podemos cogerlos como referencia
        View view = inflater.inflate(R.layout.fragment_consultas, container, false);

        svSearch = view.findViewById(R.id.search_users);
        getActivity().setTitle("Consultas");
        svSearch.setOnQueryTextListener(this);

        LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(mLayoutManager);

        usersArrayList = new ArrayList<>();
        id_actual = user.getUid();

        adapter = new User_List_Adapter(usersArrayList, getContext());
        rv.setAdapter(adapter);

        //En funcion del rol muestra ciertos usuarios
        DatabaseReference ref_user = FirebaseDatabase.getInstance().getReference();
        ref_user.child("Usuarios").child(id_actual).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Buscamos el rol
                rol = snapshot.child("rol").getValue(String.class);

                //Cargamos los usuarios indicados
                if (rol.equals("usuario")) {
                    users_usuario();

                }else if (rol.equals("medico")){
                    users_medico();
                }else if(rol.equals("admin")){
                    users_admin();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return view;
    }

    private void users_admin() {
        //Leemos todos los usuarios
        DatabaseReference myref = database.getReference("Usuarios");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si existen leemos todos
                if (snapshot.exists()) {
                    usersArrayList.removeAll(usersArrayList);

                    //Comprobamos el rol de cada uno
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String rol = dataSnapshot.getValue(Usuario.class).getRol();

                        //Mostramos TODOS los usuarios
                        Usuario user = dataSnapshot.getValue(Usuario.class);
                        usersArrayList.add(user);

                    }
                } else {
                    Toast.makeText(getContext(), "No existen usuarios", Toast.LENGTH_SHORT).show();
                }
                adapter = new User_List_Adapter(usersArrayList, getContext());
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void users_medico() {
        final DatabaseReference ref_paciente = database.getReference("Chats");
        ref_paciente.child(id_actual).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                //Buscamos los id que tenga en chat
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String id_buscado = dataSnapshot.getKey();

                    //bucamos los usuarios que coincida el id_buscado
                    DatabaseReference myref = database.getReference("Usuarios");
                    myref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Si existen leemos todos
                            if (snapshot.exists()) {
                                usersArrayList.removeAll(usersArrayList);

                                //Comprobamos el rol de cada uno
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String id_consulta = dataSnapshot.getValue(Usuario.class).getId();

                                    //Mostramos solos a los medicos
                                    if (id_consulta.equals(id_buscado)) {
                                        Usuario user = dataSnapshot.getValue(Usuario.class);
                                        usersArrayList.add(user);
                                    }
                                }
                            } else {
                                Toast.makeText(getContext(), "No existen usuarios", Toast.LENGTH_SHORT).show();
                            }
                            adapter = new User_List_Adapter(usersArrayList, getContext());
                            rv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void users_usuario() {
        //Leemos todos los usuarios
        DatabaseReference myref = database.getReference("Usuarios");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si existen leemos todos
                if (snapshot.exists()) {
                    usersArrayList.removeAll(usersArrayList);

                    //Comprobamos el rol de cada uno
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String rol_u = dataSnapshot.getValue(Usuario.class).getRol();

                        //Mostramos solos a los medicos
                        if (rol_u.equals("medico")) {
                            Usuario user = dataSnapshot.getValue(Usuario.class);
                            usersArrayList.add(user);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "No existen usuarios", Toast.LENGTH_SHORT).show();
                }
                adapter = new User_List_Adapter(usersArrayList, getContext());
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }
}

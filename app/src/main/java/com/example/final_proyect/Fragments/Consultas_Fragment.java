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
    String rol;

    public Consultas_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Hace referencia nuetra bbdd
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Podemos cogerlos como referencia
        View view = inflater.inflate(R.layout.fragment_consultas, container, false);

        svSearch = view.findViewById(R.id.search_users);

        getActivity().setTitle("Consultas");

        svSearch.setOnQueryTextListener(this);

        RecyclerView rv;
        LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(mLayoutManager);

        usersArrayList = new ArrayList<>();
        String id = user.getUid();

        adapter = new User_List_Adapter(usersArrayList, getContext());
        rv.setAdapter(adapter);

        DatabaseReference ref_user = FirebaseDatabase.getInstance().getReference();
        ref_user.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Comprobamos que sea usuario
                rol = snapshot.child("rol").getValue(String.class);

                if (rol.equals("usuario")) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myref = database.getReference("Usuarios");

                    myref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                usersArrayList.removeAll(usersArrayList);

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    String rol = dataSnapshot.getValue(Usuario.class).getRol();

                                    //Mostramos solos a los medicos
                                    if (rol.equals("medico")) {
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
        return view;
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

package com.example.final_proyect.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.final_proyect.Adapters.User_List_Adapter;
import com.example.final_proyect.Models.Usuario;
import com.example.final_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Consultas_Fragment extends Fragment {

    public Consultas_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences mPref;

        ProgressBar progressBar;
        //Hace referencia a nuetra bbdd
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Podemos cogerlos como referencia
        View view = inflater.inflate(R.layout.fragment_consultas, container, false);

        TextView tv_user = view.findViewById(R.id.tv_user);
        ImageView img_user = view.findViewById(R.id.img_user);

        //Cargar la imagen del usuario
        //Glide.with(this).load(user.getPhotoUrl()).into(img_user);

        RecyclerView rv;
        ArrayList<Usuario> usersArrayList;
        User_List_Adapter adapter;
        LinearLayoutManager mLayoutManager;

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(mLayoutManager);

        usersArrayList = new ArrayList<>();
        adapter = new User_List_Adapter(usersArrayList, getContext());
        rv.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("Usuarios");

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    usersArrayList.removeAll(usersArrayList);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Usuario user = dataSnapshot.getValue(Usuario.class);
                        usersArrayList.add(user);
                    }
                    adapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(getContext(), "No existen usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}
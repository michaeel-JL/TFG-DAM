package com.example.proyectodam.Fragments;

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

import com.bumptech.glide.Glide;
import com.example.proyectodam.Adapter.ChatAdapter;
import com.example.proyectodam.Adapter.UserAdapter;
import com.example.proyectodam.Models.Usuarios;
import com.example.proyectodam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class usersFragment extends Fragment {
    public usersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ProgressBar progressBar;
        //Hace referencia a nuetra bbdd
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Podemos cogerlos como referencia
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        TextView tv_user = view.findViewById(R.id.tv_user);
        ImageView img_user = view.findViewById(R.id.img_user);

        progressBar = view.findViewById(R.id.progressbar);

        //Nos muestra el nombre del usuario que ha iniciado sesion
        assert user != null;
        tv_user.setText(user.getDisplayName());

        //Cargar la imagen del usuario
        Glide.with(this).load(user.getPhotoUrl()).into(img_user);

        RecyclerView rv;
        ArrayList<Usuarios>usersArrayList;
        ChatAdapter adapter;
        LinearLayoutManager mLayoutManager;

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(mLayoutManager);

        usersArrayList = new ArrayList<>();
        adapter = new ChatAdapter(usersArrayList, getContext());
        rv.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("Usuarios");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    rv.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    usersArrayList.removeAll(usersArrayList);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Usuarios user = dataSnapshot.getValue(Usuarios.class);
                        usersArrayList.add(user);
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    progressBar.setVisibility(View.GONE);
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
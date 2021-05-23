package com.example.final_proyect.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.example.final_proyect.Activities.Noticia_Activity;
import com.example.final_proyect.Activities.Edit_Noticia_Activity;
import com.example.final_proyect.Activities.Add_Noticia_Activity;
import com.example.final_proyect.Adapters.Noticias_Adapter;
import com.example.final_proyect.Models.Noticia;
import com.example.final_proyect.Models.Usuario;
import com.example.final_proyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Noticias_Fragment extends Fragment  {

    private FloatingActionButton fab; //Boton flotante
    private List<Noticia> noticias;
    private Noticias_Adapter adapter; //Adaptar
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mDataBase; //BBDD
    private FirebaseAuth mAuth; //Firebase
    private SharedPreferences prefs;
    private Context context;
    private String rol;

    private MenuItem item;

    FirebaseDatabase databse = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    public Noticias_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_noticias, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_notificas_f);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.toolbar_noticias);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        //Metodo para cargar las tarjeta de las ciudades
        getNoticiasFromFirebase();

        getActivity().setTitle("Noticias");

        //Array de Noticias
        noticias = new ArrayList<Noticia>();

        setHasOptionsMenu(true);

        layoutManager = new LinearLayoutManager(context);

        //Redimensionamos el recycler
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_noticias);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new Noticias_Adapter(noticias, R.layout.cardview_noticia);
        recyclerView.setAdapter(adapter);

        return view;

    }

    //Lee los noticias de Firebase
    private void getNoticiasFromFirebase() {

        mDataBase.child("Noticias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Noticias_Adapter.OnItemClickListener itemListener = null;
                Noticias_Adapter.OnButtonClickListener btnListener = null;

                //Si hay datos...
                if (dataSnapshot.exists()) {
                    //Actuliza la lista
                    noticias.clear();
                    //Busca todos los datos
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String titulo=ds.child("titulo").getValue().toString();
                        String description=ds.child("description").getValue().toString();
                        String imagePrincipal = ds.child("imagePrincipal").getValue().toString();
                        String imageSecundaria = ds.child("imagenSecundaria").getValue().toString();
                        String decNoticia = ds.child("textoNoticia").getValue().toString();
                        String etiqueta = ds.child("etiqueta").getValue().toString();
                        String slikes = ds.child("megustas").getValue().toString();
                        String scomentarios = ds.child("comentarios").getValue().toString();
                        String fecha = ds.child("fecha").getValue().toString();
                        String hora = ds.child("hora").getValue().toString();

                        int likes = Integer.parseInt(slikes);
                        int comentarios = Integer.parseInt(scomentarios);

                        String id = ds.getKey();
                        //Creamos un chat
                        Noticia noticia = new Noticia(id, titulo, description, imagePrincipal, imageSecundaria, decNoticia, etiqueta, fecha, hora, likes, comentarios);
                        //Añadimos la ciudad al List
                        noticias.add(noticia);

                        Collections.sort(noticias, new Comparator<Noticia>() {
                            @Override
                            public int compare(Noticia noticia, Noticia t1) {
                                return noticia.getFecha().compareTo(t1.getFecha());
                            }
                        });

                        Collections.sort(noticias, new Comparator<Noticia>() {
                            @Override
                            public int compare(Noticia noticia, Noticia t1) {
                                return noticia.getFecha().compareTo(t1.getHora());
                            }
                        });


                    }
                }

                //Adaptador

                adapter = new Noticias_Adapter(noticias, R.layout.cardview_noticia, new Noticias_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Noticia noticia, int position) {

                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getUid();

                        DatabaseReference ref;
                        ref = FirebaseDatabase.getInstance().getReference();

                        // Agregamos un listener a la referencia
                        ref.child("Usuarios").child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {

                                    rol = dataSnapshot.child("rol").getValue(String.class);
                                    if(rol.equals("admin")){
                                        Intent intent = new Intent(getActivity(), Edit_Noticia_Activity.class);
                                        intent.putExtra("id", noticias.get(position).getId());
                                        intent.putExtra("name", noticias.get(position).getTitulo());
                                        intent.putExtra("description", noticias.get(position).getDescription());
                                        intent.putExtra("imagePrincipal", noticias.get(position).getImagePrincipal());
                                        intent.putExtra("imagenSecundaria", noticias.get(position).getImagenSecundaria());
                                        intent.putExtra("textoNoticia", noticias.get(position).getTextoNoticia());
                                        intent.putExtra("etiqueta", noticias.get(position).getEtiqueta());
                                        startActivity(intent);


                                    }else{
                                        Intent intent = new Intent(getActivity(),  Noticia_Activity.class);
                                        intent.putExtra("id", noticias.get(position).getId());
                                        intent.putExtra("name", noticias.get(position).getTitulo());
                                        intent.putExtra("description", noticias.get(position).getDescription());
                                        intent.putExtra("imagePrincipal", noticias.get(position).getImagePrincipal());
                                        intent.putExtra("imagenSecundaria", noticias.get(position).getImagenSecundaria());
                                        intent.putExtra("textoNoticia", noticias.get(position).getTextoNoticia());
                                        intent.putExtra("etiqueta", noticias.get(position).getEtiqueta());
                                        startActivity(intent);


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("Error de lectura: " + databaseError.getCode());
                            }
                        });



                    }
                    //Boton edit
                }, new Noticias_Adapter.OnButtonClickListener() {
                    @Override
                    public void onButtonClick(Noticia noticia, int position) {

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

        DatabaseReference ref_usuarios = databse.getReference("Usuarios").child(uid);
        ref_usuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String rol = snapshot.getValue(Usuario.class).getRol();
                if (rol.equals("admin")){
                    menu.findItem(R.id.ic_add).setVisible(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ic_add:
                Intent intent = new Intent(getActivity(), Add_Noticia_Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
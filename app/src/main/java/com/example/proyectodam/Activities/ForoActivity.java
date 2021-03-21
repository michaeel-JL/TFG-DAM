package com.example.proyectodam.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodam.Adapter.ForoAdapter;
import com.example.proyectodam.Models.Chats;
import com.example.proyectodam.R;
import com.example.proyectodam.Models.Chats;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ForoActivity extends AppCompatActivity {


    private FloatingActionButton fab; //Boton flotante
    private List<Chats> chats;
    private ForoAdapter adapter; //Adaptar
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mDataBase; //BBDD
    private FirebaseAuth mAuth; //Firebase
    private SharedPreferences prefs;
    private Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foro_activity);


        //Obtenemos el usuario cuya sesión está abierta
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();


        //Instanciamos Firebase
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();


        //Metodo para cargar las tarjeta de las ciudades
        getChatsFromFirebase();


        chats = new ArrayList<Chats>();
        layoutManager = new LinearLayoutManager(context);


        //Redimensionamos el recycler
        recyclerView= (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //cambio de layout
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.foroItem);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                        switch (item.getItemId()) {
                            case R.id.perfilItem:
                                startActivity(new Intent(getApplicationContext(), com.example.proyectodam.Activities.PacienteActivity.class));
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.consultaItem:
                                startActivity(new Intent(getApplicationContext(), ConsultaActivity.class));
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.foroItem:

                                return true;
                        }

                        return false;
                    }
                });


    }

    //Lee las ciudades de Firebase
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
                        String titulo="casa";
                        //String titulo = ds.child("titulo").getValue().toString();
                        System.out.print("TITULOOO-------------"+titulo);
                        String image = ds.child("image").getValue().toString();
                        System.out.println(image);
                        String id = ds.getKey();
                        //Creamos una ciudad

                        System.out.println(image);

                        Chats chat = new Chats(id, titulo, image);
                        //Añadimos la ciudad al List
                        chats.add(chat);
                    }
                }

                //Adaptador
                adapter = new ForoAdapter(chats, R.layout.recicler_foro, new ForoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Chats chat, int position) {
                   /*     //En caso de dar click a la tarjeta enviamos los datos
                        Intent intent = new Intent(CityActivity.this, Add_Edit_City_Activity.class);
                        intent.putExtra("id", cities.get(position).getId());
                        intent.putExtra("name", cities.get(position).getName());
                        intent.putExtra("description", cities.get(position).getDescription());
                        intent.putExtra("link", cities.get(position).getImage());

                        float ss = cities.get(position).getStars();
                        String enviar = Float.toString(ss);

                        intent.putExtra("stars", enviar);
                        startActivity(intent);*/
                    }


                    //Boton eliminar
                }, new ForoAdapter.OnButtonClickListener() {
                    @Override
                    public void onButtonClick(Chats chat, int position) {

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
}

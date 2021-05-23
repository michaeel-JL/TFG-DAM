package com.example.final_proyect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.final_proyect.Adapters.Enfermedades_Adapter;
import com.example.final_proyect.Models.Enfermedad;
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

public class Enfermedades_Activity extends AppCompatActivity {

    ArrayList<Enfermedad> enfermedadList;
    Enfermedades_Adapter adapter;
    RecyclerView rv;

    //BBDD
    private DatabaseReference mDataBase;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enfermedades);

        //configuración Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_enfermedades);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.toolbar_enfermedades);

        LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv = findViewById(R.id.enfermedades_rv);
        rv.setLayoutManager(mLayoutManager);

        //Realizamos las consultas necesarias a FIREBASE
        consultasFirebase();

    }

    private void consultasFirebase() {
        enfermedadList = new ArrayList<>();
        adapter = new Enfermedades_Adapter(enfermedadList,this);
        rv.setAdapter(adapter);

        //Comprobamos que rol tiene el usuario actual
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Usuarios").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String rol = snapshot.getValue(Usuario.class).getRol();
                if(rol.equals("medico")){
                    //Recogemos el id del PACIENTE y buscamos sus alergias
                    String id_paciente = getIntent().getExtras().getString("id_paciente");
                    ref.child("Enfermedades").child(id_paciente).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            mostrarEnfermedades(snapshot);
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }else if(rol.equals("usuario")){
                    //Cargamos las alergias del usuario ACTUAl
                    ref.child("Enfermedades").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            mostrarEnfermedades(snapshot);
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void mostrarEnfermedades(DataSnapshot snapshot) {
        if (snapshot.exists()){

            enfermedadList.removeAll(enfermedadList);

            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                Enfermedad enfermedad = dataSnapshot.getValue(Enfermedad.class);
                enfermedadList.add(enfermedad);
                setScroll();
            }
        }
        adapter = new Enfermedades_Adapter(enfermedadList, getBaseContext());
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setScroll() {
        rv.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.ic_add).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ic_add:
                Intent intent = new Intent(this, Add_Enfermedad_Activity.class);
                intent.putExtra("editar", "no");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        consultasFirebase();
    }

    //Botón atrás
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
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
import android.widget.Spinner;

import com.example.final_proyect.Adapters.Alergias_Adapter;
import com.example.final_proyect.Models.Alergia;
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

public class Alergias_Activity extends AppCompatActivity {

    ArrayList<Alergia> alergiaList;
    Alergias_Adapter adapter;
    RecyclerView rv;
    Spinner spn_gravedad;

    //BBDD
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alergias);

        //configuración Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_alergias);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.toolbar_alergias);




        spn_gravedad = findViewById(R.id.add_alergia_spn_gravedad);

        LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(mLayoutManager);

        alergiaList = new ArrayList<>();

        adapter = new Alergias_Adapter(alergiaList,this);
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
                    ref.child("Alergias").child(id_paciente).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            mostrarAlergias(snapshot);
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });

                }else if(rol.equals("usuario")){
                    //Cargamos las alergias del usuario ACTUAl
                    ref.child("Alergias").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            mostrarAlergias(snapshot);
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

    private void mostrarAlergias(DataSnapshot snapshot) {

        if (snapshot.exists()){
            alergiaList.removeAll(alergiaList);
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                Alergia alergia = dataSnapshot.getValue(Alergia.class);
                alergiaList.add(alergia);
                setScroll();
            }
        }
        adapter = new Alergias_Adapter(alergiaList, getBaseContext());
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
        ref.child("Usuarios").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String rol = snapshot.getValue(Usuario.class).getRol();
                if (rol.equals("usuario")){
                    menu.findItem(R.id.ic_add).setVisible(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ic_add:
                Intent intent = new Intent(this, Add_Alergia_Activity.class);
                intent.putExtra("editar", "no");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Botón atrás
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
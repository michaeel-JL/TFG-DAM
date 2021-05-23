package com.example.final_proyect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.final_proyect.Models.Alergia;
import com.example.final_proyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Add_Alergia_Activity extends AppCompatActivity {

    private EditText alergia_name, alergia_details;
    private DatabaseReference mDataBase; //BBDD
    private Spinner spn_gravedad;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String id_alergia, editar_alergia, uid;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alergia);

        //configuración Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_add_alergia);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.toolbar_add_alergia);

        //Capturamos el id del layout
        alergia_name = findViewById(R.id.add_alergia_nombre);
        alergia_details = findViewById(R.id.add_alergia_detalles);
        spn_gravedad = (Spinner) findViewById(R.id.add_alergia_spn_gravedad);
        fab = findViewById(R.id.add_alergia_btn_save);

        //Cogemos el id del usuario
        uid = user.getUid();

        //Cogemos algunos datos pasados por intent
        editar_alergia = getIntent().getExtras().getString("editar");
        id_alergia = getIntent().getExtras().getString("id_alergia");


        //Comprobamos si hay que editar o si es el medico
        if(editar_alergia.equals("si")){
            cargarDatos();
        }else if (editar_alergia.equals("vista_medico")){
            cargarDatos();
            noEdit();
        }

        //Boton guardar
        fab.setOnClickListener(view -> {
            mDataBase = FirebaseDatabase.getInstance().getReference();
            FirebaseDatabase databse = FirebaseDatabase.getInstance();
            DatabaseReference ref_alergias = databse.getReference("Alergias").child(uid);

            if (!alergia_name.getText().toString().isEmpty() && !alergia_details.getText().toString().isEmpty()) {

                Alergia alergia = new Alergia();
                alergia.setNombre(alergia_name.getText().toString());
                alergia.setDescripcion(alergia_details.getText().toString());
                alergia.setGravedad(spn_gravedad.getSelectedItem().toString());

                //Comprobamos si hay que editar o crear uno nuevo
                if (editar_alergia.equals("si")) {
                    alergia.setId(id_alergia);
                    ref_alergias.child(id_alergia).setValue(alergia);
                } else {
                    String id = ref_alergias.push().getKey();
                    alergia.setId(id);
                    ref_alergias.child(alergia.getId()).setValue(alergia);
                }
                onBackPressed();
            }else{
                Toast.makeText(this, "No pueden haber campos vacíos", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void noEdit() {
        alergia_name.setEnabled(false);
        alergia_details.setEnabled(false);
        spn_gravedad.setEnabled(false);
        fab.setVisibility(View.INVISIBLE);
    }

    private void cargarDatos() {
        String nombre_alergia = getIntent().getExtras().getString("n_alergia");
        String gravedad_alergia = getIntent().getExtras().getString("gravedad_alergia");
        String descripcion_alergia = getIntent().getExtras().getString("descripcion_alergia");

        //Nos devuelve la posicion del spinner
        int pos = positionSpiner(gravedad_alergia);

        alergia_name.setText(nombre_alergia);
        alergia_details.setText(descripcion_alergia);
        spn_gravedad.setSelection(pos);
    }

    private int positionSpiner(String gravedad_alergia) {

        //Comprobamos el tipo
        if (gravedad_alergia.equals("Débil")){
            return 0;
        }else if(gravedad_alergia.equals("Normal")){
            return 1;
        }else if(gravedad_alergia.equals("Fuerte")){
            return 2 ;
        }
        return 1;
    }

    //Botón atrás
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete_noticia, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if(editar_alergia.equals("si")) {
            menu.findItem(R.id.ic_delete).setVisible(true);
        }else{
            menu.findItem(R.id.ic_delete).setVisible(false);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ic_delete:

                DatabaseReference ref_alergias = FirebaseDatabase.getInstance().getReference().child("Alergias").child(uid).child(id_alergia);
                ref_alergias.removeValue();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
package com.example.final_proyect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.final_proyect.Models.Enfermedad;
import com.example.final_proyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Add_Enfermedad_Activity extends AppCompatActivity {
    EditText etxt_nombre, etxt_detalles, etxt_diag, etxt_resol;
    String fecha_diag, fecha_resol;
    String id_enfermedad;

    private DatabaseReference mDataBase; //BBDD
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_enfermedad);

        //configuración Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_add_enfermedad);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.toolbar_add_enfermedad);

        etxt_diag = findViewById(R.id.add_enfermedad_fecha_diag);
        etxt_resol = findViewById(R.id.add_enfermedad_fecha_resol);
        etxt_nombre = findViewById(R.id.add_enfermedad_nombre);
        etxt_detalles = findViewById(R.id.add_enfermedad_detalles);
        fab = findViewById(R.id.add_enfermedad_btn_save);

        String uid = user.getUid();
        String editar_enfermedad = getIntent().getExtras().getString("editar");

        if(editar_enfermedad.equals("si")){
            cargarDatos();
        }else if (editar_enfermedad.equals("vista_medico")){
            cargarDatos();
            noEdit();
        }


        //Boton guardar
        fab.setOnClickListener(view -> {
            mDataBase = FirebaseDatabase.getInstance().getReference();
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference ref_enfermedades = database.getReference("Enfermedades").child(uid);
            ref_enfermedades.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Enfermedad enfermedad = new Enfermedad();
                    enfermedad.setNombre(etxt_nombre.getText().toString());
                    enfermedad.setDetalles(etxt_detalles.getText().toString());

                    if (editar_enfermedad.equals("si")){
                        enfermedad.setId(id_enfermedad);
                        enfermedad.setFecha_diagnostico(fecha_diag);
                        enfermedad.setFecha_resolucion(fecha_resol);
                        ref_enfermedades.child(id_enfermedad).setValue(enfermedad);

                    }else{
                        String id = ref_enfermedades.push().getKey();
                        enfermedad.setFecha_diagnostico(fecha_diag);
                        enfermedad.setFecha_resolucion(fecha_resol);
                        enfermedad.setId(id);
                        ref_enfermedades.child(enfermedad.getId()).setValue(enfermedad);
                    }
                    onBackPressed();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
    }

    private void noEdit() {

        etxt_nombre.setEnabled(false);
        etxt_detalles.setEnabled(false);
        etxt_diag.setEnabled(false);
        etxt_resol.setEnabled(false);
        fab.setVisibility(View.INVISIBLE);
    }

    private void cargarDatos() {

        String nombre_enfermedad = getIntent().getExtras().getString("nombre_enfermedad");
        String detalles_enfermedad = getIntent().getExtras().getString("detalles_enfermedad");
        fecha_diag = getIntent().getExtras().getString("fecha_d_enfermedad");
        fecha_resol = getIntent().getExtras().getString("fecha_r_enfermedad");
        id_enfermedad = getIntent().getExtras().getString("id_enfermedad");

        etxt_nombre.setText(nombre_enfermedad);
        etxt_detalles.setText(detalles_enfermedad);
        etxt_diag.setText("Diagnosticado el: " + fecha_diag);
        etxt_resol.setText("Diagnosticado el: " + fecha_resol);

    }

    public void openCalendarD(View view){
        Calendar cal = Calendar.getInstance();
        int anio = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(Add_Enfermedad_Activity.this, (view1, year, month, dayOfMonth) -> {

            fecha_diag = dayOfMonth + "/" + month + "/" + year;
            etxt_diag.setText("Diagnosticado el: " + fecha_diag);
        }, anio, mes, dia);
        dpd.show();
    }

    public void openCalendarR(View view){
        Calendar cal = Calendar.getInstance();
        int anio = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(Add_Enfermedad_Activity.this, (view1, year, month, dayOfMonth) -> {

            fecha_resol = dayOfMonth + "/" + month + "/" + year;
            etxt_resol.setText("Diagnosticado el: " + fecha_resol);
        }, anio, mes, dia);
        dpd.show();
    }
}
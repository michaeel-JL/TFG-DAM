package com.example.final_proyect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
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
    EditText nombre, detalles, etxt_diag, etxt_resol;
    String fecha_diag, fecha_resol;

    private DatabaseReference mDataBase; //BBDD
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_enfermedad);
        setTitle("Añadir Enfermedad");

        etxt_diag = findViewById(R.id.add_enfermedad_fecha_diag);
        etxt_resol = findViewById(R.id.add_enfermedad_fecha_resol);

        nombre = findViewById(R.id.add_enfermedad_nombre);
        detalles = findViewById(R.id.add_enfermedad_detalles);

        String uid = user.getUid();

        //Boton guardar
        FloatingActionButton fab = findViewById(R.id.add_enfermedad_btn_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataBase = FirebaseDatabase.getInstance().getReference();
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference ref_enfermedades = database.getReference("Enfermedades").child(uid);
                ref_enfermedades.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String id = ref_enfermedades.push().getKey();

                        Enfermedad enfermedad = new Enfermedad();
                        enfermedad.setNombre(nombre.getText().toString());
                        enfermedad.setDetalles(detalles.getText().toString());
                        enfermedad.setFecha_diagnostico(fecha_diag);
                        enfermedad.setFecha_resolucion(fecha_resol);
                        enfermedad.setId(id);

                        ref_enfermedades.child(enfermedad.getId()).setValue(enfermedad);
                        onBackPressed();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });





    }

    public void openCalendarD(View view){
        Calendar cal = Calendar.getInstance();
        int anio = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(Add_Enfermedad_Activity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                fecha_diag = dayOfMonth + "/" + month + "/" + year;
                etxt_diag.setText("Diagnosticado el: " + fecha_diag);
            }
        }, anio, mes, dia);
        dpd.show();
    }

    public void openCalendarR(View view){
        Calendar cal = Calendar.getInstance();
        int anio = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(Add_Enfermedad_Activity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                fecha_resol = dayOfMonth + "/" + month + "/" + year;
                etxt_resol.setText("Diagnosticado el: " + fecha_resol);
            }
        }, anio, mes, dia);
        dpd.show();
    }
}
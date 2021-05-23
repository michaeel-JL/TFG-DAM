package com.example.final_proyect.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.final_proyect.Models.Noticia;
import com.example.final_proyect.Models.Usuario;
import com.example.final_proyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Edit_User_Activity extends AppCompatActivity {

    private String id, nombre, apellidos, edad, sexo, email, rol , password, foto;
    private EditText edit_name, edit_apellidos, edit_edad, edit_email, edit_rol;
    private RadioButton radioButtonF, radioButtonM;
    private Spinner spinner;

    private String spin_rol_a,  spin_rol_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        //configuración Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_edit_user);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.toolbar_edit_user);

        //Elementos
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup_edit);
        radioButtonM=(RadioButton)findViewById(R.id.radio_masculino_edit);
        radioButtonF=(RadioButton)findViewById(R.id.radio_femenino_edit);
        edit_name=(EditText)findViewById(R.id.txt_nombre);
        edit_apellidos=(EditText) findViewById(R.id.txt_apellidos);
        edit_edad=(EditText) findViewById(R.id.txt_edad);
        edit_email=(EditText) findViewById(R.id.txt_email);
        spinner=(Spinner)findViewById(R.id.spn_rol);


        //Traemos los datos de la tarjeta
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id= bundle.getString("id");
        nombre= bundle.getString("nombre");
        apellidos=bundle.getString("apellidos");
        edad = bundle.getString("edad");
        sexo = bundle.getString("sexo");
        email = bundle.getString("email");
        rol = bundle.getString("rol");
        password=bundle.getString("password");
        foto=bundle.getString("foto");

        //aÃ±adimos los datos al EditText
        edit_name.setText(nombre);
        edit_apellidos.setText(apellidos);
        edit_edad.setText(edad);
        edit_email.setText(email);


        //Comprobacion
        if(rol.equals("medico")){
            //Roles restantes
            spin_rol_a="paciente";
            spin_rol_b="admin";
        }else if(rol.equals("paciente")){
            //Roles restantes
            spin_rol_a="medico";
            spin_rol_b="admin";
        }else if(rol.equals("admin")){
            //Roles restantes
            spin_rol_a="paciente";
            spin_rol_b="medico";
        }

        //Spinner
        String[] roles = {rol,spin_rol_a,spin_rol_b};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, roles));

        if(sexo.equals("Hombre")){
            radioButtonM.setChecked(true);
        }else{
            radioButtonF.setChecked(true);

        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_femenino_edit:
                        sexo="Mujer";
                        break;
                    case R.id.radio_masculino_edit:
                        sexo="Hombre";
                        break;

                }
            }
        });


        //boton guardar
        FloatingActionButton fab = findViewById(R.id.fabSaveUser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                DatabaseReference usersRef = ref.child("Usuarios").child(id);

                //Usuario
                Usuario usuario = new Usuario();
                usuario.setNombre(edit_name.getText().toString());
                usuario.setApellidos(edit_apellidos.getText().toString());
                usuario.setEmail(edit_email.getText().toString());
                usuario.setEdad(edit_edad.getText().toString());
                usuario.setPassword(password);
                usuario.setFoto(foto);
                usuario.setId(id);
                usuario.setSexo(sexo);
                usuario.setRol(spinner.getSelectedItem().toString());

                usersRef.setValue(usuario);
                onBackPressed();
            }
        });
    }

    //Botón atrás
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
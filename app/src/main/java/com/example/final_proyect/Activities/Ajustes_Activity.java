package com.example.final_proyect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_proyect.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class Ajustes_Activity extends AppCompatActivity {

    private String nombre, apellidos, email, edad, foto, sexo;
    private EditText editEdad, editTextNombre, editTextApellidos, editTextPassword;
    private Button btnGuardar, button_change_password, button_change_email;
    private RadioButton radioButtonF, radioButtonM;
    private   RadioGroup rg;
    private TextView botonCambiarFoto;
    private Uri uri;
    private CircleImageView imageProfile;
    private StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        //configuración Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_ajustes);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.toolbar_ajustes);



        //Elementos
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        radioButtonM=(RadioButton)findViewById(R.id.ajustes_masculino);
        radioButtonF=(RadioButton)findViewById(R.id.ajustes_femenino);
        editEdad=findViewById(R.id.ajustes_edad);
        editTextNombre=findViewById(R.id.ajustes_nombre);
        editTextApellidos=findViewById(R.id.ajustes_apellidos);
        botonCambiarFoto=findViewById(R.id.ajustes_img_user);
        imageProfile = (CircleImageView) findViewById(R.id.img_perfil_ajustes);
        mStorage = FirebaseStorage.getInstance().getReference();
        button_change_password=findViewById(R.id.button_change_password);
        button_change_email=findViewById(R.id.button_change_email);

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Ajustes_Activity.this, Cambiar_Contrasena_Activity.class);
                startActivity(intent);
            }

        });


        button_change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Ajustes_Activity.this, Cambiar_Email_Activity.class);
                startActivity(intent);
            }

        });


        //Traemos los datos de firebase
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference();

        // Agregamos un listener a la referencia
        ref.child("Usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    //usuario
                    nombre = dataSnapshot.child("nombre").getValue(String.class);
                    editTextNombre.setText(nombre);
                    //edad
                    edad = dataSnapshot.child("edad").getValue(String.class);
                    editEdad.setText(edad);
                    //Apellidos
                    apellidos =  dataSnapshot.child("apellidos").getValue(String.class);
                    editTextApellidos.setText(apellidos);

                    //Foto perfil
                    foto = dataSnapshot.child("foto").getValue(String.class).toString();
                    cargarImagen(foto);



                    //sexo
                    sexo = dataSnapshot.child("sexo").getValue(String.class);
                    if(sexo.equals("Hombre")){
                        radioButtonM.setChecked(true);
                    }else{
                        radioButtonF.setChecked(true);

                    }

                    rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                    {
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch(checkedId){
                                case R.id.ajustes_femenino:
                                    sexo="Mujer";
                                    break;
                                case R.id.ajustes_masculino:
                                    sexo="Hombre";
                                    break;

                            }
                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error de lectura: " + databaseError.getCode());
            }
        });








        btnGuardar=findViewById(R.id.ajustes_btn_save);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();

                DatabaseReference ref;
                ref = FirebaseDatabase.getInstance().getReference();


                DatabaseReference usersRef = ref.child("Usuarios").child(uid);

                usersRef.child("edad").setValue(editEdad.getText().toString());
                usersRef.child("apellidos").setValue(editTextApellidos.getText().toString());
                usersRef.child("nombre").setValue(editTextNombre.getText().toString());
                usersRef.child("sexo").setValue(sexo);


                //Subimos la foto
                //SUBIR IMAGEN
                if (uri!=null) {
                    //MARCAMOS LA RUTA DE LAS IMAGENES EN EL STORAGE
                    final StorageReference filePath = mStorage.child("Fotos de perfil").child(uri.getLastPathSegment());
                    //COGEMOS ESA RUTA Y LE METEMOS LA URI, QUE ES LA FOTO SELECCIONADA, GUARDADA ANTERIORMENTE EN EL ANTERIOR ON CLICK
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //GUARDA LA URL DE LA ULTIMA FOTO SUBIDA
                            Task<Uri> downloadUrl = mStorage.child("Fotos de perfil").child(uri.getLastPathSegment()).
                                    getDownloadUrl();
                            //SI ES CORRECTO ENTRAMOS AQUI
                            downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri2) {
                                    //URI DOS CORRESPONDE A downloadUrl Y SE LO AÑADIMOS A UN STRING QUE LE PASAMOS AL OBJETO
                                    foto = uri2.toString();
                                    usersRef.child("foto").setValue(foto);

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplication(), "ERROR AL CARGAR LA IMAGEN", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                onBackPressed();

            }
        });


        //boton cambiar foto perfil
        botonCambiarFoto = (TextView) findViewById(R.id.ajustes_img_user);

        botonCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                CropImage.activity(uri).setAspectRatio(1, 1)
                        .start(Ajustes_Activity.this);
            }
        });


    }


    private void cargarImagen(String link) {
        Picasso.get()
                .load(link)
                .fit()

                .into(imageProfile);
    }




    //PARA CARGAR LA FOTO DENTRO DEL URI

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if(resultCode == Activity.RESULT_OK){
                uri=result.getUri();
                imageProfile.setImageURI(uri);
            }
        } else{
            Toast.makeText(this,"Error, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
        }
        //SI ES CORRECTO
    }
}
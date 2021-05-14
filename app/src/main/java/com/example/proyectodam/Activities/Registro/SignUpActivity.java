package com.example.proyectodam.Activities.Registro;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam.Models.Usuarios;
import com.example.proyectodam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    private EditText editEmail, editNombreUsuario, editEdad, editContraseña, editRepetirContraseña;
    private ToggleButton togglerol;
    private CircleImageView imageProfile;
    private Button botonRegistrar, botonLogin;
    private TextView botonAñadirFoto;
    private String email, nombreUsuario, edad, contraseña, repetirContraseña, stringFoto;
    private String rol="paciente";
    private String recordar = "false";
    //PARA LA FOTO DE PERFIL
    private static final int GALLERY_INTENT = 1;
    private Uri uri;
    //OBJETO PARA LA BASE DE DATOS
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        editEmail = (EditText) findViewById(R.id.sign_email);
        editNombreUsuario = (EditText) findViewById(R.id.sign_nombre);
        editEdad = (EditText) findViewById(R.id.sign_Edad);
        editContraseña = (EditText) findViewById(R.id.sign_input_password);
        editRepetirContraseña = (EditText) findViewById(R.id.sign_input_password_repetir);
        togglerol=(ToggleButton)findViewById(R.id.toggleButtonRol);
        imageProfile = (CircleImageView) findViewById(R.id.sign_profile_image);
        botonAñadirFoto = (TextView) findViewById(R.id.sign_image_profile_btn);
        botonAñadirFoto.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                CropImage.activity(uri).setAspectRatio(1, 1)
                        .start(SignUpActivity.this);
            }
        });



        botonRegistrar = (Button) findViewById(R.id.sign_btn);
        botonRegistrar.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                registrarUsuario();

            }
        });

        botonLogin = (Button) findViewById(R.id.sign_btn_login);
        botonLogin.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        togglerol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is paciente
                    Toast.makeText(getApplication(), "Medico", Toast.LENGTH_SHORT).show();
                    rol="medico";

                } else {
                    // The toggle is medico

                    Toast.makeText(getApplication(), "Paciente", Toast.LENGTH_SHORT).show();
                    rol="paciente";
                }
            }
        });


    }
    private void registrarUsuario() {
        //COGEMOS LO DE LOS EDIT Y LOS SPINNER
        email = editEmail.getText().toString();
        nombreUsuario = editNombreUsuario.getText().toString();
        edad = editEdad.getText().toString();
        contraseña = editContraseña.getText().toString();
        repetirContraseña = editRepetirContraseña.getText().toString();


        recordar = "false";


        //VALIDACIONES
        if (!email.isEmpty() && !nombreUsuario.isEmpty() &&  !edad.isEmpty() && !contraseña.isEmpty() &&
                !repetirContraseña.isEmpty() && !email.trim().equals("") && !nombreUsuario.trim().equals("") && !edad.trim().equals("")) {
            if (contraseña.length() >= 6 || contraseña.trim().equals("")) {
                if (contraseña.equals(repetirContraseña)) {
                    //SUBIR IMAGEN
                    if (uri!=null) {
                        //MARCAMOS LA RUTA DE LAS IMAGENES EN EL STORANGE
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
                                        stringFoto = uri2.toString();


                                        //LLAMAMOS AL METODO PARA REGISTRO
                                        completarRegistro();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplication(), "ERROR AL CARGAR LA IMAGEN", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        //EN CASO DE NO SELECCIONAR NINGUNA FOTO SE GARGA ESTA POR DEFECTO
                        stringFoto = "https://firebasestorage.googleapis.com/v0/b/aplicacinftc.appspot.com/o/por%20defecto.jpg?alt=media&token=e88772f9-b7c2-422c-967f-2d0016747b53";
                        completarRegistro();
                    }
                    //TERMINAR DE SUBIR IMAGEN                    //MAS VALIDACIONES
                } else {
                    Toast.makeText(getApplication(), "LAS CONTRASEÑAS NO COINCIDEN", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplication(), "LA CONTRASEÑA NO ES SEGURA", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
        }
    }

    private void completarRegistro() {
        //EL AUTH PARA CREAR USUARIO EN EL AUTHENTIFICATION PASANDOLE EL CORREO Y CONTRASEÑA
        mAuth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult > task) {
                if (task.isSuccessful()) {
                    //GUARDAMOS LA ID DEL USUARIO CREADO
                    String id = mAuth.getCurrentUser().getUid();

                    //CREAMOS OBJETO PARA PASARSELO A LA BASE
                    Usuarios user = new Usuarios(id, nombreUsuario,edad, email, contraseña,stringFoto, rol);

                    //ENTRAMOS DONDE LOS USUARIOS Y LE METEMOS EL OBJETO LUEGO HAY VALIDACIONES
                    mDataBase.child("Usuarios").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        //COMIENZO VALIDACIONES
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                Toast.makeText(getApplication(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplication(), "ERROR EN EL REGISTRO 1", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mAuth.signOut();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplication(), "EMAIL YA EN USO", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), "ERROR EN EL REGISTRO 2", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplication(), "COMPRUEBE SI LOS DATOS SON CORRECTOS", Toast.LENGTH_SHORT).show();

            }

        });
        //FIN VALIDACIONES

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


package com.example.final_proyect.Activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
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

import java.security.SecureRandom;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.final_proyect.Models.Usuario;
import com.example.final_proyect.R;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_Dcotor_Activity extends AppCompatActivity {

    private String email, nombre=" ", apellidos=" ", sexo=" " , edad=" ", password, stringFoto, rol="medico";
    private EditText editEmail, editTextApellidos,editTextEdad, editTextNombre;
    private TextView textPassword;
    private CircleImageView imageProfile;
    private TextView btn_add_foto;
    private Button botonRegistrar, copiar_password;
    private TextView botonAñadirFoto;

    //OBJETO PARA LA BASE DE DATOS
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private FirebaseAuth mAuth2;
    private StorageReference mStorage;
    private String recordar = "false";
    private String id_actual;

    private Uri uri;
    private RadioButton radioButtonF, radioButtonM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_usuario);

        //configuración Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_add_doctor);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.toolbar_add_doctor);

        showAlertForCreatingUserRol();


        mAuth = FirebaseAuth.getInstance();
        mAuth2=FirebaseAuth.getInstance();

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        editEmail = (EditText) findViewById(R.id.add_user_email);
        radioButtonF = (RadioButton) findViewById(R.id.radio_femenino_edit);
        radioButtonM = (RadioButton) findViewById(R.id.radio_masculino_edit);
        editTextNombre = (EditText) findViewById(R.id.nombre_signup);
        editTextApellidos = (EditText) findViewById(R.id.apellidos_signup) ;
        editTextEdad = (EditText) findViewById(R.id.edad_signup);

        password=generateRandomString(10);
        textPassword=findViewById(R.id.generate_password);

        textPassword.setText(password);


        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup_edit);

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



        imageProfile = (CircleImageView) findViewById(R.id.img_perfil_signup);
        botonAñadirFoto = (TextView) findViewById(R.id.tv_img_signup);
        botonAñadirFoto.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                CropImage.activity(uri).setAspectRatio(1, 1)
                        .start(Add_Dcotor_Activity.this);
            }
        });


        botonRegistrar = (Button) findViewById(R.id.add_user_btn_signup);
        botonRegistrar.setOnClickListener(v -> registrarUsuario());


        //Botón copiar al portapapeles
        copiar_password=findViewById(R.id.button_copiar);
        copiar_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Add_Dcotor_Activity.this, "Copiado al portapapeles", Toast.LENGTH_LONG).show();

                ClipboardManager myClipboard = myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;

                String text = textPassword.getText().toString();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

            }
        });

    }

    //** Dialogs **/
    private void showAlertForCreatingUserRol() {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);

        builder.setTitle("Indique el rol del usuario");

        String[] items = {"Médico","Administrador"};

        int checkedItem = 0;
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            switch (which) {
                case 0:
                    rol="medico";
                    Toast.makeText(Add_Dcotor_Activity.this, "Médico", Toast.LENGTH_LONG).show();

                    break;
                case 1:
                    rol="admin";
                    Toast.makeText(Add_Dcotor_Activity.this, "Administrador", Toast.LENGTH_LONG).show();

                    break;

            }
        });

        builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {

            if(rol.equals("admin")){
                startActivity(new Intent(Add_Dcotor_Activity.this, Add_Admin_Activity.class));
                finish();
            }
        });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {
            onBackPressed();

        });



        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void registrarUsuario() {

        //COGEMOS LO DE LOS EDIT Y LOS SPINNER
        email = editEmail.getText().toString();
        nombre = editTextNombre.getText().toString();
        apellidos = editTextApellidos.getText().toString();
        edad = editTextEdad.getText().toString();
        recordar = "false";

        //VALIDACIONES
        if (!email.isEmpty() &&  !password.isEmpty() &&!nombre.isEmpty() &&  !edad.isEmpty()&& !sexo.isEmpty()
                && !email.trim().equals("") ) {
            if (password.length() >= 6 || password.trim().equals("")) {
                //SUBIR IMAGEN
                if (uri!=null) {
                    //MARCAMOS LA RUTA DE LAS IMAGENES EN EL STORANGE
                    final StorageReference filePath = mStorage.child("Fotos de perfil").child(uri.getLastPathSegment());
                    //COGEMOS ESA RUTA Y LE METEMOS LA URI, QUE ES LA FOTO SELECCIONADA, GUARDADA ANTERIORMENTE EN EL ANTERIOR ON CLICK
                    filePath.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                        //GUARDA LA URL DE LA ULTIMA FOTO SUBIDA
                        Task<Uri> downloadUrl = mStorage.child("Fotos de perfil").child(uri.getLastPathSegment()).
                                getDownloadUrl();
                        //SI ES CORRECTO ENTRAMOS AQUI
                        downloadUrl.addOnSuccessListener(uri2 -> {
                            //URI DOS CORRESPONDE A downloadUrl Y SE LO AÑADIMOS A UN STRING QUE LE PASAMOS AL OBJETO
                            stringFoto = uri2.toString();


                            //LLAMAMOS AL METODO PARA REGISTRO
                            completarRegistro();
                        });
                    }).addOnFailureListener(e -> Toast.makeText(getApplication(), "ERROR AL CARGAR LA IMAGEN", Toast.LENGTH_SHORT).show());
                } else {
                    //EN CASO DE NO SELECCIONAR NINGUNA FOTO SE GARGA ESTA POR DEFECTO
                    stringFoto = "https://firebasestorage.googleapis.com/v0/b/aplicacinftc.appspot.com/o/por%20defecto.jpg?alt=media&token=e88772f9-b7c2-422c-967f-2d0016747b53";
                    completarRegistro();
                }
                //TERMINAR DE SUBIR IMAGEN                    //MAS VALIDACIONES

            } else {
                Toast.makeText(getApplication(), "LA CONTRASEÑA NO ES SEGURA", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
        }
    }


    private void completarRegistro() {
        //EL AUTH PARA CREAR USUARIO EN EL AUTHENTIFICATION PASANDOLE EL CORREO Y CONTRASEÑA
        mAuth2.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //GUARDAMOS LA ID DEL USUARIO CREADO
                String id = mAuth2.getCurrentUser().getUid();


                //CREAMOS OBJETO PARA PASARSELO A LA BASE
                Usuario user = new Usuario(id, nombre, apellidos, sexo,edad, email, password,stringFoto, rol);

                //ENTRAMOS DONDE LOS USUARIOS Y LE METEMOS EL OBJETO LUEGO HAY VALIDACIONES
                //COMIENZO VALIDACIONES
                mDataBase.child("Usuarios").child(id).setValue(user).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        Toast.makeText(getApplication(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(getApplication(), "ERROR EN EL REGISTRO 1", Toast.LENGTH_SHORT).show();
                    }
                });
                //mAuth.signOut();
            } else {
                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(getApplication(), "EMAIL YA EN USO", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "ERROR EN EL REGISTRO 2", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(getApplication(), "COMPRUEBE SI LOS DATOS SON CORRECTOS", Toast.LENGTH_SHORT).show());
        //FIN VALIDACIONES
    }

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


    public static String generateRandomString(int length) {
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";

        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
        SecureRandom random = new SecureRandom();

        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 0-62 (exclusivo), retorno aleatorio 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sb.append(rndChar);
        }

        return sb.toString();
    }



}
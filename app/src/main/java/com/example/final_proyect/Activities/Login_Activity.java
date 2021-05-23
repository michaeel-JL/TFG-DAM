package com.example.final_proyect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_proyect.Profiles.Home_Admin_Activity;
import com.example.final_proyect.Profiles.Home_Doctor_Activity;
import com.example.final_proyect.Profiles.Home_User_Activity;
import com.example.final_proyect.R;
import com.example.final_proyect.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Activity extends AppCompatActivity {

    RelativeLayout gallery1, gallery2;
    Button btnlogin, btnsignup, btnreset;
    EditText mMail, mPassword;
    TextView Mail, Password, textoSplashScreen;
    ImageView visible;
    Boolean psw = true;

    private String PREFS_KEY = "mispreferencias";
    private FirebaseAuth mAuth;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private ProgressDialog barraCarga;
    private String rol;
    private DatabaseReference mDataBase;
    //boton flotante
    private FloatingActionButton fab;
    private Switch rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();


        items();

        barraCarga = new ProgressDialog(this);


        rememberMe= (Switch) findViewById(R.id.remember_me_switch);



        //Referenciamos los elementos de la vista
        mDataBase = FirebaseDatabase.getInstance().getReference();
        gallery1 = findViewById(R.id.gallery1);
        gallery2 = findViewById(R.id.gallery2);
        btnlogin = findViewById(R.id.btnlogin);
        btnsignup = findViewById(R.id.btnsignup);
        btnreset = findViewById(R.id.btnreset);
        mMail = findViewById(R.id.mail);
        mPassword = findViewById(R.id.password);
        visible = findViewById(R.id.ic_visible);
        barraCarga = new ProgressDialog(this);
        //Iniciar instancia de autenticación
        mAuth = FirebaseAuth.getInstance();

        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!psw) {
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    psw = true;

                    ///aqui puedes cambiar el texto del boton, o textview, o cambiar la imagen de un imageView.
                }
                else {
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    psw = false;

                    ///aqui puedes cambiar el texto del boton, o textview, o cambiar la imagen de un imageView.
                }
            }
        });







        //BTN - Registrarse
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, SignUp_Activity.class);
                startActivity(intent);
            }
        });

        //BTN - Restablecer password
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, Reset_passwd_Activity.class);
                startActivity(intent);
            }
        });

        //BTN - Iniciar sesion
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicioSesion();
            }
        });




        //se auto-rellenan el email y contraseña en caso de haberse guardado
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExist();

    }






    //Iniciamos sesion
    private void inicioSesion() {



        //Se recogen las credenciales para loguear al usuario
        String email= mMail.getText().toString();
        String password= mPassword.getText().toString();

        //Comprobamos que los datos estén bien
        if(login(email,password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Si el usuario y contraseña son correctos, se carga el PacienteActivity.
                            if (task.isSuccessful()) {
                                barraCarga.dismiss();
                                saveOnPreferences(mMail.getText().toString().trim(), mPassword.getText().toString().trim());
                                //Cogemos los datos del usuario y su ID
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = user.getUid();

                                //Cogemos la referencia
                                DatabaseReference ref;
                                ref = FirebaseDatabase.getInstance().getReference();

                                // Comprobamos el ROL del usuario
                                ref.child("Usuarios").child(uid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            rol= dataSnapshot.child("rol").getValue(String.class);
                                            if(rol.equals("medico")){
                                                Intent intent = new Intent(getApplication(), Home_Doctor_Activity.class);
                                                startActivity(intent);
                                            }
                                            else if(rol.equals("usuario")){
                                                Intent intent = new Intent(getApplication(), Home_User_Activity.class);
                                                startActivity(intent);
                                            }
                                            else if(rol.equals("admin")){
                                                Intent intent = new Intent(getApplication(), Home_Admin_Activity.class);
                                                startActivity(intent);
                                            }
                                        }

//                                        Intent intent = new Intent(getApplication(), PerfilActivity.class);
//                                        startActivity(intent);
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println("Fallo la lectura: " + databaseError.getCode());
                                    }
                                });
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                barraCarga.dismiss();
                                Toast.makeText(Login_Activity.this, "Error, compruebe el usuario o contraseña", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }

    //Comprobamos sus datos
    private boolean login(String email, String password) {
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email no válido, por favor inténtalo de nuevo", Toast.LENGTH_LONG).show();
            return false;
        } else if (!isValidPassword(password)) {
            Toast.makeText(this, "Password incorrecta, 4 carácteres mínimo, por favor intentalo de nuevo", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    //Comprobamos EMAIL
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //Comprobamos PASSWORD
    private boolean isValidPassword(String password) {
        return password.length() >= 4;
    }

    //Datos guardados LOGIN
    private void setCredentialsIfExist() {
        //Cogemos los datos ya guardados
        String email = Util.getUserMailPrefs(prefs);
        String password = Util.getUserPassPrefs(prefs);

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mMail.setText(email);
            mPassword.setText(password);
        }
    }

    //Cogemos los elementos
    private void items() {
        //Referenciamos los elementos de la vista
        mDataBase = FirebaseDatabase.getInstance().getReference();
        gallery1 = findViewById(R.id.gallery1);
        gallery2 = findViewById(R.id.gallery2);
        btnlogin = findViewById(R.id.btnlogin);
        btnsignup = findViewById(R.id.btnsignup);
        btnreset = findViewById(R.id.btnreset);
        mMail = findViewById(R.id.mail);
        mPassword = findViewById(R.id.password);
    }


    //método que guarda el email y contraseña introducidos
    private void saveOnPreferences(String email, String password) {
        if (rememberMe.isChecked()) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", email);
            editor.putString("pass", password);
            editor.apply();
        } else {

            Util.removedSharedPreferences(prefs);
        }
    }



}


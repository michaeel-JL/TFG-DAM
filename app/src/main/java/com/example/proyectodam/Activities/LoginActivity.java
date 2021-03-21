package com.example.proyectodam.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam.Models.Usuarios;
import com.example.proyectodam.R;
import com.example.proyectodam.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout gallery1, gallery2;
    Button btnlogin, btnsignup, btnreset;
    Switch rememberMe;
    EditText mMail, mPassword;
    TextView Mail, Password, textoSplashScreen;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;
    private ProgressDialog barraCarga;
    private String rol;
    private DatabaseReference mDataBase;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login_activity);
            getSupportActionBar().hide();

            //Referenciamos los elementos de la vista
            mDataBase = FirebaseDatabase.getInstance().getReference();
            gallery1 = findViewById(R.id.gallery1);
            gallery2 = findViewById(R.id.gallery2);
            btnlogin = findViewById(R.id.btnlogin);
            btnsignup = findViewById(R.id.btnsignup);
            btnreset = findViewById(R.id.btnreset);
            rememberMe = findViewById(R.id.remember_me_switch);
            mMail = findViewById(R.id.mail);
            mPassword = findViewById(R.id.password);
            barraCarga = new ProgressDialog(this);
            //Iniciar instancia de autenticación
            mAuth = FirebaseAuth.getInstance();

            //se auto-rellenan el email y contraseña en caso de haberse guardado
            prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
            setCredentialsIfExist();

            //btn que nos lleva al activity de registro
            btnsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(com.example.proyectodam.Activities.LoginActivity.this, com.example.proyectodam.Activities.SignUpActivity.class);
                    startActivity(intent);
                }
            });

            //Al activity de reseteo de contraseña
            btnreset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(com.example.proyectodam.Activities.LoginActivity.this, com.example.proyectodam.Activities.RecuperarActivity.class);
                    startActivity(intent);
                }
            });


            //Botón de login
            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });


        }
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
            private boolean isValidEmail(String email) {
                return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
            }
            private boolean isValidPassword(String password) {
                return password.length() >= 4;
            }




            private void login() {
                //Se recogen las credenciales para loguear al usuario
                String email= mMail.getText().toString();
                String password= mPassword.getText().toString();
                if(login(email,password)) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(com.example.proyectodam.Activities.LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) { //Si el usuario y contraseña son correctos, se carga el PacienteActivity.
                                        // Sign in success, update UI with the signed-in user's information
                                        barraCarga.dismiss();
                                        Toast.makeText(com.example.proyectodam.Activities.LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                        saveOnPreferences(mMail.getText().toString().trim(), mPassword.getText().toString().trim());


                                        Intent intent = new Intent(getApplication(), com.example.proyectodam.Activities.PacienteActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        barraCarga.dismiss();
                                        Toast.makeText(com.example.proyectodam.Activities.LoginActivity.this, "Error, compruebe el usuario o contraseña", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }


            //método que fija el email y contraseña que se hayan guardado
            private void setCredentialsIfExist() {
                String email = Util.getUserMailPrefs(prefs);
                String password = Util.getUserPassPrefs(prefs);
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mMail.setText(email);
                    mPassword.setText(password);
                    rememberMe.setChecked(true);
                }
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
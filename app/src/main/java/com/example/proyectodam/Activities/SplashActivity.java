package com.example.proyectodam.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private FirebaseAuth mAuth;
    private String rol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        Intent intentLogin = new Intent(this, LoginActivity.class);
        if (!TextUtils.isEmpty(Util.getUserMailPrefs(prefs)) &&
                !TextUtils.isEmpty(Util.getUserPassPrefs(prefs))) {
            //login();
        } else {
            startActivity(intentLogin);
        }
    }

    private void login() {
        //Se recogen las credenciales para loguear al usuario
        String email= Util.getUserMailPrefs(prefs);
        String password= Util.getUserPassPrefs(prefs);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(com.example.proyectodam.Activities.SplashActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) { //Si el usuario y contraseña son correctos, se carga el PacienteActivity.
                    Intent intent = new Intent(getApplication(), com.example.proyectodam.Activities.PacienteActivity.class);
                    startActivity(intent);

                }
            }
        });
    }
}

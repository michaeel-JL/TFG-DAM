package com.example.final_proyect.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_proyect.Profiles.Home_Admin_Activity;
import com.example.final_proyect.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        Intent intentLogin = new Intent(this, Login_Activity.class);
        if (!TextUtils.isEmpty(Util.getUserMailPrefs(prefs)) &&
                !TextUtils.isEmpty(Util.getUserPassPrefs(prefs))) {
            login();
        } else {
            startActivity(intentLogin);
        }
    }


    private void login() {
        //Se recogen las credenciales para loguear al usuario
        String email= Util.getUserMailPrefs(prefs);
        String password= Util.getUserPassPrefs(prefs);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(SplashActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) { //Si el usuario y contraseña son correctos, se carga el PacienteActivity.
                    Intent intent = new Intent(getApplication(), Home_Admin_Activity.class);
                    startActivity(intent);

                }
            }
        });
    }
}

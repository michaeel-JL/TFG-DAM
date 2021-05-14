package com.example.proyectodam.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam.Activities.Registro.LoginActivity;
import com.example.proyectodam.R;
import com.example.proyectodam.Util.Util;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cargamos la imagen mientras abre la app
        setContentView(R.layout.activity_splash);

        //Cogemos preferencias
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        Intent intentLogin = new Intent(this, LoginActivity.class);


        if (!TextUtils.isEmpty(Util.getUserMailPrefs(prefs)) &&
                !TextUtils.isEmpty(Util.getUserPassPrefs(prefs))) {
            //login();
        } else {
            startActivity(intentLogin);
        }
    }

}

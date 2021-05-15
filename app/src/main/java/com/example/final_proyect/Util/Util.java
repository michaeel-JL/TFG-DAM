package com.example.final_proyect.Util;

import android.content.SharedPreferences;

public class Util {

    //devuelve el email guardado
    public static String getUserMailPrefs(SharedPreferences preferences){
        return preferences.getString("email", "");
    }

    //devuelve la contraseña guardada
    public static String getUserPassPrefs(SharedPreferences preferences){
        return preferences.getString("pass", "");
    }

    //borra los valores guardados
    public static void removedSharedPreferences(SharedPreferences preferences){
        SharedPreferences.Editor editor= preferences.edit();
        editor.remove("email");
        editor.remove("pass");
        editor.apply();
    }
}

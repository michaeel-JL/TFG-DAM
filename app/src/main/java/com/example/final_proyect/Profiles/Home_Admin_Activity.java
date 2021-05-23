package com.example.final_proyect.Profiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.final_proyect.Fragments.Consultas_Fragment;
import com.example.final_proyect.Fragments.Noticias_Fragment;
import com.example.final_proyect.Fragments.Perfil_Fragment;
import com.example.final_proyect.Fragments.Usuarios_Fragment;
import com.example.final_proyect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

public class Home_Admin_Activity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque = new ArrayDeque<>(4);
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        bottomNavigationView = findViewById(R.id.navigation);
        integerDeque.push(R.id.bn_noticias);

        //Cargamos los fargments
        cargarFragments(new Noticias_Fragment());

        bottomNavigationView.setSelectedItemId(R.id.bn_noticias);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        int id = item.getItemId();
                        if(integerDeque.contains(id)){
                            if(id == R.id.bn_noticias){
                                if(integerDeque.size() != 1){
                                    if(flag){
                                        //Cuando flag  es true añadimos noticias aduque list
                                        integerDeque.addFirst(R.id.bn_noticias);
                                        flag = false;
                                    }
                                }
                            }
                            //Eliminamos el id seleccionado
                            integerDeque.remove(id);
                        }
                        //Cabiamos al id seleccionado
                        integerDeque.push(id);
                        cargarFragments(getFragment(item.getItemId()));
                        return true;
                    }
                }
        );
    }

    private Fragment getFragment(int itemId) {
        switch (itemId){
            case R.id.bn_noticias:
                //cargamos el fragment
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new Noticias_Fragment();
            case R.id.bn_consultas:
                //cargamos el fragment
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new Consultas_Fragment();
            case R.id.bn_usuarios:
                //cargamos el fragment
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                return new Usuarios_Fragment();
            case R.id.bn_perfil:
                //cargamos el fragment
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
                return new Perfil_Fragment();
        }
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        return new Noticias_Fragment();
    }

    private void cargarFragments(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.viewPager, fragment, fragment.getClass().getSimpleName())
                .commit();
    }
}
package com.example.final_proyect.Profiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.final_proyect.Adapters.User_List_Adapter;
import com.example.final_proyect.Fragments.Consultas_Fragment;
import com.example.final_proyect.Fragments.Mapa_Fragment;
import com.example.final_proyect.Fragments.Noticias_Fragment;
import com.example.final_proyect.Fragments.Perfil_Fragment;
import com.example.final_proyect.Models.Estado;
import com.example.final_proyect.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayDeque;
import java.util.Deque;

public class Home_User_Activity extends AppCompatActivity {

    Fragment currentFRagment;



    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference ref_estado = database.getReference("Estado").child(user.getUid());

    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque = new ArrayDeque<>(4);
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

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
            case R.id.bn_mapa:
                //cargamos el fragment
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                return new Mapa_Fragment();
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

    @Override
    public void onBackPressed() {
        integerDeque.pop();
        if(!integerDeque.isEmpty()){
            cargarFragments(getFragment(integerDeque.peek()));
        }else {
            finish();
        }
    }

    private void estadoUsuario(String estado) {

        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Estado est = new Estado("", "", estado);
                ref_estado.setValue(est);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



    /*
    @Override
    protected void onResume() {
        super.onResume();
        estadoUsuario("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        estadoUsuario("offline");
    }
     */

}
package com.example.final_proyect.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.final_proyect.Adapters.Alergias_Adapter;
import com.example.final_proyect.Adapters.User_List_Adapter;
import com.example.final_proyect.Models.Alergia;
import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Alergias_Activity extends AppCompatActivity {

    ArrayList<Alergia> alergiaList;
    Alergias_Adapter adapter;
    RecyclerView rv;
    Spinner spn_gravedad;

    //BBDD
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alergias);

        setTitle("Alergias");

        spn_gravedad = findViewById(R.id.add_alergia_spn_gravedad);

        LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(mLayoutManager);

        //Referencia del id
        String uid = user.getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        alergiaList = new ArrayList<>();

        adapter = new Alergias_Adapter(alergiaList,this);
        rv.setAdapter(adapter);

        ref.child("Alergias").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){

                    alergiaList.removeAll(alergiaList);

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Alergia alergia = dataSnapshot.getValue(Alergia.class);
                        alergiaList.add(alergia);
                        setScroll();
                    }
                }
                adapter = new Alergias_Adapter(alergiaList, getBaseContext());
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }
    private void setScroll() {
        rv.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.ic_add_noticia).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ic_add_noticia:
                Intent intent = new Intent(this, Add_Alergia_Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
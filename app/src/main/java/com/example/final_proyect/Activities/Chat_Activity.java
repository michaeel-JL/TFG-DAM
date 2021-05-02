package com.example.final_proyect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.final_proyect.Adapters.Chat_Adapter;
import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Activity extends AppCompatActivity {

    CircleImageView img_user;
    TextView username;
    ImageView ic_conectado, ic_desconectado;
    SharedPreferences mPref;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase databse = FirebaseDatabase.getInstance();
    DatabaseReference ref_estado = databse.getReference("Estado").child(user.getUid());
    DatabaseReference ref_chat = databse.getReference("Chat");


    EditText et_mensaje_txt;
    ImageButton btn_enviar_mensaje;

    //ID CHAT GLOBAL---
    String id_chat_global;
    Boolean amigoonline = false;

    //RV....
    RecyclerView rv_chats;
    Chat_Adapter adapter;
    ArrayList<Chat> chatlist;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_);

        Toolbar toolbar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        img_user = findViewById(R.id.img_usuario);
        username = findViewById(R.id.tv_user);
        ic_conectado = findViewById(R.id.icon_conectado);
        ic_desconectado = findViewById(R.id.icon_desconectado);

        String usuario = getIntent().getExtras().getString("nombre");
        String foto = getIntent().getExtras().getString("img_user");
        String id_user = getIntent().getExtras().getString("id_user");
        //id_chat_global = getIntent().getExtras().getString("id_unico");


        et_mensaje_txt = findViewById(R.id.et_txt_mensaje);
        /*btn_enviar_mensaje = findViewById(R.id.btn_enviar_mensaje);
        btn_enviar_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msj = et_mensaje_txt.getText().toString();
                if(!msj.isEmpty()){
                    final Calendar c = Calendar.getInstance();
                    final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    String idpush = ref_chat.push().getKey();

                    if(amigoonline){
                        Chat chatmsj = new Chat(idpush, user.getUid(), id_user, msj, "si", dateFormat.format(c.getTime()), timeFormat.format(c.getTime()));
                        ref_chat.child(id_chat_global).child(idpush).setValue(chatmsj);
                        Toast.makeText(Chat_Activity.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                        et_mensaje_txt.setText("");
                    }else{
                        Chat chatmsj = new Chat(idpush, user.getUid(), id_user, msj, "no", dateFormat.format(c.getTime()), timeFormat.format(c.getTime()));
                        ref_chat.child(id_chat_global).push().setValue(chatmsj);
                        Toast.makeText(Chat_Activity.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                        et_mensaje_txt.setText("");
                    }

                }else{
                    Toast.makeText(Chat_Activity.this, "El mensaje esta vacio!", Toast.LENGTH_SHORT).show();
                }

            }
        });*/

        //final String id_user_sp = mPref.getString("usuario_sp","");


        username.setText(usuario);
        Glide.with(this). load(foto).into(img_user);

        final DatabaseReference ref = databse.getReference("Estado").child(id_user);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chatcon = snapshot.getValue(String.class);

                if(snapshot.exists()){
                    if(chatcon.equals(user.getUid())){
                        amigoonline = true;
                        ic_conectado.setVisibility(View.VISIBLE);
                        ic_desconectado.setVisibility(View.GONE);
                    }else{
                        amigoonline = false;
                        ic_conectado.setVisibility(View.GONE);
                        ic_desconectado.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //RV....

        rv_chats = findViewById(R.id.rv);
        rv_chats.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rv_chats.setLayoutManager(linearLayoutManager);

        chatlist = new ArrayList<>();
        adapter = new Chat_Adapter(chatlist, this);
        rv_chats.setAdapter(adapter);

        //Leermensaje();

    }//Fin del onCreate

    private void Leermensaje() {
        ref_chat.child(id_chat_global).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    chatlist.removeAll(chatlist);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        chatlist.add(chat);
                        setScroll();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setScroll() {
        rv_chats.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void estadoUsuario(String estado) {
        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //final String id_user_sp = mPref.getString("usuario_sp","");


                //Estado est = new Estado(estado,"","", id_user_sp);
                ref_estado.setValue("");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        estadoUsuario("conectado");
    }

    @Override
    protected void onPause() {
        super.onPause();
        estadoUsuario("desconectado");
        dameUltimaFecha();
    }

    private void dameUltimaFecha() {
        final Calendar c = Calendar.getInstance();
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref_estado.child("fecha").setValue(dateFormat.format(c.getTime()));
                ref_estado.child("hora").setValue(timeFormat.format(c.getTime()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
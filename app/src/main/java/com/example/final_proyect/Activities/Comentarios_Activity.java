package com.example.final_proyect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_proyect.Adapters.Chat_Adapter;
import com.example.final_proyect.Adapters.Comentarios_adapter;
import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.Models.Comentario;
import com.example.final_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xml.sax.DTDHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comentarios_Activity extends AppCompatActivity {

    CircleImageView img_user_comentario;
    TextView user_name_comentario;
    String id_noticia, id_not;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase databse = FirebaseDatabase.getInstance();

    DatabaseReference ref_comentarios = databse.getReference("Comentarios");

    EditText mensaje_enviar;
    ImageButton btn_send;

    //RV....
    RecyclerView rv_comentarios;
    Comentarios_adapter adapter;
    ArrayList<Comentario> comentarioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        img_user_comentario = findViewById(R.id.img_user_comentarios);
        user_name_comentario = findViewById(R.id.usuario_comentarios);
        mensaje_enviar = findViewById(R.id.mensaje_comentarios);
        btn_send = findViewById(R.id.btn_enviar_mensaje);

        //Recuperamos los datos pasados
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id_noticia = bundle.getString("id_noticia");

        DatabaseReference ref_noticias = databse.getReference("Noticias").child(id_noticia).child("id");
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Cogemos el mensaje que se escribe
                String msj = mensaje_enviar.getText().toString();

                if(!msj.isEmpty()) {

                    final Calendar c = java.util.Calendar.getInstance();
                    final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    //Creamos un id para el comentario
                    String idpush = ref_noticias.push().getKey();

                    //Creamos el bojeto comentario
                    Comentario comentario = new Comentario(idpush, user.getUid(), id_noticia, msj, dateFormat.format(c.getTime()), timeFormat.format(c.getTime()));

                    //Guardamos el mensaje en Firebase
                    ref_comentarios.child(id_noticia).child(idpush).setValue(comentario);

                    //Mensaje de confirmación limpiamos el campo mensaje
                    Toast.makeText(Comentarios_Activity.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                    mensaje_enviar.setText("");

                }else {
                    Toast.makeText(Comentarios_Activity.this, "El mensaje está vacio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //RV
        rv_comentarios = findViewById(R.id.rv_comentarios);
        rv_comentarios.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rv_comentarios.setLayoutManager(linearLayoutManager);

        comentarioList = new ArrayList<>();
        adapter = new Comentarios_adapter(comentarioList, this);
        rv_comentarios.setAdapter(adapter);

        //leemos los mensajes de los comentarios a traves del id de la noticia
        ref_comentarios.child(id_noticia).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 comentarioList.clear();

                 for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                     Comentario comentario = dataSnapshot.getValue(Comentario.class);
                     comentarioList.add(comentario);
                     setScroll();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    private void setScroll() {
        rv_comentarios.scrollToPosition(adapter.getItemCount() - 1);
    }
}
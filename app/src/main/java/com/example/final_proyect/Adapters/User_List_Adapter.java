package com.example.final_proyect.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_proyect.Activities.Chat_Activity;
import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.Models.Medico;
import com.example.final_proyect.Models.Usuario;
import com.example.final_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class User_List_Adapter extends RecyclerView.Adapter<User_List_Adapter.viewHolderAdapter>{

    List<Usuario> userList;
    Context context;
    List<Usuario> usuariosOriginal;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase databse = FirebaseDatabase.getInstance();


    private OnItemClickListener itemClickListener;

    public User_List_Adapter(List<Usuario> userList, Context context) {
        this.userList = userList;
        this.context = context;
        this.usuariosOriginal = new ArrayList<>();
        usuariosOriginal.addAll(userList);
    }

    @NonNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user_consulta, parent, false);
        viewHolderAdapter holder = new viewHolderAdapter(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderAdapter holder, int position) {
        Usuario userss = userList.get(position);

        Glide.with(context).load(userss.getFoto()).into(holder.img_user);
        holder.tv_usuario.setText(userss.getNombre());

        if(userss.getRol().equals("medico")){

            String id = userss.getId();
            DatabaseReference ref_medico = databse.getReference("Usuarios").child(id);
            ref_medico.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    holder.tv_especialidad.setText(snapshot.getValue(Medico.class).getEspecialidad());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        //Si el usuario que está recorriendo es igual al que tenemos lo oculta
        if (userss.getId().equals(user.getUid())){
            holder.cardView.setVisibility(View.GONE);
        }else{
            holder.cardView.setVisibility(View.VISIBLE);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref_chats = databse.getReference("Chats").child(user.getUid()).child(userss.getId()).child("id_chat");
                ref_chats.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //Si existe un IDCHAT
                        if(snapshot.exists()){

                            //Cogemos el ID_CHAT de firebase
                            String id_chat = snapshot.getValue(String.class);

                            Intent intent = new Intent(view.getContext(), Chat_Activity.class);
                            intent.putExtra("nombre", userss.getNombre());
                            intent.putExtra("img_user", userss.getFoto());
                            intent.putExtra("id_user2", userss.getId());
                            intent.putExtra("id_chat", id_chat);
                            view.getContext().startActivity(intent);

                        }else{

                            //Creamos un id_unico para el chat y lo guardamos en Firebase
                            String id_chat = ref_chats.push().getKey();

                            //Guardamos el ID_CHAT en ambos usuarios
                            DatabaseReference A = databse.getReference("Chats").child(user.getUid()).child(userss.getId()).child("id_chat");
                            DatabaseReference B = databse.getReference("Chats").child(userss.getId()).child(user.getUid()).child("id_chat");
                            //SAVE
                            A.setValue(id_chat);
                            B.setValue(id_chat);

                            DatabaseReference ref_mensajes = databse.getReference("Mensajes");
                            ref_mensajes.child(id_chat);

                            Intent intent = new Intent(view.getContext(), Chat_Activity.class);
                            intent.putExtra("nombre", userss.getNombre());
                            intent.putExtra("img_user", userss.getFoto());
                            intent.putExtra("id_user2", userss.getId());
                            intent.putExtra("id_chat", id_chat);
                            view.getContext().startActivity(intent);
                        }//Fin del if
                    }//Fin de OnDataChange

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {
        TextView tv_usuario, tv_especialidad;
        ImageView img_user;
        CardView cardView;

        public viewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            tv_usuario = itemView.findViewById(R.id.tv_user);
            img_user = itemView.findViewById(R.id.img_user);
            cardView = itemView.findViewById(R.id.cardiew);
            tv_especialidad= itemView.findViewById(R.id.chat_especialidad);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Chat chat, int position);
    }

    //Buscador de usuarios
    public void filter (final String strSearrch){

        //Si la longitud es menor a 0
        if (strSearrch.length() == 0){
            userList.clear();
            userList.addAll(usuariosOriginal);
        }
        //Sino realiza la busqueda
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                List<Usuario> collect = usuariosOriginal.stream()
                        .filter(i -> i.getNombre().toLowerCase().contains(strSearrch))
                        .collect(Collectors.toList());
                userList.clear();
                userList.addAll(collect);
            }else {
                userList.clear();
                for (Usuario i : usuariosOriginal){
                    if (i.getNombre().toLowerCase().contains(strSearrch)){
                        userList.add(i);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}

package com.example.final_proyect.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_proyect.Activities.Comentarios_Activity;
import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.Models.Comentario;
import com.example.final_proyect.Models.Usuario;
import com.example.final_proyect.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comentarios_adapter extends RecyclerView.Adapter<Comentarios_adapter.viewHolderAdapter> {

    List<Comentario> comentariosList;
    Context context;

    //firebase
    FirebaseDatabase databse = FirebaseDatabase.getInstance();



    public Comentarios_adapter(List<Comentario> comentariosList, Context context) {
        this.comentariosList = comentariosList;
        this.context = context;
    }



    @NonNull
    @Override
    public Comentarios_adapter.viewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.comentario_item_left, parent, false);
            return new Comentarios_adapter.viewHolderAdapter(view);
        }

    @Override
    public void onBindViewHolder(@NonNull Comentarios_adapter.viewHolderAdapter holder, int position) {

        Comentario comentario = comentariosList.get(position);
        holder.mensaje_coment.setText(comentario.getMensaje());
        holder.fecha_coment.setText(comentario.getFecha() + " " + comentario.getHora());

        DatabaseReference ref_usuarios = databse.getReference("Usuarios").child(comentario.getEnvia());
        ref_usuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String nombre_envia = snapshot.getValue(Usuario.class).getNombre();
                String foto_user = snapshot.getValue(Usuario.class).getFoto();
                String apellidos_user = snapshot.getValue(Usuario.class).getApellidos();

                holder.nombre_coment.setText(nombre_envia);
                holder.apellidos_coment.setText(apellidos_user);
                Picasso.get().load(foto_user).fit().into(holder.img_coment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return comentariosList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {

        TextView nombre_coment, apellidos_coment, mensaje_coment, fecha_coment;
        CircleImageView img_coment;

        public viewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            nombre_coment = itemView.findViewById(R.id.usuario_comentarios);
            apellidos_coment = itemView.findViewById(R.id.apellidos_comentarios);
            mensaje_coment = itemView.findViewById(R.id.mensaje_comentarios);
            fecha_coment = itemView.findViewById(R.id.fecha_comentarios);
            img_coment = itemView.findViewById(R.id.img_user_comentarios);

        }
    }

}

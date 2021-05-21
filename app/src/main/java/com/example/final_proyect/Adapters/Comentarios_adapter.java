package com.example.final_proyect.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.Models.Comentario;
import com.example.final_proyect.R;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.List;

public class Comentarios_adapter extends RecyclerView.Adapter<Comentarios_adapter.viewHolderAdapter> {

    List<Comentario> comentariosList;
    Context context;
    Boolean soloright = false;
    FirebaseUser fuser;


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


    }

    @Override
    public int getItemCount() {
        return comentariosList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {

        TextView nombre_coment, mensaje_coment, fecha_coment;

        public viewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            nombre_coment = itemView.findViewById(R.id.usuario_comentarios);
            mensaje_coment = itemView.findViewById(R.id.mensaje_comentarios);
            fecha_coment = itemView.findViewById(R.id.fecha_comentarios);

        }
    }

}

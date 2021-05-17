package com.example.final_proyect.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.final_proyect.Models.Usuario;
import com.example.final_proyect.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.ViewHolder>{

    private Context context;
    private List<Usuario> users;
    private int layout;
    private OnItemClickListener itemClickListener;

    public User_Adapter(List<Usuario> users, int layout, OnItemClickListener itemListener) {
        this.users = users;
        this.layout = layout;
        this.itemClickListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(users.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombre;
        public TextView edad;
        public TextView email;

        //private TextView password;
        public ImageView imagePerfil;
        private TextView rol;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.textViewNombreUsuario);
            edad = (TextView) itemView.findViewById(R.id.textViewEdad);
            email = (TextView) itemView.findViewById(R.id.textViewEmail);
            //password = (TextView) itemView.findViewById(R.id.textViewStars);
            rol = (TextView) itemView.findViewById(R.id.textViewRol);
            imagePerfil = (ImageView) itemView.findViewById(R.id.imageViewUser);
        }

        public void bind(final Usuario users, final OnItemClickListener itemListener) {
            nombre.setText(users.getNombre());
            email.setText("Email: "+users.getEmail());
            edad.setText(users.getEdad());
            rol.setText("Rol: "+users.getRol());
            Picasso.get().load(users.getFoto()).fit().into(imagePerfil);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(users, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Usuario users, int position);
    }
}
package com.example.proyectodam.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodam.Models.Usuarios;
import com.example.proyectodam.R;

import com.squareup.picasso.Picasso;
import java.util.List;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context context;
    private List<Usuarios> users;
    private int layout;
    private OnItemClickListener itemClickListener;
    private OnButtonClickListener btnClickListener;

    public UserAdapter(List<Usuarios> users, int layout, OnItemClickListener itemListener, OnButtonClickListener btnListener) {
        this.users = users;
        this.layout = layout;
        this.itemClickListener = itemListener;
        this.btnClickListener = btnListener;
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
        holder.bind(users.get(position), itemClickListener, btnClickListener);
    }


    @Override
    public int getItemCount() {
        return users.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombreUsuario;
        public TextView edad;
        public TextView email;

        //private TextView password;
        public ImageView imagePerfil;
        private TextView rol;
        public Button btnDelete;


        public ViewHolder(View itemView) {
            super(itemView);
            nombreUsuario = (TextView) itemView.findViewById(R.id.textViewNombreUsuario);
            edad = (TextView) itemView.findViewById(R.id.textViewEdad);
            email = (TextView) itemView.findViewById(R.id.textViewEmail);
            //password = (TextView) itemView.findViewById(R.id.textViewStars);
            rol = (TextView) itemView.findViewById(R.id.textViewRol);
            imagePerfil = (ImageView) itemView.findViewById(R.id.imageViewUser);
            btnDelete = (Button) itemView.findViewById(R.id.buttonDeleteUser);
        }

        public void bind(final Usuarios users, final OnItemClickListener itemListener, final OnButtonClickListener btnListener) {
            nombreUsuario.setText(users.getNombreUsuario());
            email.setText("Email: "+users.getEmail());
            edad.setText("Edad: "+users.getEdad());
            rol.setText("Rol: "+users.getRol());
            Picasso.get().load(users.getFoto()).fit().into(imagePerfil);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnListener.onButtonClick(users, getAdapterPosition());
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(users, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Usuarios users, int position);
    }

    public interface OnButtonClickListener {
        void onButtonClick(Usuarios users, int position);
    }
}

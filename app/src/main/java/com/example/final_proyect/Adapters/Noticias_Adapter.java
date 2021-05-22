package com.example.final_proyect.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.final_proyect.Models.Noticia;
import com.example.final_proyect.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Noticias_Adapter extends  RecyclerView.Adapter<Noticias_Adapter.ViewHolder>{

    private Context context;
    private List<Noticia> noticias;
    private int layout;
    private OnItemClickListener itemClickListener;
    private OnButtonClickListener btnClickListener;

    public Noticias_Adapter(List<Noticia> noticias, int layout) {
        this.noticias = noticias;
        this.layout = layout;
    }


    public Noticias_Adapter(List<Noticia> noticias, int layout, OnItemClickListener itemListener, OnButtonClickListener btnListener) {
        this.noticias = noticias;
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
        holder.bind(noticias.get(position), itemClickListener, btnClickListener);
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titulo, description, etiqueta,fecha,  num_likes, num_comentarios;
        public ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            titulo =  itemView.findViewById(R.id.titulo_noticia_cv);
            description = itemView.findViewById(R.id.descripcion_noticia_cv);
            image =  itemView.findViewById(R.id.imagen_noticia_cv);
            etiqueta = itemView.findViewById(R.id.txt_etiqueta);
            fecha = itemView.findViewById(R.id.fecha_noticia_cv);
            num_likes = itemView.findViewById(R.id.num_likes_noticia);
            num_comentarios = itemView.findViewById(R.id.num_comentarios_noticia);

        }

        public void bind(final Noticia noticias, final OnItemClickListener itemListener, final OnButtonClickListener btnListener) {
            titulo.setText(noticias.getTitulo());
            description.setText(noticias.getDescription());
            Picasso.get().load(noticias.getImagePrincipal()).fit().into(image);
            etiqueta.setText(noticias.getEtiqueta());
            String likes = String.valueOf(noticias.getMegustas());
            fecha.setText(noticias.getFecha());
            String comentarios = String.valueOf(noticias.getComentarios());
            num_likes.setText(likes);
            num_comentarios.setText(comentarios);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(noticias, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Noticia noticias, int position);
    }

    public interface OnButtonClickListener {
        void onButtonClick(Noticia noticias, int position);
    }

}
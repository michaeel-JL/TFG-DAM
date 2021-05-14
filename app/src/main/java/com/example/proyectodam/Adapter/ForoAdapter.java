package com.example.proyectodam.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodam.Models.Chats;
import com.example.proyectodam.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ForoAdapter extends  RecyclerView.Adapter<ForoAdapter.ViewHolder> {

    private Context context;
    private List<Chats> chats;
    private int layout;
    private OnItemClickListener itemClickListener;
    private OnButtonClickListener btnClickListener;


    public ForoAdapter(List<Chats> chats, int layout, OnItemClickListener itemListener, OnButtonClickListener btnListener) {
        this.chats = chats;
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
        holder.bind(chats.get(position), itemClickListener, btnClickListener);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titulo;
        public TextView description;
        public ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.textViewTitle);
            description=(TextView) itemView.findViewById(R.id.textViewDescription);
            image = (ImageView) itemView.findViewById(R.id.imageChat);

        }

        public void bind(final Chats chats, final OnItemClickListener itemListener, final OnButtonClickListener btnListener) {
            titulo.setText(chats.getTitulo());
            description.setText(chats.getDescription());
            Picasso.get().load(chats.getImagePrincipal()).fit().into(image);





            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(chats, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Chats chat, int position);
    }

    public interface OnButtonClickListener {
        void onButtonClick(Chats chat, int position);
    }
}
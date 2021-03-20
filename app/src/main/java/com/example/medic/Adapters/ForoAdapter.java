package com.example.medic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medic.Models.Chats;
import com.example.medic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ForoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Chats> chats;
    private int layout;
    private OnItemClickListener itemClickListener;
    private OnButtonClickListener btnClickListener;


    public ForoAdapter() {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public ForoAdapter(List<Chats> chats, int layout, OnItemClickListener itemListener, OnButtonClickListener btnListener) {
        this.chats = chats;
        this.layout = layout;
        this.itemClickListener = itemListener;
        this.btnClickListener = btnListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        RecyclerView.ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView image;



        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textViewTitle);
            image = (ImageView) itemView.findViewById(R.id.imageChat);
        }

        public void bind(final Chats chats, final OnItemClickListener itemListener, final OnButtonClickListener btnListener) {
            name.setText(chats.getTitulo());
            Picasso.get().load(chats.getImage()).fit().into(image);




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

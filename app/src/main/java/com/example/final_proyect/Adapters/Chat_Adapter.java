package com.example.final_proyect.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.Models.Usuario;
import com.example.final_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.viewHolderAdapter>{

    List<Chat> chatList;
    Context context;
    public static final int MENSAJE_RIGHT = 1;
    public static final int MENSAJE_LEFT = 0;
    Boolean soloright = false;
    FirebaseUser fuser;

    public Chat_Adapter(List<Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public Chat_Adapter.viewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MENSAJE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new Chat_Adapter.viewHolderAdapter(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new Chat_Adapter.viewHolderAdapter(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Chat_Adapter.viewHolderAdapter holder, int position) {

        Chat chat = chatList.get(position);

        holder.tv_mensaje.setText(chat.getMensaje());

        if (soloright){
            final Calendar c = Calendar.getInstance();
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            if (chat.getFecha().equals(dateFormat.format(c.getTime()))){
                holder.tv_fecha.setText("hoy" + chat.getHora());
            }else{
                holder.tv_fecha.setText(chat.getFecha() + " " + chat.getHora());
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {

        TextView tv_mensaje, tv_fecha;
        ImageView img_entregado, img_visto;

        public viewHolderAdapter(@NonNull View itemView) {
            super(itemView);
            tv_mensaje = itemView.findViewById(R.id.tv_mensaje);
            tv_fecha = itemView.findViewById(R.id.tv_fecha);
            img_entregado = itemView.findViewById(R.id.img_entregado);
            img_visto= itemView.findViewById(R.id.img_visto);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if(chatList.get(position).getEnvia().equals(fuser.getUid())){
            soloright=true;
            return MENSAJE_RIGHT;
        }else {
            soloright = false;
            return MENSAJE_LEFT;
        }
    }
}

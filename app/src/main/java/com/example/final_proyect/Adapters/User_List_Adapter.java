package com.example.final_proyect.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_proyect.Activities.Chat_Activity;
import com.example.final_proyect.Models.Chat;
import com.example.final_proyect.Models.Usuario;
import com.example.final_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class User_List_Adapter extends RecyclerView.Adapter<User_List_Adapter.viewHolderAdapter>{

    List<Usuario> userList;
    Context context;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database =  FirebaseDatabase.getInstance();

    private OnItemClickListener itemClickListener;

    SharedPreferences mPref;

    public User_List_Adapter(List<Usuario> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    public User_List_Adapter(List<Usuario> userList, Context context, OnItemClickListener itemClickListener) {
        this.userList = userList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_consulta_cardview, parent, false);
        viewHolderAdapter holder = new viewHolderAdapter(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderAdapter holder, int position) {
        Usuario userss = userList.get(position);

        final Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        Glide.with(context).load(userss.getFoto()).into(holder.img_user);
        holder.tv_usuario.setText(userss.getNombreUsuario());

        //Si el usuario que está recorriendo es igual al que tenemos lo oculta
        if (userss.getId().equals(user.getUid())){
            holder.cardView.setVisibility(View.GONE);
        }else{
            holder.cardView.setVisibility(View.VISIBLE);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "He dado click al cradview", Toast.LENGTH_SHORT).show();

                //mPref = view.getContext().getSharedPreferences("usuario_sp",Context.MODE_PRIVATE);
                //final SharedPreferences.Editor editor = mPref.edit();

                    Intent intent = new Intent(view.getContext(), Chat_Activity.class);
                    intent.putExtra("nombre", userss.getNombreUsuario());
                    intent.putExtra("img_user", userss.getFoto());
                    intent.putExtra("id_user", userss.getId());
                    //editor.putString("usuario_sp",userss.getId());
                    //editor.apply();

                    view.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {
        TextView tv_usuario;
        ImageView img_user;
        CardView cardView;
        ProgressBar progressBar;



        public viewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            tv_usuario = itemView.findViewById(R.id.tv_user);
            img_user = itemView.findViewById(R.id.img_user);
            cardView = itemView.findViewById(R.id.cardiew);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Chat chat, int position);
    }
}

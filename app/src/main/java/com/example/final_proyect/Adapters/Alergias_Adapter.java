package com.example.final_proyect.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_proyect.Activities.Add_Alergia_Activity;
import com.example.final_proyect.Activities.Alergias_Activity;
import com.example.final_proyect.Models.Alergia;
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

public class Alergias_Adapter extends RecyclerView.Adapter<Alergias_Adapter.viewHolderAdapter>{

    List<Alergia> alergiaList;
    Context context;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase databse = FirebaseDatabase.getInstance();
    DatabaseReference ref;


    public Alergias_Adapter(List<Alergia> alergiaList, Context context) {
        this.alergiaList = alergiaList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_alergias, parent, false);
        viewHolderAdapter holder = new viewHolderAdapter(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderAdapter holder, int position) {

        Alergia alergiass = alergiaList.get(position);

        holder.txt_alergia.setText(alergiass.getNombre());
        holder.txt_gravedad.setText(alergiass.getGravedad());

        //Referencia del id
        String uid = user.getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("Usuarios").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String rol = snapshot.getValue(Usuario.class).getRol();

                if(rol.equals("medico")){
                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            enviarDatosAddAlergia(view, alergiass, "vista_medico");
                        }
                    });
                }else if (rol.equals("usuario")){
                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            enviarDatosAddAlergia(view, alergiass, "si");
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void enviarDatosAddAlergia(View view, Alergia alergiass, String modo) {

        Intent intent = new Intent(view.getContext(), Add_Alergia_Activity.class);
        intent.putExtra("n_alergia", alergiass.getNombre());
        intent.putExtra("descripcion_alergia", alergiass.getDescripcion());
        intent.putExtra("gravedad_alergia", alergiass.getGravedad());
        intent.putExtra("id_alergia", alergiass.getId());
        intent.putExtra("editar", modo);
        view.getContext().startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return alergiaList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {
        TextView txt_alergia, txt_gravedad;
        CardView cardView;

        public viewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            //Se coge el id del layout
            txt_alergia = itemView.findViewById(R.id.nombre_alergia_cv);
            txt_gravedad = itemView.findViewById(R.id.txt_gravedad_cv);
            cardView = itemView.findViewById(R.id.cardview_alergia);

        }
    }
}

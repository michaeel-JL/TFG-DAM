package com.example.final_proyect.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_proyect.Activities.Add_Alergia_Activity;
import com.example.final_proyect.Activities.Add_Enfermedad_Activity;
import com.example.final_proyect.Models.Enfermedad;
import com.example.final_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Enfermedades_Adapter extends RecyclerView.Adapter<Enfermedades_Adapter.viewHolderAdapter>{

        List<Enfermedad> enfermedadList;
        Context context;

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase databse=FirebaseDatabase.getInstance();

public Enfermedades_Adapter(List<Enfermedad> enfermedadList,Context context){
        this.enfermedadList = enfermedadList;
        this.context=context;
        }


@NonNull
@Override
public viewHolderAdapter onCreateViewHolder(@NonNull  ViewGroup parent,int viewType){
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_enfermedad,parent,false);
        viewHolderAdapter holder =  new viewHolderAdapter(v);

        return holder;
        }

@Override
public void onBindViewHolder(@NonNull viewHolderAdapter holder,int position){

        Enfermedad enfermedadd = enfermedadList.get(position);
        holder.txt_enfermedad.setText(enfermedadd.getNombre());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref_alergias = databse.getReference("Enfermedades").child(user.getUid()).child(enfermedadd.getId());
                ref_alergias.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {

                            Intent intent = new Intent(view.getContext(), Add_Enfermedad_Activity.class);
                            intent.putExtra("nombre_enfermedad", enfermedadd.getNombre());
                            intent.putExtra("detalles_enfermedad", enfermedadd.getDetalles());
                            intent.putExtra("fecha_d_enfermedad", enfermedadd.getFecha_diagnostico());
                            intent.putExtra("fecha_r_enfermedad", enfermedadd.getFecha_resolucion());
                            intent.putExtra("id_enfermedad", enfermedadd.getId());
                            intent.putExtra("editar", "si");
                            view.getContext().startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        }

@Override
public int getItemCount(){
        return enfermedadList.size();
        }

public class viewHolderAdapter extends RecyclerView.ViewHolder {
    TextView txt_enfermedad;
    CardView cardView;


    public viewHolderAdapter(@NonNull View itemView) {
        super(itemView);

        //Se coge el id del layout
        txt_enfermedad = itemView.findViewById(R.id.nombre_enfermedad_cv);
        cardView = itemView.findViewById(R.id.cardview_enfermedad);

    }
}
}
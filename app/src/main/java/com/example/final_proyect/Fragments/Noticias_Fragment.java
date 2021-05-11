package com.example.final_proyect.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_proyect.Activities.Noticia_Activity;
import com.example.final_proyect.Adapters.Noticias_Adapter;
import com.example.final_proyect.Models.Noticia;
import com.example.final_proyect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Noticias_Fragment extends Fragment {

    private FloatingActionButton fab; //Boton flotante
    private List<Noticia> noticias;
    private Noticias_Adapter adapter; //Adaptar
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mDataBase; //BBDD
    private FirebaseAuth mAuth; //Firebase
    private SharedPreferences prefs;
    private Context context;


    public Noticias_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_noticias, container, false);


        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();


        //Metodo para cargar las tarjeta de las ciudades
        getNoticiasFromFirebase();

        //Array de Noticias
        noticias = new ArrayList<Noticia>();

        noticias.add(new Noticia("1", "¡Avances en la vacuna del Covid-19!", "Chat abierto para hablar temas relacionados con el Covid-19 " +
                "donde todos los usuarios podrán comentar y hablar sobre el tema. Queda totalmente prohibido el uso del chat para un uso fraudulento", "https://www.redaccionmedica.com/images/directos/covid-19-ultimas-noticias-vacuna-de-janssen-y-crece-la-incidencia_620x368.jpg"
                , "https://www.goldenhotels.com/cobi/media/gh78/90/4f/covid19.png", "La OMS está colaborando estrechamente con expertos mundiales, gobiernos y asociados para ampliar rápidamente los conocimientos científicos sobre este nuevo virus, rastrear su propagación y virulencia y asesorar a los países y " +
                "las personas sobre la medidas para proteger la salud y prevenir la propagación del brote."));
        noticias.add(new Noticia("2", "Higiene", "Chat abierto para hablar temas relacionados con la Higiene " +
                "donde todos los usuarios podrán comentar y hablar sobre el tema. Queda totalmente prohibido el uso del chat para un uso fraudulento", "https://concepto.de/wp-content/uploads/2013/05/higiene-manos-limpias-e1552168615767.jpg", "https://alcazardesanjuan.com/wp-content/uploads/2020/04/higiene-ante-el-covid-19.jpg",
                "La higiene es el conjunto de conocimientos y técnicas que aplican los individuos para el control de los factores que ejercen o pueden tener efectos nocivos sobre la salud. La higiene personal es el concepto básico del aseo, de la limpieza y del cuidado del cuerpo humano. Son una serie de hábitos relacionados con el cuidado personal que inciden positivamente en la salud y que previene posibles enfermedades e infecciones; asimismo, es la parte de la medicina o ciencia que trata de los medios de prolongar la vida, y conservar la salud de las personas."));
        noticias.add(new Noticia("3", "Vida saludable, ¿es solo ir al gimnasio?", "Vida saludable, con una actitud positiva, un cuerpo fuerte y cargado de energía. ¡Empieza desde hoy a cambiar tus hábitos! No solo perderás peso. Ganarás salud.",
                "https://www.vivesanamente.com/wp-content/uploads/2019/01/Ha%CC%81bitos-alimentarios-2.png", "https://www.minutoe.com/u/fotografias/m/2020/9/11/f500x333-49750_73553_0.jpg", "Vivimos en una sociedad cada vez más sedentaria que además busca resultados rápidos y sin esfuerzo. Personas que pueden estar 3 o 4 horas tumbadas en el sofá pero no pueden hacer 45 min de ejercicio. Y eso tenemos que cambiarlo desde ¡ya!\n" +
                "\n" +
                "Cuando hablamos de vida saludable pensamos en alguien que hace deporte, se alimenta correctamente, tiene una buena salud y se ve bien físicamente. Algo que cualquiera podría conseguir aplicando un poco de disciplina.\n" +
                "\n" +
                "La mejor estrategia ganar salud y ponerse en forma es tener una mentalidad a largo plazo. Los resultados más eficaces y duraderos son los que se consiguen poco a poco. Por eso hay que introducir paulatinamente hábitos que puedan mantenerse siempre."));

        noticias.add(new Noticia("4", "Dientes limpios", "¿Crees que sabes todo respecto de las técnicas para cepillarte los dientes y usar hilo dental? Aprende los conceptos básicos y qué puedes hacer para promover la salud bucal.",
                "https://gacetadental.com/wp-content/uploads/2020/09/Salud-oral-1280x720.jpg", "https://gacetadental.com/wp-content/uploads/2021/01/Salud-oral.jpg", "Ten en cuenta estos conceptos básicos de cepillado:\n" +
                "\n" +
                "Cepíllate los dientes dos veces al día. Cuando te cepilles, no te apresures. Tómate unos dos minutos para hacer un trabajo minucioso. No te cepilles inmediatamente después de comer, especialmente si ingeriste algo ácido como toronja o refresco. No olvides limpiarte la lengua, que alberga bacterias, con un cepillo de dientes o un raspador de lengua.\n" +
                "Usa el equipo adecuado. Usa una pasta dental con flúor y un cepillo de dientes de cerdas suaves que se adapte cómodamente a la boca. Considera el uso de un cepillo de dientes eléctrico o a pilas, que puede reducir la placa y una forma leve de enfermedad de las encías (gingivitis) más que el cepillado manual. Estos dispositivos también son útiles si tienes artritis u otros problemas que dificultan el cepillado eficaz.\n" +
                "Practica una buena técnica. Sostén el cepillo de dientes en un ángulo leve: apuntando las cerdas hacia el área donde los dientes se encuentran con las encías. Cepilla suavemente con movimientos circulares cortos hacia adelante y hacia atrás. Cepillarse demasiado fuerte o con cerdas duras puede dañar las encías.\n" +
                "\n" +
                "Cepíllate los dientes durante dos minutos. Recuerda cepillarte el exterior, el interior y las superficies de masticación de los dientes, así como la lengua.\n" +
                "\n" +
                "Mantén tu equipo limpio. Siempre enjuaga el cepillo de dientes con agua después de cepillarte. Guarda el cepillo de dientes en posición vertical y déjalo secar al aire hasta que lo vuelvas a usar."));
        mDataBase.getDatabase().getReference().child("Noticias").setValue(noticias);

        layoutManager = new LinearLayoutManager(context);


        //Redimensionamos el recycler
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_noticias);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    //Lee los noticias de Firebase
    private void getNoticiasFromFirebase() {

        mDataBase.child("Noticias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Noticias_Adapter.OnItemClickListener itemListener = null;
                Noticias_Adapter.OnButtonClickListener btnListener = null;

                //Si hay datos...
                if (dataSnapshot.exists()) {
                    //Actuliza la lista
                    noticias.clear();
                    //Busca todos los datos
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String titulo=ds.child("titulo").getValue().toString();
                        String description=ds.child("description").getValue().toString();
                        String imagePrincipal = ds.child("imagePrincipal").getValue().toString();
                        String imageSecundaria = ds.child("imagenSecundaria").getValue().toString();
                        String decNoticia = ds.child("textoNoticia").getValue().toString();

                        String id = ds.getKey();
                        //Creamos un chat
                        Noticia noticia = new Noticia(id, titulo, description, imagePrincipal, imageSecundaria, decNoticia);
                        //Añadimos la ciudad al List
                        noticias.add(noticia);
                    }
                }

                //Adaptador

                adapter = new Noticias_Adapter(noticias, R.layout.noticia_cardview, new Noticias_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Noticia noticia, int position) {


                        Intent intent = new Intent(getActivity(), Noticia_Activity.class);
                        intent.putExtra("id", noticias.get(position).getId());
                        intent.putExtra("name", noticias.get(position).getTitulo());
                        intent.putExtra("description", noticias.get(position).getDescription());
                        intent.putExtra("imagePrincipal", noticias.get(position).getImagePrincipal());
                        intent.putExtra("imagenSecundaria", noticias.get(position).getImagenSecundaria());
                        intent.putExtra("textoNoticia", noticias.get(position).getTextoNoticia());
                        startActivity(intent);
                    }
                    //Boton edit
                }, new Noticias_Adapter.OnButtonClickListener() {
                    @Override
                    public void onButtonClick(Noticia noticia, int position) {
/*
                        Intent intent = new Intent(getActivity(), Noticia_edit_Activity.class);
                        intent.putExtra("id", chats.get(position).getId());
                        intent.putExtra("name", chats.get(position).getTitulo());
                        intent.putExtra("description", chats.get(position).getDescription());
                        intent.putExtra("imagePrincipal", chats.get(position).getImagePrincipal());
                        intent.putExtra("imagenSecundaria", chats.get(position).getImagenSecundaria());
                        intent.putExtra("textoNoticia", chats.get(position).getTextoNoticia());
                        startActivity(intent);
*/
                    }
                                    });

                //Cargamos el recycler metiendo el adaptador
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
package com.example.proyectodam.Activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam.Activities.Perfiles.DoctorActivity;
import com.example.proyectodam.Activities.Secciones.ChatPacientes;
import com.example.proyectodam.Activities.Secciones.ForoDoctorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.proyectodam.R;
import com.example.proyectodam.Adapter.BoardAdapter;
import com.example.proyectodam.Models.Board;
import com.example.proyectodam.Models.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BoardActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private FloatingActionButton fab;
    private ListView listView;
    private BoardAdapter adapter;
    private DatabaseReference mDataBase;

    private List<Board> boards;
    private List<Note> notes;
    private String id;
    private String title;
    private Date createdAt = new Date();
    private int numNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        // Db Firebase
        boards= new ArrayList<Board>();
        notes = new ArrayList<Note>();

        mDataBase = FirebaseDatabase.getInstance().getReference();
        listView = (ListView) findViewById(R.id.listViewBoard);

        readBoard();
        listView.setOnItemClickListener(BoardActivity.this);
        registerForContextMenu(listView);
        fab = (FloatingActionButton) findViewById(R.id.fabAddBoard);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForCreatingBoard("Añadir nuevo tablero", "Escribir el nombre del tablero");
            }
        });
    }

    //** CRUD Actions **/
    private void readBoard() {
        mDataBase.child("Boards").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boards.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        title = ds.child("title").getValue().toString();
                        numNotes = Integer.parseInt( ds.child("numNotes").getValue().toString());
                        String date = ds.child("createdAt").child("date").getValue().toString();
                        String month = ds.child("createdAt").child("month").getValue().toString();
                        int monthInt = Integer.parseInt(month)+1;
                        String year = ds.child("createdAt").child("year").getValue().toString();
                        int yearInt = (Integer.parseInt(year)+1900);
                        String fecha=null;
                        if(Integer.parseInt(month)<10){
                            fecha=date+"/0"+monthInt+"/"+yearInt;
                        }else{
                            fecha=date+"/"+monthInt+"/"+yearInt;

                        }
                        createdAt = null;
                        try {
                            createdAt=new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        id = ds.getKey();


                        Board board = new Board(id, title, createdAt,numNotes);
                        boards.add(board);
                    }
                }
                // Observa como pasamos el activity, con this. Podríamos declarar
                // Activity o Context en el constructor y funcionaría pasando el mismo valor, this
                adapter = new BoardAdapter(BoardActivity.this, boards, R.layout.list_view_board_item);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.notasItem);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.perfilItem:
                                startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.chatPacientesItem:
                                startActivity(new Intent(getApplicationContext(), ChatPacientes.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.foroItem:
                                startActivity(new Intent(getApplicationContext(), ForoDoctorActivity.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.notasItem:
                                return true;
                        }
                        return false;
                    }
                });
    }

    private void createNewBoard(String boardName) {
        Board board = new Board(boardName);
        mDataBase.child("Boards").push().setValue(board).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplication(), "Board Creado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "Error al crear", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editBoard(String newName, Board board) {
        board.setTitle(newName);
        mDataBase.child("Boards").child(board.getId()).child("title").setValue(board.getTitle());


        Toast.makeText(getApplication(), "Se ha editado correctamente", Toast.LENGTH_SHORT).show();

    }

    private void deleteBoard(final Board board) {
        if(boards.size()==1){
            boards.clear();
        }
        mDataBase.child("Boards").child(board.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(BoardActivity.this, "Se ha eliminado satisfactoriamente", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void deleteAll() {
        boards.clear();
        notes.clear();
        mDataBase.child("Notes").removeValue();

        mDataBase.child("Boards").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(BoardActivity.this, "Se ha eliminado satisfactoriamente todo", Toast.LENGTH_SHORT).show();
                    boards.clear();
                }
            }
        });



    }

    //** Dialogs **/

    private void showAlertForCreatingBoard(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewBoard);


        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String boardName = input.getText().toString().trim();
                if (boardName.length() > 0)
                    createNewBoard(boardName);
                else
                    Toast.makeText(getApplicationContext(), "Es necesario un nuevo nombre para el tablero", Toast.LENGTH_LONG).show();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertForEditingBoard(String title, String message, final Board board) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewBoard);
        input.setText(board.getTitle());

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String boardName = input.getText().toString().trim();
                if (boardName.length() == 0)
                    Toast.makeText(getApplicationContext(), "El nombre es requerido al editar el tablero", Toast.LENGTH_LONG).show();
                else if (boardName.equals(board.getTitle()))
                    Toast.makeText(getApplicationContext(), "El nombre es igual al anterior", Toast.LENGTH_LONG).show();
                else
                    editBoard(boardName, board);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /* Events */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_all:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(boards.get(info.position).getTitle());
        getMenuInflater().inflate(R.menu.context_menu_board_activity, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_board:
                deleteBoard(boards.get(info.position));
                return true;
            case R.id.edit_board:
                showAlertForEditingBoard("Editar tablero", "Cambia el nombre del tablero", boards.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(BoardActivity.this, NoteActivity.class);
        intent.putExtra("id", boards.get(position).getId());
        intent.putExtra("title", boards.get(position).getTitle());
        intent.putExtra("numNotes", boards.get(position).getNumNotes());

        startActivity(intent);
    }
}
package com.example.proyectodam.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam.Adapter.NoteAdapter;
import com.example.proyectodam.Models.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.proyectodam.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.content.DialogInterface;
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

public class NoteActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton fab;

    private NoteAdapter adapter;
    private List<Note> notes;
    private DatabaseReference mDataBase;

    private String boardId;
    private String boardTitle;
    private int numNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        notes= new ArrayList<Note>();

        if (getIntent().getExtras() != null)
            boardId = getIntent().getExtras().getString("id");
        boardTitle = getIntent().getExtras().getString("title");
        numNotes = getIntent().getExtras().getInt("numNotes");

        this.setTitle(boardTitle);

        readNoteId(boardId);


        fab = (FloatingActionButton) findViewById(R.id.fabAddNote);
        listView = (ListView) findViewById(R.id.listViewNote);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForCreatingNote("Añadir nueva nota", "El tipo de la nota es " + boardTitle + ".");
            }
        });

        registerForContextMenu(listView);
    }

    private void readNoteId(final String boardId) {
        mDataBase.child("Notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    notes.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("idBoard").getValue().equals(boardId)) {
                            String description = ds.child("description").getValue().toString();

                            String id = ds.getKey();
                            String date = ds.child("createdAt").child("date").getValue().toString();
                            String month = ds.child("createdAt").child("month").getValue().toString();
                            int monthInt = Integer.parseInt(month) + 1;
                            String year = ds.child("createdAt").child("year").getValue().toString();
                            int yearInt = (Integer.parseInt(year) + 1900);
                            String fecha = null;
                            if (Integer.parseInt(month) < 10) {
                                fecha = date + "/0" + monthInt + "/" + yearInt;
                            } else {
                                fecha = date + "/" + monthInt + "/" + yearInt;

                            }
                            Date createdAt = null;
                            try {
                                createdAt = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Note note = new Note(id, description, createdAt, boardId);
                            notes.add(note);
                        }
                    }
                }
                adapter = new NoteAdapter(NoteActivity.this, notes, R.layout.list_view_note_item);

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //** CRUD Actions **/

    private void createNewNote(String note) {
        Note _note = new Note(note, boardId);
        mDataBase.child("Boards").child(boardId).child("numNotes").setValue(++numNotes);
        mDataBase.child("Notes").push().setValue(_note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getApplication(), "Nota Creada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "Error al crear", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    private void editNote(String newNoteDescription, Note note) {
        note.setDescription(newNoteDescription);
        mDataBase.child("Notes").child(note.getId()).child("description").setValue(note.getDescription());
        Toast.makeText(getApplication(), "Se ha editado correctamente", Toast.LENGTH_SHORT).show();

    }




    private void deleteNote(Note note) {
        if(notes.size()==1){
            notes.clear();
        }
        mDataBase.child("Boards").child(boardId).child("numNotes").setValue(--numNotes);
        mDataBase.child("Notes").child(note.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NoteActivity.this, "Se ha eliminado satisfactoriamente", Toast.LENGTH_SHORT).show();



                }
            }
        });

    }

    private void deleteAll() {
        notes.clear();
        numNotes=0;
        mDataBase.child("Boards").child(boardId).child("numNotes").setValue(numNotes);
        mDataBase.child("Notes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NoteActivity.this, "Se ha eliminado satisfactoriamente", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    //** Dialogs **/

    private void showAlertForCreatingNote(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewNote);


        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String note = input.getText().toString().trim();
                if (note.length() > 0)
                    createNewNote(note);
                else
                    Toast.makeText(getApplicationContext(), "La nota no puede estar vacía", Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertForEditingNote(String title, String message, final Note note) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewNote);
        input.setText(note.getDescription());


        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String noteDescription = input.getText().toString().trim();
                if (noteDescription.length() == 0)
                    Toast.makeText(getApplicationContext(), "El texto de la nota es requerido al editar", Toast.LENGTH_LONG).show();
                else if (noteDescription.equals(note.getDescription()))
                    Toast.makeText(getApplicationContext(), "La nota es igual a la anterior", Toast.LENGTH_LONG).show();
                else
                    editNote(noteDescription, note);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /* Events*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_note_activity, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_note:
                deleteNote(notes.get(info.position));
                return true;
            case R.id.edit_note:
                showAlertForEditingNote("Editar la nota", "Cambia el nombre de la nota", notes.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }



}

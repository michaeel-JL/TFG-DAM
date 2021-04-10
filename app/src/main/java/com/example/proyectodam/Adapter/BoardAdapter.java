package com.example.proyectodam.Adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.example.proyectodam.Models.Board;
import com.example.proyectodam.Models.Note;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.proyectodam.R;

public class BoardAdapter extends BaseAdapter {

    private Context context;
    private List<Board> list;
    private int layout;
    private DatabaseReference mDataBase;
    private List<Note> notes = new ArrayList<Note>();


    public BoardAdapter(Context context, List<Board> boards, int layout) {
        this.context = context;
        this.list = boards;
        this.layout = layout;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Board getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        final ViewHolder vh;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            vh = new ViewHolder();
            vh.title = (TextView) convertView.findViewById(R.id.textViewBoardTitle);
            vh.notes = (TextView) convertView.findViewById(R.id.textViewBoardNotes);
            vh.createdAt = (TextView) convertView.findViewById(R.id.textViewBoardDate);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        Board board = list.get(position);
        vh.title.setText(board.getTitle());
        int numberOfNotes = board.getNumNotes();
        String textForNotes = (numberOfNotes == 1) ? numberOfNotes + " Nota" : numberOfNotes + " Notas";
        vh.notes.setText(textForNotes);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String createdAt = df.format(board.getCreatedAt());
        vh.createdAt.setText(createdAt);



        return convertView;

    }

    public class ViewHolder {
        TextView title;
        TextView notes;
        TextView createdAt;
    }
}
package com.example.proyectodam.Models;

import java.util.Date;

public class Board {

     private String id;
    private String title;
    private Date createdAt;
    private int numNotes = 0;

    public Board() {

    }

    public Board( String title) {
        this.title = title;
        this.createdAt = new Date();
    }
    public Board(String id, String title, Date createdAt, int numNotes) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.numNotes =  numNotes;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public int getNumNotes(){return  numNotes;}


}

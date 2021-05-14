package com.example.proyectodam.Models;

import java.util.Date;

public class Note {

    private String id;
    private String description;
    private Date createdAt;
    private String idBoard;
    public Note() {

    }

    public Note( String description,String idBoard) {
        this.idBoard=idBoard;
        this.description = description;
        this.createdAt = new Date();
    }
    public Note(String id, String description, Date createdAt,String idBoard) {
        this.id = id;
        this.idBoard=idBoard;
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public String getIdBoard() {
        return idBoard;
    }

}
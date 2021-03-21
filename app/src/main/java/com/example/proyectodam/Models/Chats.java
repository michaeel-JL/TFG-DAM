package com.example.proyectodam.Models;

public class Chats {

    private String id, titulo, image;

    public Chats() {
    }

    public Chats(String id, String titulo, String image) {
        this.id = id;
        this.titulo = titulo;
        this.image = image;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

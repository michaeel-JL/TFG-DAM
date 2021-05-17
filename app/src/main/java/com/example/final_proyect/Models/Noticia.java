package com.example.final_proyect.Models;

public class Noticia {

    private String id, titulo, description, imagePrincipal, imagenSecundaria, textoNoticia, etiqueta;

    public Noticia() {
    }


    public Noticia(String id, String titulo, String description, String imagePrincipal, String imagenSecundaria, String textoNoticia, String etiqueta) {
        this.id = id;
        this.titulo = titulo;
        this.description = description;
        this.imagePrincipal = imagePrincipal;
        this.imagenSecundaria = imagenSecundaria;
        this.textoNoticia = textoNoticia;
        this.etiqueta = etiqueta;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePrincipal() {
        return imagePrincipal;
    }

    public void setImagePrincipal(String imagePrincipal) {
        this.imagePrincipal = imagePrincipal;
    }

    public String getImagenSecundaria() {
        return imagenSecundaria;
    }

    public void setImagenSecundaria(String imagenSecundaria) {
        this.imagenSecundaria = imagenSecundaria;
    }

    public String getTextoNoticia() {
        return textoNoticia;
    }

    public void setTextoNoticia(String textoNoticia) {
        this.textoNoticia = textoNoticia;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
}
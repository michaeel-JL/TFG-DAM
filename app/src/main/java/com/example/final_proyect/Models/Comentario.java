package com.example.final_proyect.Models;

public class Comentario {

    private String id;
    private String envia;
    private String id_noticia;
    private String mensaje;
    private String fecha;
    private String hora;

    public Comentario() {
    }

    public Comentario(String id, String envia, String id_noticia, String mensaje, String fecha, String hora) {
        this.id = id;
        this.envia = envia;
        this.id_noticia = id_noticia;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnvia() {
        return envia;
    }

    public void setEnvia(String envia) {
        this.envia = envia;
    }

    public String getId_noticia() {
        return id_noticia;
    }

    public void setId_noticia(String id_noticia) {
        this.id_noticia = id_noticia;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}

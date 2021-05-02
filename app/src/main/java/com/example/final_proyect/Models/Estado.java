package com.example.final_proyect.Models;

public class Estado {
    String fecha;
    String hora;
    String online;
    String chatcon;

    public Estado() {
    }

    public Estado(String fecha, String hora, String online, String chatcon) {
        this.fecha = fecha;
        this.hora = hora;
        this.online = online;
        this.chatcon = chatcon;
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

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getChatcon() {
        return chatcon;
    }

    public void setChatcon(String chatcon) {
        this.chatcon = chatcon;
    }
}

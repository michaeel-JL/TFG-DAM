package com.example.final_proyect.Models;

public class Estado {
    String fecha;
    String hora;
    String status;

    public Estado() {
    }

    public Estado(String fecha, String hora, String status ) {
        this.fecha = fecha;
        this.hora = hora;
        this.status = status;
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
        return status;
    }

    public void setOnline(String online) {
        this.status = status;
    }

}

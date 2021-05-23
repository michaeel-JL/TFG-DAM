package com.example.final_proyect.Models;

public class Alergia {

    private String id;
    private String nombre;
    private String gravedad;
    private String descripcion;

    public  Alergia(){

    }

    public Alergia(String id, String nombre, String gravedad, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.gravedad = gravedad;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGravedad() {
        return gravedad;
    }

    public void setGravedad(String gravedad) {
        this.gravedad = gravedad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

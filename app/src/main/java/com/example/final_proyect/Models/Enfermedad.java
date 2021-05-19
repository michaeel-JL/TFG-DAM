package com.example.final_proyect.Models;

public class Enfermedad {

    private String id;
    private String nombre;
    private String detalles;
    private String fecha_diagnostico;
    private String fecha_resolucion;

    public Enfermedad() {
    }

    public Enfermedad(String id, String nombre, String detalles, String fecha_diagnostico, String fecha_resolucion) {
        this.id = id;
        this.nombre = nombre;
        this.detalles = detalles;
        this.fecha_diagnostico = fecha_diagnostico;
        this.fecha_resolucion = fecha_resolucion;
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

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getFecha_diagnostico() {
        return fecha_diagnostico;
    }

    public void setFecha_diagnostico(String fecha_diagnostico) {
        this.fecha_diagnostico = fecha_diagnostico;
    }

    public String getFecha_resolucion() {
        return fecha_resolucion;
    }

    public void setFecha_resolucion(String fecha_resolucion) {
        this.fecha_resolucion = fecha_resolucion;
    }
}

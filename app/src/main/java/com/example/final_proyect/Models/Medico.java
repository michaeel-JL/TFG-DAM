package com.example.final_proyect.Models;

public class Medico extends Usuario{

    private String especialidad;

    public Medico() {
    }

    public Medico(String id, String nombre, String apellidos, String sexo, String edad, String email, String password, String foto, String rol, String especialidad) {
        super(id, nombre, apellidos, sexo, edad, email, password, foto, rol);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}

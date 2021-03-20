package com.example.medic.Models;

public class Usuarios {
    private String id, nombreUsuario, edad, email, password, foto, rol;

    public Usuarios(String id, String nombreUsuario,String edad, String email, String password, String foto, String rol) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.edad = edad;
        this.email = email;
        this.password = password;
        this.foto = foto;
        this.rol = rol;
    }

    public Usuarios() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

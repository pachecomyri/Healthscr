package com.example.healthscribe.Model;

import java.io.Serializable;

public class Enfermedad implements Serializable {
    private Integer id;
    private String nombre;
    private Integer tipo;
    private String sintomas;

    public Enfermedad() {
    }

    public Enfermedad(Integer id, String nombre, Integer tipo, String sintomas) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.sintomas = sintomas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

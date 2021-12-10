package com.example.healthscribe.Model;


import java.io.Serializable;

public class Medicamento implements Serializable {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String efectosAdv;

    public Medicamento() {
    }

    public Medicamento(Integer id, String nombre, String descripcion, String efectosAdv) {
        this.id = id;
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.efectosAdv=efectosAdv;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEfectosAdv() {
        return efectosAdv;
    }

    public void setEfectosAdv(String efectosAdv) {
        this.efectosAdv = efectosAdv;
    }

    @Override
    public String toString() {
        return nombre;
    }


}

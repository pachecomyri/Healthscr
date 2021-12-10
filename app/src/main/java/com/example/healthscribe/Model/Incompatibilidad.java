package com.example.healthscribe.Model;

import java.util.List;

public class Incompatibilidad {
    private Integer id;
    private Medicamento medicamento;
    private List<Medicamento> listaMedicamentos;
    private List<Enfermedad> listaEnfermedades;
    private List<Enfermedad> listaAlergias;

    public Incompatibilidad() {
    }

    public Incompatibilidad(Integer id, Medicamento medicamento, List<Medicamento> listaMedicamentos, List<Enfermedad> listaEnfermedades, List<Enfermedad> listaAlergias) {
        this.id = id;
        this.medicamento = medicamento;
        this.listaMedicamentos = listaMedicamentos;
        this.listaEnfermedades = listaEnfermedades;
        this.listaAlergias = listaAlergias;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public List<Medicamento> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public void setListaMedicamentos(List<Medicamento> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    public List<Enfermedad> getListaEnfermedades() {
        return listaEnfermedades;
    }

    public void setListaEnfermedades(List<Enfermedad> listaEnfermedades) {
        this.listaEnfermedades = listaEnfermedades;
    }

    public List<Enfermedad> getListaAlergias() {
        return listaAlergias;
    }

    public void setListaAlergias(List<Enfermedad> listaAlergias) {
        this.listaAlergias = listaAlergias;
    }

    @Override
    public String toString() {
        return "Incompatibilidad de " + medicamento;
    }
}

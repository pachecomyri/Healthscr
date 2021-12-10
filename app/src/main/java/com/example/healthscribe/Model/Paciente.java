package com.example.healthscribe.Model;

public class Paciente{
    private Integer id;
    private Usuario usuario;
    private HistoriaClinica historiaClinica;

    public Paciente() {
    }

    public Paciente(Integer id, Usuario usuario, HistoriaClinica historiaClinica) {
        this.id = id;
        this.usuario = usuario;
        this.historiaClinica = historiaClinica;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public HistoriaClinica getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    @Override
    public String toString() {
        return usuario.getNombre()+" "+usuario.getApellidos();
    }
}

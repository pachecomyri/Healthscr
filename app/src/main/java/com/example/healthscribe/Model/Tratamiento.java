package com.example.healthscribe.Model;

import java.io.Serializable;
import java.util.List;

public class Tratamiento  implements Serializable{
    private Integer id;
    private Medicamento medicamento;
    private String motivo;
    private String fechaInicio;
    private String fechaFin;

    public Tratamiento() {
    }

    public Tratamiento(Integer id, Medicamento medicamento, String motivo, String fechaInicio  , String fechaFin) {
        this.id = id;
        this.medicamento=medicamento;
        this.motivo = motivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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


    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public String toString() {
        return "Tratamiento " + fechaInicio+" "+medicamento;
    }
}

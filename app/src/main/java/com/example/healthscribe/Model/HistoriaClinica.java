package com.example.healthscribe.Model;

import java.util.List;

public class HistoriaClinica {
    private Integer id;
    private List<Tratamiento> tratamientos;
    private List<Enfermedad> enfermedades;
    private List<Enfermedad> alergias;
   private List<Medicamento> medicamentos;

    public HistoriaClinica() {
    }

    public HistoriaClinica(Integer id, List<Tratamiento> tratamientos,List<Enfermedad> enfermedades, List<Enfermedad> alergias ,List<Medicamento> medicamentos) {
        this.id = id;
        this.tratamientos = tratamientos;
        this.enfermedades = enfermedades;
        this.alergias = alergias;
        this.medicamentos = medicamentos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Tratamiento> getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(List<Tratamiento> tratamientos) {
        this.tratamientos = tratamientos;
    }

    public List<Enfermedad> getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(List<Enfermedad> enfermedades) {
        this.enfermedades = enfermedades;
    }

    public List<Enfermedad> getAlergias() {
        return alergias;
    }

    public void setAlergias(List<Enfermedad> alergias) {
        this.alergias = alergias;
    }

    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }



    @Override
    public String toString() {
        return "Historia Clínica " + id;
    }
}

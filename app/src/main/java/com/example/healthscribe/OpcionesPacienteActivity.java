package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class OpcionesPacienteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_paciente);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Método botón Recetar Tratamiento
    public void accRecetarTratamiento(View view){
        Integer id = getIntent().getIntExtra("PacienteSel",-1);
        Intent accRecetar = new Intent(OpcionesPacienteActivity.this, CreacionTratamientoActivity.class);
        accRecetar.putExtra("PacienteR", id);
        startActivity(accRecetar);

    }

    //Método botón Historia Clínica
    public void accHistoriaClinica(View view){
        Integer id = getIntent().getIntExtra("PacienteSel",-1);
        Intent accHistoria = new Intent(OpcionesPacienteActivity.this, HistoriaClinicaActivity.class);
        accHistoria.putExtra("PacienteS", id);
        startActivity(accHistoria);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.example.healthscribe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuMedicoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_medico);
    }

    //Método botón Pacientes
    public void accesoPacientes(View view){
       Intent accPac = new Intent(MenuMedicoActivity.this, PacienteActivity.class);
       startActivity(accPac);
    }

    //Método botón Perfil
    public void accesoPerfilMed(View view){
        Integer id = getIntent().getIntExtra("idMedico",-1);
        Intent accesoPerf = new Intent(MenuMedicoActivity.this, PerfilMedicoActivity.class);
        accesoPerf.putExtra("idMedic",id);
        startActivity(accesoPerf);
    }


}
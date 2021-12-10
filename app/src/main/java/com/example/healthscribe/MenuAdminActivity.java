package com.example.healthscribe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
    }

    //Método botón Registrar Médico
    public void accRegistrarMedico(View view){
        Intent registrarMed= new Intent(MenuAdminActivity.this,RegistrarMedicoActivity.class);
        startActivity(registrarMed);
    }

    //Método botón Usuarios
    public void accesoUsuarios(View view){
       Intent accesoUsu = new Intent(MenuAdminActivity.this, UsuarioActivity.class);
        startActivity(accesoUsu);
    }

    //Método botón Medicamentos
    public void accesoMedicamentos(View view){
        Intent accesoMedic = new Intent(MenuAdminActivity.this, MedicamentoActivity.class);
        startActivity(accesoMedic);
    }

    //Método botón Enfermedades
    public void accesoEnfermedades(View view){
        Intent accesoEnf = new Intent(MenuAdminActivity.this, EnfermedadActivity.class);
        startActivity(accesoEnf);
    }

    //Método botón Alergias
    public void accesoAlergias(View view){
       Intent accesoAler = new Intent(MenuAdminActivity.this, AlergiaActivity.class);
       startActivity(accesoAler);
    }

    //Método botón Incompatibilidades
    public void accesoIncompatibilidades(View view){
        Intent accesoInc = new Intent(MenuAdminActivity.this, IncompatibilidadActivity.class);
        startActivity(accesoInc);
    }

    //Método botón Perfil
    public void accesoPerfil(View view){
        Integer id = getIntent().getIntExtra("idAdministrador",-1);
        Intent accesoPerf = new Intent(MenuAdminActivity.this, PerfilAdminActivity.class);
        accesoPerf.putExtra("idAdminist", id);
        startActivity(accesoPerf);

    }

}
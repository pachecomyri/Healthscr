package com.example.healthscribe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }

    //Método para botón Iniciar sesión
    public void IniciarSesion(View view){
        Intent iniciarSesion = new Intent(InicioActivity.this, EntrarActivity.class);
        startActivity(iniciarSesion);

    }
    //Método para botón registrarse
    public void Registrarse(View view){
        Intent registrarse = new Intent(InicioActivity.this, RegistrarActivity.class);
        startActivity(registrarse);
    }

}
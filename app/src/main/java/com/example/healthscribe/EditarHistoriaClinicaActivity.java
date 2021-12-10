package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditarHistoriaClinicaActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_historia_clinica);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //Método botón lista enfermedades
    public void accListaEnfHis(View view){
        Integer id = getIntent().getIntExtra("PacienteHistoria",-1);
        Intent intent = new Intent(EditarHistoriaClinicaActivity.this, ListaEnfermHistActivity.class);
        intent.putExtra("PacienteHisto", id);
        startActivity(intent);

    }

    //Método botón lista alergias
    public void accListaAlerHis(View view){
        Integer id = getIntent().getIntExtra("PacienteHistoria",-1);
        Intent intent = new Intent(EditarHistoriaClinicaActivity.this, ListaAlerActivity.class);
        intent.putExtra("PacienteHisto", id);
        startActivity(intent);

    }


    //Método botón lista medicamentos
    public void accListaMedicam(View view){
        Integer id = getIntent().getIntExtra("PacienteHistoria",-1);
        Intent intent = new Intent(EditarHistoriaClinicaActivity.this, ListaMedicamHistoriaActivity.class);
        intent.putExtra("PacienteHisto", id);
        startActivity(intent);

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
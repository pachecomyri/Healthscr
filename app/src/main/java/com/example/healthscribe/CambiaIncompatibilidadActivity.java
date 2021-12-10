package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.healthscribe.Model.Incompatibilidad;
import com.example.healthscribe.Model.Medicamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CambiaIncompatibilidadActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_incompatibilidad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    //Método cambia Lista Medicamentos
    public void cambiaMedicamentos(View view){
        Integer id = getIntent().getIntExtra("incompatibilidadSel", -1);
        Intent cambiaMed = new Intent(CambiaIncompatibilidadActivity.this, CambiaListaMedicamActivity.class);
        cambiaMed.putExtra("incompatibilidadS", id);
        startActivity(cambiaMed);

    }

    //Método cambia Lista Enfermedades
    public void cambiaEnfermedades(View view){
        Integer id = getIntent().getIntExtra("incompatibilidadSel", -1);
        Intent cambiaEnf = new Intent(CambiaIncompatibilidadActivity.this, CambiaListaEnfermActivity.class);
        cambiaEnf.putExtra("incompatibilidadS", id);
        startActivity(cambiaEnf);
    }

    //Método cambia Lista Enfermedades
    public void cambiaAlergias(View view){
        Integer id = getIntent().getIntExtra("incompatibilidadSel", -1);
        Intent cambiaAler = new Intent(CambiaIncompatibilidadActivity.this, CambiaListaAlerActivity.class);
        cambiaAler.putExtra("incompatibilidadS", id);
        startActivity(cambiaAler);
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
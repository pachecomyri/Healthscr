package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.healthscribe.Model.HistoriaClinica;
import com.example.healthscribe.Model.Paciente;
import com.example.healthscribe.Model.Tratamiento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private DatabaseReference refPac;
    final List<Paciente> listaPac = new ArrayList<Paciente>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        refPac = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");

        refPac.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Paciente pac = data.getValue(Paciente.class);
                    listaPac.add(pac);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //Método botón tratamientos
    public void accesoTratamientos(View view){
        Integer id = getIntent().getIntExtra("idPaciente",-1);
        for(Paciente pac: listaPac) {
            if(pac.getId().equals(id)) {
                HistoriaClinica h = pac.getHistoriaClinica();
                Integer idH = h.getId();
                Intent accesoTrat = new Intent(MenuActivity.this, TratamientoActivity.class);
                accesoTrat.putExtra("idPacHis", idH);
                startActivity(accesoTrat);
            }
        }

    }

    //Método botón Historia clínica
    public void accesoHC(View view){
        Integer id = getIntent().getIntExtra("idPaciente",-1);
        Intent accesoH = new Intent(MenuActivity.this,MiHistoriaActivity.class);
        accesoH.putExtra("idPacHis",id);
        startActivity(accesoH);
    }

    //Método botón Perfil
    public void accesoPerfilPac(View view){
        Integer id = getIntent().getIntExtra("idPaciente",-1);
        Intent accesoPerf = new Intent(MenuActivity.this, PerfilActivity.class);
        accesoPerf.putExtra("idPacien",id);
        startActivity(accesoPerf);
    }

}
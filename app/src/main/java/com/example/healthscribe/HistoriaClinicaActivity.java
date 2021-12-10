package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.healthscribe.Model.HistoriaClinica;
import com.example.healthscribe.Model.Paciente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoriaClinicaActivity extends AppCompatActivity {

    private TextView tvTrat, tvEnf, tvAler, tvMedicam;
    private DatabaseReference refPac;
    final ArrayList<Paciente> listaPac = new ArrayList<Paciente>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia_clinica);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refPac = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");

        tvTrat = findViewById(R.id.tv_trat_historia);
        tvEnf = findViewById(R.id.tv_enf_historia);
        tvAler = findViewById(R.id.tv_aler_historia);
        tvMedicam = findViewById(R.id.tv_medicamento_historia);

        Integer id = getIntent().getIntExtra("PacienteS",-1);

        refPac.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Paciente p = data.getValue(Paciente.class);
                    listaPac.add(p);
                    for(Paciente pac: listaPac){
                        if(pac.getId().equals(id)){
                            HistoriaClinica h = pac.getHistoriaClinica();
                            String listTrat = String.valueOf(h.getTratamientos());
                            String listEnf = String.valueOf(h.getEnfermedades());
                            String listAler = String.valueOf(h.getAlergias());
                            String lisMedic = String.valueOf(h.getMedicamentos());
                            if(h.getTratamientos()==null){
                                tvTrat.setText("");
                            }else{
                                tvTrat.setText(listTrat.replace("[", "").replace("]", ""));
                            }
                            if(h.getMedicamentos()==null){
                                tvMedicam.setText("");
                            }else{
                                tvMedicam.setText(lisMedic.replace("[", "").replace("]", ""));
                            }
                            if(h.getEnfermedades()==null){
                                tvEnf.setText("");
                            }else{
                                tvEnf.setText(listEnf.replace("[", "").replace("]", ""));
                            }
                           if(h.getAlergias()==null){
                               tvAler.setText("");
                           }else {
                               tvAler.setText(listAler.replace("[", "").replace("]", ""));
                            }

                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });


    }

    //Método botón modificar
    public void accModificaHistoria(View view){
        Integer id = getIntent().getIntExtra("PacienteS",-1);
        Intent accModHistoria = new Intent(HistoriaClinicaActivity.this, EditarHistoriaClinicaActivity.class);
        accModHistoria.putExtra("PacienteHistoria",id);
        startActivity(accModHistoria);

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
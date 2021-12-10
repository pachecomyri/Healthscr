package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.healthscribe.Model.HistoriaClinica;
import com.example.healthscribe.Model.Paciente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MiHistoriaActivity extends AppCompatActivity {

    private TextView tvMedicam, tvEnf, tvAler;
    private DatabaseReference refPac;

    final ArrayList<Paciente> listaPac = new ArrayList<Paciente>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_historia);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refPac = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");

        tvEnf = findViewById(R.id.tv_mi_enferm);
        tvAler = findViewById(R.id.tv_mi_aler);
        tvMedicam = findViewById(R.id.tv_mi_medicam);

        Integer id = getIntent().getIntExtra("idPacHis",-1);


        refPac.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Paciente p = data.getValue(Paciente.class);
                    listaPac.add(p);
                    for(Paciente pac: listaPac){
                        if(pac.getId().equals(id)){
                            HistoriaClinica h = pac.getHistoriaClinica();
                            String listEnf = String.valueOf(h.getEnfermedades());
                            String listAler = String.valueOf(h.getAlergias());
                            String lisMedic = String.valueOf(h.getMedicamentos());
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
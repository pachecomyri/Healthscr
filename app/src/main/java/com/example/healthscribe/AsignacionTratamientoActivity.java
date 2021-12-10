package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.healthscribe.Model.Enfermedad;
import com.example.healthscribe.Model.HistoriaClinica;
import com.example.healthscribe.Model.Incompatibilidad;
import com.example.healthscribe.Model.Paciente;
import com.example.healthscribe.Model.Tratamiento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AsignacionTratamientoActivity extends AppCompatActivity {

    private Button bAsignar;
    private DatabaseReference refPac, refTrat, refHis, refInc;
    final List<Paciente> listaPac = new ArrayList<Paciente>();
    final List<Tratamiento> listaT = new ArrayList<Tratamiento>();
    final List<Incompatibilidad> listaInc = new ArrayList<Incompatibilidad>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignacion_tratamiento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refHis = FirebaseDatabase.getInstance().getReference("Tablas").child("HistoriaClinica");
        refPac = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");
        refTrat = FirebaseDatabase.getInstance().getReference("Tablas").child("Tratamiento");
        refInc = FirebaseDatabase.getInstance().getReference("Tablas").child("Incompatibilidad");

        bAsignar = findViewById(R.id.b_asigna_trat);


        refPac.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Paciente p = data.getValue(Paciente.class);
                    listaPac.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        refTrat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Tratamiento t = data.getValue(Tratamiento.class);
                    listaT.add(t);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        refInc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Incompatibilidad incom = data.getValue(Incompatibilidad.class);
                    listaInc.add(incom);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());
            }
        });


        bAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tratamiento trat = (Tratamiento) getIntent().getSerializableExtra("TratamientoRec");
                Integer idP = getIntent().getIntExtra("idPacienteReceta", -1);
                List<Tratamiento> ListaTr = new ArrayList<Tratamiento>();

                for(Paciente pac: listaPac){
                    if(pac.getId().equals(idP)){
                        HistoriaClinica his = pac.getHistoriaClinica();
                        Integer idH=his.getId();
                            for(Incompatibilidad incom: listaInc){
                                    if(trat.getMedicamento().getNombre().equals(incom.getMedicamento().getNombre())){
                                        for(Enfermedad enf: his.getEnfermedades()){
                                            for(Enfermedad enferm: incom.getListaEnfermedades()){
                                                if(enferm.getNombre().equals(enf.getNombre())){
                                                    Toast.makeText(AsignacionTratamientoActivity.this, "El tratamiento no se puede recetar", Toast.LENGTH_SHORT).show();

                                                }else{
                                                    if(his.getTratamientos()==null) {
                                                        ListaTr.add(trat);
                                                        his.setTratamientos(ListaTr);
                                                        refHis.child(String.valueOf(idH)).setValue(his);
                                                        pac.setHistoriaClinica(his);
                                                        refPac.child(String.valueOf(idP)).setValue(pac);
                                                        Intent intent = new Intent(AsignacionTratamientoActivity.this, MenuMedicoActivity.class);
                                                        startActivity(intent);
                                                        Toast.makeText(AsignacionTratamientoActivity.this, "El tratamiento es recetado", Toast.LENGTH_SHORT).show();
                                                    }else if(his.getTratamientos()!=null){
                                                        List<Tratamiento> listaTrata = his.getTratamientos();
                                                        listaTrata.add(trat);
                                                        his.setTratamientos(listaTrata);
                                                        refHis.child(String.valueOf(idH)).setValue(his);
                                                        pac.setHistoriaClinica(his);
                                                        refPac.child(String.valueOf(idP)).setValue(pac);
                                                        Intent intent = new Intent(AsignacionTratamientoActivity.this, MenuMedicoActivity.class);
                                                        startActivity(intent);
                                                        Toast.makeText(AsignacionTratamientoActivity.this, "El tratamiento es recetado", Toast.LENGTH_SHORT).show();
                                                    }

                                        }

                                    }
                                }
                            }

                        }
                        }
                    }
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
package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthscribe.Model.Medicamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CambiaMedicamentoActivity extends AppCompatActivity {

    private EditText etNomSelec, etDescSelec, etEfectosSelec;

    private DatabaseReference refMedicam;

    final ArrayList<Medicamento> listaMedicam = new ArrayList<Medicamento>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_medicamento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refMedicam = FirebaseDatabase.getInstance().getReference("Tablas").child("Medicamento");

        etNomSelec = (EditText)findViewById(R.id.txt_nom_med_sel);
        etDescSelec = (EditText)findViewById(R.id.txt_desc_med_sel);
        etEfectosSelec = (EditText)findViewById(R.id.txt_efec_med_sel);

        Integer id = getIntent().getIntExtra("medicamentoSel",-1);

        refMedicam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Medicamento m = data.getValue(Medicamento.class);
                    listaMedicam.add(m);
                    for(Medicamento med: listaMedicam){
                        if(med.getId().equals(id)){
                            etNomSelec.setText(med.getNombre());
                            etDescSelec.setText(med.getDescripcion());
                            etEfectosSelec.setText(med.getEfectosAdv());
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

    //Método modificar
    public void modificarMedicamentoSelec(View view){
        Integer id = getIntent().getIntExtra("medicamentoSel",-1);
        String nomS = etNomSelec.getText().toString();
        String descS = etDescSelec.getText().toString();
        String efecS = etEfectosSelec.getText().toString();

        for(Medicamento med : listaMedicam){
            if(med.getId().equals(id)){
                med.setNombre(nomS);
                med.setDescripcion(descS);
                med.setEfectosAdv(efecS);

                refMedicam.child(String.valueOf(id)).setValue(med);

                Toast.makeText(CambiaMedicamentoActivity.this, "El medicamento "+ med.getNombre()+" ha sido modificado",Toast.LENGTH_LONG).show();
            }
        }

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
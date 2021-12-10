package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.healthscribe.Model.Medicamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MedicamentoSpinnerActivity extends AppCompatActivity {

    private DatabaseReference refSp;
    private Spinner mSpinner;
    private Button bEnviar;

    final List<Medicamento> medicamentos = new ArrayList<Medicamento>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamento_spinner);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refSp = FirebaseDatabase.getInstance().getReference("Tablas").child("Medicamento");

        mSpinner = (Spinner)findViewById(R.id.sp_medicamento);
        bEnviar = (Button) findViewById(R.id.b_enviar_medicam);

        refSp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot data: snapshot.getChildren()){
                        String descripcion = data.child("descripcion").getValue().toString();
                        String nombre = data.child("nombre").getValue().toString();
                        String efectosAdv = data.child("efectosAdv").getValue().toString();
                        Integer id = Integer.valueOf(data.child("id").getValue().toString());

                        medicamentos.add(new Medicamento(id, nombre, descripcion, efectosAdv));
                    }
                    ArrayAdapter<Medicamento> arrayAdapter  = new ArrayAdapter<>(MedicamentoSpinnerActivity.this, android.R.layout.simple_dropdown_item_1line, medicamentos);
                    mSpinner.setAdapter(arrayAdapter);

                    bEnviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Medicamento medic =(Medicamento) mSpinner.getSelectedItem();
                            Intent envio = new Intent(MedicamentoSpinnerActivity.this, ListaMedicamIncompActivity.class);
                            envio.putExtra("MedicamentoInc", medic);
                            startActivity(envio);
                        }

                    });


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
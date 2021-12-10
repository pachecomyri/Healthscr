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
import android.widget.Toast;

import com.example.healthscribe.Model.Enfermedad;
import com.example.healthscribe.Model.Incompatibilidad;
import com.example.healthscribe.Model.Medicamento;
import com.example.healthscribe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CrearIncompatibilidadActivity extends AppCompatActivity {

    private Button bCrear;
    private DatabaseReference refInc;

    final ArrayList<Incompatibilidad> incompatibilidades = new ArrayList<Incompatibilidad>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incompatibilidad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refInc = FirebaseDatabase.getInstance().getReference("Tablas").child("Incompatibilidad");

        bCrear = (Button) findViewById(R.id.b_crear_incompatibilidad);

        refInc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Incompatibilidad inc = data.getValue(Incompatibilidad.class);
                    incompatibilidades.add(inc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        bCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medicamento medic = (Medicamento) getIntent().getSerializableExtra("MedicamentoInc3");
                List<Medicamento> medicamentos = (List<Medicamento>) getIntent().getSerializableExtra("ListaMedicamentos2");
                List<Enfermedad> enfermedades = (List<Enfermedad>) getIntent().getSerializableExtra("ListaEnfermedades1");
                List<Enfermedad> alergias = (List<Enfermedad>) getIntent().getSerializableExtra("ListaAlergias");

                if(!incompatibilidades.isEmpty()){
                    for(Incompatibilidad  in: incompatibilidades) {
                        if(in.getMedicamento().equals(medic)){
                            Toast.makeText(CrearIncompatibilidadActivity.this, "La incompatibilidad para " + String.valueOf(medic) + " ya existe", Toast.LENGTH_LONG).show();
                        }else {
                            Incompatibilidad i = incompatibilidades.get(incompatibilidades.size() - 1);
                            Integer id = i.getId() + 1;
                            Incompatibilidad inc = new Incompatibilidad(id, medic, medicamentos, enfermedades, alergias);
                            refInc.child(String.valueOf(id)).setValue(inc);
                            Toast.makeText(CrearIncompatibilidadActivity.this, "La incompatibilidad para " + String.valueOf(medic) + " ha sido creada", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CrearIncompatibilidadActivity.this, IncompatibilidadActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }


                }else{
                    Integer id = incompatibilidades.size()+1;
                    Incompatibilidad in = new Incompatibilidad(id, medic, medicamentos, enfermedades, alergias);
                    refInc.child(String.valueOf(id)).setValue(in);
                    Toast.makeText(CrearIncompatibilidadActivity.this, "La incompatibilidad para " + String.valueOf(medic) + " ha sido creada", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CrearIncompatibilidadActivity.this, IncompatibilidadActivity.class);
                    startActivity(intent);
                    finish();
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



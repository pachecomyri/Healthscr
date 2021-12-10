package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
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
import java.util.UUID;

public class CrearMedicamentoActivity extends AppCompatActivity {

    private EditText etnombre, etdescripcion, etefectos;

    private DatabaseReference refMedicamento;

    final ArrayList<Medicamento> listMedicamentos = new ArrayList<Medicamento>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_medicamento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refMedicamento = FirebaseDatabase.getInstance().getReference("Tablas").child("Medicamento");

        etnombre = (EditText)findViewById(R.id.txt_nombre_medicam);
        etdescripcion = (EditText)findViewById(R.id.txt_descr_medicam);
        etefectos = (EditText)findViewById(R.id.txt_efectos_medicam);

        refMedicamento.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Medicamento m = data.getValue(Medicamento.class);
                    listMedicamentos.add(m);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });
    }

    public void crearMedicamento(View view){
        String nombre = etnombre.getText().toString();
        String descripcion = etdescripcion.getText().toString();
        String efectos = etefectos.getText().toString();

        if(!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(descripcion) && !TextUtils.isEmpty(efectos)){
            if(!listMedicamentos.isEmpty()){
                for(Medicamento m: listMedicamentos){
                    if(m.getNombre().equals(nombre)){
                        Toast.makeText(this, "El medicamento " + nombre + " ya existe", Toast.LENGTH_SHORT).show();
                    }else{
                        Medicamento medic = listMedicamentos.get(listMedicamentos.size()-1);
                        Integer id = medic.getId()+1;
                        Medicamento med = new Medicamento(id, nombre, descripcion, efectos);
                        refMedicamento.child(String.valueOf(id)).setValue(med);
                        Toast.makeText(this, "El medicamento " + nombre + " ha sido registrado", Toast.LENGTH_SHORT).show();
                        etnombre.setText("");
                        etdescripcion.setText("");
                        etefectos.setText("");
                    }
                }

            }else{
                Integer id = listMedicamentos.size()+1;
                Medicamento med = new Medicamento(id, nombre, descripcion, efectos);
                refMedicamento.child(String.valueOf(id)).setValue(med);
                Toast.makeText(this, "El medicamento " + nombre + " ha sido registrado", Toast.LENGTH_SHORT).show();
                etnombre.setText("");
                etdescripcion.setText("");
                etefectos.setText("");
            }

        }else{
            Toast.makeText(this, "Se deben rellenar todos los campos", Toast.LENGTH_SHORT).show();
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
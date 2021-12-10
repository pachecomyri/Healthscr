package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthscribe.Model.Medicamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatosMedicamentoActivity extends AppCompatActivity {

    private TextView tvNombreSelec, tvDescripSelec, tvEfectosSelec;

    private Button bEliminar;

    private DatabaseReference refMedicam;

    final ArrayList<Medicamento> listaMedicam = new ArrayList<Medicamento>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_medicamento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refMedicam = FirebaseDatabase.getInstance().getReference("Tablas").child("Medicamento");

        tvNombreSelec = (TextView)findViewById(R.id.tv_nom_med_sel);
        tvDescripSelec = (TextView)findViewById(R.id.tv_desc_med_sel);
        tvEfectosSelec = (TextView)findViewById(R.id.tv_efec_med_sel);
        bEliminar = (Button)findViewById(R.id.b_eli_med_sel);

        Integer id = getIntent().getIntExtra("medicamentoSelec", -1);

        refMedicam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Medicamento m = data.getValue(Medicamento.class);
                    listaMedicam.add(m);
                    for(Medicamento med: listaMedicam){
                        if(med.getId().equals(id)){
                            tvNombreSelec.setText(med.getNombre());
                            tvDescripSelec.setText(med.getDescripcion());
                            tvEfectosSelec.setText(med.getEfectosAdv());
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        bEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(DatosMedicamentoActivity.this);
                alerta.setMessage("¿Está seguro de que desea eliminar este medicamento?");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        refMedicam.child(String.valueOf(id)).removeValue();
                        Toast.makeText(DatosMedicamentoActivity.this, "Este medicamento ha sido eliminado", Toast.LENGTH_SHORT).show();

                    }
                });

                alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Eliminar medicamento");
                titulo.show();
            }
        });
    }

    //Método botón modificar
    public void accModificarMedicamentoSelec(View view){
        Integer id = getIntent().getIntExtra("medicamentoSelec", -1);
        Intent modificaMedicam = new Intent(DatosMedicamentoActivity.this, CambiaMedicamentoActivity.class);
        modificaMedicam.putExtra("medicamentoSel", id);
        startActivity(modificaMedicam);

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
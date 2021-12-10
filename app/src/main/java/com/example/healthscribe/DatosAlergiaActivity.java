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

import com.example.healthscribe.Model.Enfermedad;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatosAlergiaActivity extends AppCompatActivity {

    private TextView tvNomSelec, tvSinSelec;

    private Button bEliminar;

    private DatabaseReference refAler;

    final ArrayList<Enfermedad> listaAler = new ArrayList<Enfermedad>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_alergia);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refAler = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");

        tvNomSelec = (TextView)findViewById(R.id.tv_nom_aler_sel);
        tvSinSelec= (TextView)findViewById(R.id.tv_sin_aler_sel);
        bEliminar = (Button)findViewById(R.id.b_eli_aler_sel);

        Integer id = getIntent().getIntExtra("alergiaSelec", -1);

        refAler.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Enfermedad a = data.getValue(Enfermedad.class);
                    listaAler.add(a);
                    for(Enfermedad aler: listaAler){
                        if(aler.getId().equals(id)){
                            tvNomSelec.setText(aler.getNombre());
                            tvSinSelec.setText(aler.getSintomas());
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
                AlertDialog.Builder alerta = new AlertDialog.Builder(DatosAlergiaActivity.this);
                alerta.setMessage("¿Está seguro de que desea eliminar esta alergia?");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refAler.child(String.valueOf(id)).removeValue();
                        Toast.makeText(DatosAlergiaActivity.this,"Esta alergia ha sido eliminada", Toast.LENGTH_SHORT).show();
                    }
                });
                alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Eliminar Alergia");
                titulo.show();
            }
        });
    }

    //Método botón Modificar
    public void accModificarAlergiaSelec(View view){
        Integer id = getIntent().getIntExtra("alergiaSelec", -1);
        Intent modificaAler = new Intent(DatosAlergiaActivity.this, CambiaAlergiaActivity.class);
        modificaAler.putExtra("alergiaSel", id);
        startActivity(modificaAler);

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
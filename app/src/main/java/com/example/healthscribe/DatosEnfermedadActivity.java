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

public class DatosEnfermedadActivity extends AppCompatActivity {

    private TextView tvNomSelec, tvSinSelec;

    private Button bEliminar;

    private DatabaseReference refEnf;

    final ArrayList<Enfermedad> listaEnf = new ArrayList<Enfermedad>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_enfermedad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refEnf = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");

        tvNomSelec = (TextView)findViewById(R.id.tv_nom_enf_sel);
        tvSinSelec = (TextView)findViewById(R.id.tv_sin_enf_sel);
        bEliminar = (Button)findViewById(R.id.b_eli_enf_sel);

        Integer id = getIntent().getIntExtra("enfermedadSelec", -1);

        refEnf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Enfermedad e = data.getValue(Enfermedad.class);
                    listaEnf.add(e);
                    for(Enfermedad enf : listaEnf){
                        if(enf.getId().equals(id)){
                            tvNomSelec.setText(enf.getNombre());
                            tvSinSelec.setText(enf.getSintomas());
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
                AlertDialog.Builder alerta = new AlertDialog.Builder(DatosEnfermedadActivity.this);
                alerta.setMessage("¿Está seguro de que desea eliminar esta enfermedad?");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refEnf.child(String.valueOf(id)).removeValue();
                        Toast.makeText(DatosEnfermedadActivity.this, "Esta enfermedad ha sido eliminada", Toast.LENGTH_SHORT).show();


                    }
                });
                alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Eliminar enfermedad");
                titulo.show();
            }
        });
    }

    //método botón modificar
    public void accModificarEnfermedadSelec(View view){
        Integer id = getIntent().getIntExtra("enfermedadSelec", -1);
        Intent modificaEnf = new Intent(DatosEnfermedadActivity.this, CambiaEnfermedadActivity.class);
        modificaEnf.putExtra("enfermedadSel", id);
        startActivity(modificaEnf);

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
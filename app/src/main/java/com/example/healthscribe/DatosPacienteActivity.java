package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthscribe.Model.Paciente;
import com.example.healthscribe.Model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatosPacienteActivity extends AppCompatActivity {

    private TextView tvNombreSelec, tvApeSelec, tvDniSelec, tvEmailSelec, tvDirSelec, tvPhoneSelec;

    private DatabaseReference refPac;

    final ArrayList<Paciente> listaPac = new ArrayList<Paciente>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_paciente);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refPac= FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");

        tvNombreSelec = (TextView)findViewById(R.id.tv_nom_pac_sel);
        tvApeSelec = (TextView)findViewById(R.id.tv_ape_pac_sel);
        tvDniSelec = (TextView)findViewById(R.id.tv_dni_pac_sel);
        tvEmailSelec = (TextView)findViewById(R.id.tv_ema_pac_sel);
        tvDirSelec = (TextView)findViewById(R.id.tv_dir_pac_sel);
        tvPhoneSelec = (TextView)findViewById(R.id.tv_telef_pac_sel);

        Integer id = getIntent().getIntExtra("PacienteSelec",-1);

        refPac.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Paciente p = data.getValue(Paciente.class);
                    listaPac.add(p);
                    for(Paciente pac: listaPac) {
                        if (pac.getId().equals(id)) {
                            tvNombreSelec.setText(pac.getUsuario().getNombre());
                            tvApeSelec.setText(pac.getUsuario().getApellidos());
                            tvDniSelec.setText(pac.getUsuario().getDni());
                            tvEmailSelec.setText(pac.getUsuario().getEmail());
                            tvDirSelec.setText(pac.getUsuario().getDireccion());
                            tvPhoneSelec.setText(pac.getUsuario().getTelefono());
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

    //Método botón Siguiente
    public void opcionesPaciente(View view){
        Integer id = getIntent().getIntExtra("PacienteSelec",-1);
        Intent opcPac = new Intent(DatosPacienteActivity.this, OpcionesPacienteActivity.class);
        opcPac.putExtra("PacienteSel", id);
        startActivity(opcPac);

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
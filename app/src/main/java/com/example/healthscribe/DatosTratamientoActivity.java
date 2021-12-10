package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.healthscribe.Model.Tratamiento;
import com.google.firebase.database.DatabaseReference;

public class DatosTratamientoActivity extends AppCompatActivity {

    private TextView tvFechaIni, tvFechaFin, tvMotivo, tvMedic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_tratamiento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvFechaIni = findViewById(R.id.tv_fecha_inicio);
        tvFechaFin = findViewById(R.id.tv_fecha_fin);
        tvMedic = findViewById(R.id.tv_trat_medicamentos);
        tvMotivo = findViewById(R.id.tv_motivo_trat);

        Tratamiento t = (Tratamiento) getIntent().getSerializableExtra("tratSelec");
        tvFechaIni.setText(t.getFechaInicio());
        tvFechaFin.setText(t.getFechaFin());
        tvMotivo.setText(t.getMotivo());
        tvMedic.setText(t.getMedicamento().getNombre());


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
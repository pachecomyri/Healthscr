package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.healthscribe.Model.Medicamento;
import com.example.healthscribe.Model.Paciente;
import com.example.healthscribe.Model.Tratamiento;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreacionTratamientoActivity extends AppCompatActivity {

    private Button bRecetar;
    private TextView etFechaIni, etFechaFin, etMotivo;
    private DatabaseReference refMed,refPac, refTrat;
    private ListView listMed;
    private ChildEventListener medicamListener;

    Calendar calendario = Calendar.getInstance();

    final List<Medicamento> listaMedicam = new ArrayList<Medicamento>();
    final List<Medicamento> listaSelMedicam = new ArrayList<Medicamento>();
    final List<Paciente> listaPac = new ArrayList<Paciente>();
    final List<Tratamiento> listaT = new ArrayList<Tratamiento>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_tratamiento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refMed = FirebaseDatabase.getInstance().getReference("Tablas").child("Medicamento");
        refPac = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");
        refTrat = FirebaseDatabase.getInstance().getReference("Tablas").child("Tratamiento");


        listMed = findViewById(R.id.lv_trat);
        bRecetar = findViewById(R.id.b_trat_unico);
        etFechaIni = findViewById(R.id.txt_fecha_inicio_trat);
        etFechaFin = findViewById(R.id.txt_fecha_fin_trat);
        etMotivo= findViewById(R.id.txt_motivo_trat);

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

        refMed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Medicamento m = data.getValue(Medicamento.class);
                    listaMedicam.add(m);
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

        final ArrayList<Medicamento> lista = new ArrayList<>();
        ArrayAdapter<Medicamento> adapter = new ArrayAdapter<Medicamento>(this, android.R.layout.simple_list_item_multiple_choice, lista);
        listMed.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listMed.setAdapter(adapter);


        DatePickerDialog.OnDateSetListener datePD = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendario.set(Calendar.YEAR, year);
                calendario.set(Calendar.MONTH, month);
                calendario.set(Calendar.DAY_OF_MONTH, day);
                actualizarInput();
            }
        };

        etFechaIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date = new DatePickerDialog(CreacionTratamientoActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, datePD, calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),calendario.get(Calendar.DAY_OF_MONTH));
                date.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                date.show();

            }
        });

        DatePickerDialog.OnDateSetListener datePF = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendario.set(Calendar.YEAR, year);
                calendario.set(Calendar.MONTH, month);
                calendario.set(Calendar.DAY_OF_MONTH, day);
                inputActualizar();
            }
        };

        etFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date = new DatePickerDialog(CreacionTratamientoActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, datePF, calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),calendario.get(Calendar.DAY_OF_MONTH));
                date.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                date.show();

            }
        });


        bRecetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray sparseBooleanArray = listMed.getCheckedItemPositions();

                for(int i = 0; i < sparseBooleanArray.size(); i++){
                    Medicamento m = (Medicamento) listMed.getItemAtPosition(sparseBooleanArray.keyAt(i));
                    listaSelMedicam.add(m);
                    if(sparseBooleanArray.valueAt(i)) {
                        if(listaT.isEmpty()){
                            Integer id = getIntent().getIntExtra("PacienteR",-1);
                            Integer idTrat = listaT.size()+1;
                            String fechaIni = etFechaIni.getText().toString();
                            String fechaF = etFechaFin.getText().toString();
                            String motivo = etMotivo.getText().toString();
                            Tratamiento trat = new Tratamiento(idTrat, m, motivo, fechaIni, fechaF);
                            refTrat.child(String.valueOf(idTrat)).setValue(trat);
                            Intent inte = new Intent(CreacionTratamientoActivity.this, AsignacionTratamientoActivity.class);
                            inte.putExtra("idPacienteReceta", id);
                            inte.putExtra("TratamientoRec",trat);
                            startActivity(inte);

                        }else{
                            Integer id = getIntent().getIntExtra("PacienteR",-1);
                            Tratamiento tr = listaT.get(listaT.size()-1);
                            Integer idTr =tr.getId()+1;
                            String fechaIni = etFechaIni.getText().toString();
                            String fechaF = etFechaFin.getText().toString();
                            String motivo = etMotivo.getText().toString();
                           Tratamiento tra = new Tratamiento(idTr, m, motivo, fechaIni, fechaF);
                            refTrat.child(String.valueOf(idTr)).setValue(tra);
                            Intent intent = new Intent(CreacionTratamientoActivity.this, AsignacionTratamientoActivity.class);
                            intent.putExtra("idPacienteReceta", id);
                            intent.putExtra("TratamientoRec", tra);
                            startActivity(intent);


                        }




                    }

                }

            }
        });

        medicamListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Medicamento m = snapshot.getValue(Medicamento.class);
                adapter.add(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        refMed.addChildEventListener(medicamListener);


    }

    private void actualizarInput(){
        String formatoFecha = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha, Locale.US);

        etFechaIni.setText(sdf.format(calendario.getTime()));
    }

    private void inputActualizar(){
        String formatoFecha = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha, Locale.US);

        etFechaFin.setText(sdf.format(calendario.getTime()));
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
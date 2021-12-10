package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.healthscribe.Model.Enfermedad;
import com.example.healthscribe.Model.HistoriaClinica;
import com.example.healthscribe.Model.Medicamento;
import com.example.healthscribe.Model.Paciente;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaMedicamHistoriaActivity extends AppCompatActivity {

    private DatabaseReference refMedicam, refPac, refHis;
    private ListView listMedicam;
    private Button bListMedicam;
    private ChildEventListener medicamListener;

    final List<Medicamento> listaMedic= new ArrayList<Medicamento>();
    final List<Medicamento> listaSelMedic= new ArrayList<Medicamento>();
    final List<Paciente> listaPac = new ArrayList<Paciente>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_medicam_historia);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refMedicam = FirebaseDatabase.getInstance().getReference("Tablas").child("Medicamento");
        refPac = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");
        refHis = FirebaseDatabase.getInstance().getReference("Tablas").child("HistoriaClinica");

        listMedicam = (ListView)findViewById(R.id.lv_hist_medicam);
        bListMedicam = (Button)findViewById(R.id.b_camb_his_medicam);

        refMedicam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Medicamento m= data.getValue(Medicamento.class);
                    listaMedic.add(m);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

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

        final ArrayList<Medicamento> lista = new ArrayList<>();
        ArrayAdapter<Medicamento> adapter = new ArrayAdapter<Medicamento>(this, android.R.layout.simple_list_item_multiple_choice, lista);
        listMedicam.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listMedicam.setAdapter(adapter);

        bListMedicam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = getIntent().getIntExtra("PacienteHisto",-1);
                SparseBooleanArray sparseBooleanArray = listMedicam.getCheckedItemPositions();

                for(int i = 0; i < sparseBooleanArray.size(); i++) {
                    if (sparseBooleanArray.valueAt(i)) {
                        Medicamento m= (Medicamento) listMedicam.getItemAtPosition(sparseBooleanArray.keyAt(i));
                        listaSelMedic.add(m);
                        for(Paciente pac:listaPac){
                            if(pac.getId().equals(id)){
                                HistoriaClinica h= pac.getHistoriaClinica();
                                Integer idH = h.getId();
                                h.setMedicamentos(listaSelMedic);
                                pac.setHistoriaClinica(h);
                                refPac.child(String.valueOf(id)).setValue(pac);
                                refHis.child(String.valueOf(idH)).setValue(h);
                                Toast.makeText(ListaMedicamHistoriaActivity.this,"Se ha modificado la lista de medicamentos", Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                }
            }
        });

        medicamListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Medicamento m= snapshot.getValue(Medicamento.class);
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

        refMedicam.addChildEventListener(medicamListener);


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
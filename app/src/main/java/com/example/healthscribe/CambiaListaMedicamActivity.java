package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.healthscribe.Model.Incompatibilidad;
import com.example.healthscribe.Model.Medicamento;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CambiaListaMedicamActivity extends AppCompatActivity {

    private DatabaseReference refMed, refInc;
    private ListView listMed;
    private Button bListMedicam;
    private ChildEventListener medicamListener;

    final List<Medicamento> listaMedicam = new ArrayList<Medicamento>();
    final List<Medicamento> listaSelMedicam = new ArrayList<Medicamento>();
    final ArrayList<Incompatibilidad> listaInc = new ArrayList<Incompatibilidad>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_lista_medicam);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refInc = FirebaseDatabase.getInstance().getReference("Tablas").child("Incompatibilidad");

        refMed = FirebaseDatabase.getInstance().getReference("Tablas").child("Medicamento");

        listMed = (ListView)findViewById(R.id.list_cambia_medicam);
        bListMedicam = (Button)findViewById(R.id.b_camb_list_medic);

        refInc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Incompatibilidad inc = data.getValue(Incompatibilidad.class);
                    listaInc.add(inc);
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
                    Medicamento m= data.getValue(Medicamento.class);
                    listaMedicam.add(m);
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

        bListMedicam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = getIntent().getIntExtra("incompatibilidadS", -1);
                SparseBooleanArray sparseBooleanArray = listMed.getCheckedItemPositions();

                for(int i = 0; i < sparseBooleanArray.size(); i++) {
                    if (sparseBooleanArray.valueAt(i)) {
                        Medicamento m = (Medicamento) listMed.getItemAtPosition(sparseBooleanArray.keyAt(i));
                        listaSelMedicam.add(m);
                       for(Incompatibilidad incomp: listaInc){
                           if(incomp.getId().equals(id)){
                               incomp.setListaMedicamentos(listaSelMedicam);
                               refInc.child(String.valueOf(id)).setValue(incomp);
                               Toast.makeText(CambiaListaMedicamActivity.this, "Se ha modificado la lista de medicamentos", Toast.LENGTH_SHORT).show();
                           }
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
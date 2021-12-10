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

import com.example.healthscribe.Model.Enfermedad;
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

public class ListaEnferIncompActivity extends AppCompatActivity {

    private DatabaseReference refEnf;
    private ListView listEnf;
    private Button bListEnf, bSiguiente;
    private ChildEventListener enfListener;

    final List<Enfermedad> listaEnferm= new ArrayList<Enfermedad>();
    final List<Enfermedad> listaSelEnferm = new ArrayList<Enfermedad>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_enfer_incomp);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refEnf = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");

        listEnf = (ListView)findViewById(R.id.list_inc_enferm);
        bListEnf = (Button)findViewById(R.id.b_lista_enferm);
        bSiguiente = (Button)findViewById(R.id.b_sig_enf);


        refEnf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Enfermedad e= data.getValue(Enfermedad.class);
                    listaEnferm.add(e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        final ArrayList<Enfermedad> lista = new ArrayList<>();
        ArrayAdapter<Enfermedad> adapter = new ArrayAdapter<Enfermedad>(this, android.R.layout.simple_list_item_multiple_choice, lista);
        listEnf.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listEnf.setAdapter(adapter);

        bListEnf.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray sparseBooleanArray = listEnf.getCheckedItemPositions();

                for(int i = 0; i < sparseBooleanArray.size(); i++){

                    if(sparseBooleanArray.valueAt(i)) {
                        Medicamento medic = (Medicamento) getIntent().getSerializableExtra("MedicamentoInc1");
                        List<Medicamento> medicamentos = (List<Medicamento>) getIntent().getSerializableExtra("ListaMedicamentos");
                        Enfermedad enf = (Enfermedad) listEnf.getItemAtPosition(sparseBooleanArray.keyAt(i));
                        listaSelEnferm.add(enf);
                        Intent intent = new Intent(ListaEnferIncompActivity.this, ListaAlergIncompActivity.class);
                        intent.putExtra("ListaEnfermedades",(Serializable) listaSelEnferm);
                        intent.putExtra("ListaMedicamentos1", (Serializable) medicamentos);
                        intent.putExtra("MedicamentoInc2", medic);
                        startActivity(intent);
                        finish();

                    }

                }

            }
        });

        bSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medicamento medic = (Medicamento) getIntent().getSerializableExtra("MedicamentoInc1");
                List<Medicamento> medicamentos = (List<Medicamento>) getIntent().getSerializableExtra("ListaMedicamentos");
                Intent intent = new Intent(ListaEnferIncompActivity.this, ListaAlergIncompActivity.class);
                intent.putExtra("ListaMedicamentos1", (Serializable) medicamentos);
                intent.putExtra("MedicamentoInc2", medic);
                startActivity(intent);
            }
        });

        enfListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Enfermedad e = snapshot.getValue(Enfermedad.class);
                if (e.getTipo().equals(0)) {
                    adapter.add(e);
                }
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

        refEnf.addChildEventListener(enfListener);


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
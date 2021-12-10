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

public class ListaAlergIncompActivity extends AppCompatActivity {

    private DatabaseReference refAler;
    private ListView listAler;
    private Button bListAler, bSiguiente;
    private ChildEventListener alerListener;

    final List<Enfermedad> listaAler= new ArrayList<Enfermedad>();
    final List<Enfermedad> listaSelAler = new ArrayList<Enfermedad>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alerg_incomp);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refAler = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");

        listAler = (ListView)findViewById(R.id.list_inc_alerg);
        bListAler = (Button)findViewById(R.id.b_lista_alerg);
        bSiguiente = (Button)findViewById(R.id.b_sig_alerg);

        refAler.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Enfermedad a= data.getValue(Enfermedad.class);
                    listaAler.add(a);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        final ArrayList<Enfermedad> lista = new ArrayList<>();
        ArrayAdapter<Enfermedad> adapter = new ArrayAdapter<Enfermedad>(this, android.R.layout.simple_list_item_multiple_choice, lista);
        listAler.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listAler.setAdapter(adapter);

        bListAler.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray sparseBooleanArray = listAler.getCheckedItemPositions();

                for(int i = 0; i < sparseBooleanArray.size(); i++){

                    if(sparseBooleanArray.valueAt(i)) {
                        Medicamento medic = (Medicamento) getIntent().getSerializableExtra("MedicamentoInc2");
                        List<Medicamento> medicamentos = (List<Medicamento>) getIntent().getSerializableExtra("ListaMedicamentos1");
                        List<Enfermedad> enfermedades = (List<Enfermedad>) getIntent().getSerializableExtra("ListaEnfermedades");
                        Enfermedad aler = (Enfermedad) listAler.getItemAtPosition(sparseBooleanArray.keyAt(i));
                        listaSelAler.add(aler);
                        Intent intent = new Intent(ListaAlergIncompActivity.this, CrearIncompatibilidadActivity.class);
                        intent.putExtra("ListaAlergias", (Serializable) listaSelAler);
                        intent.putExtra("MedicamentoInc3", medic);
                        intent.putExtra("ListaMedicamentos2", (Serializable) medicamentos);
                        intent.putExtra("ListaEnfermedades1", (Serializable) enfermedades);
                        startActivity(intent);
                        finish();

                    }

                }

            }
        });

        bSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medicamento medic = (Medicamento) getIntent().getSerializableExtra("MedicamentoInc2");
                List<Medicamento> medicamentos = (List<Medicamento>) getIntent().getSerializableExtra("ListaMedicamentos1");
                List<Enfermedad> enfermedades = (List<Enfermedad>) getIntent().getSerializableExtra("ListaEnfermedades");
                Intent intent = new Intent(ListaAlergIncompActivity.this, CrearIncompatibilidadActivity.class);
                intent.putExtra("MedicamentoInc3", medic);
                intent.putExtra("ListaMedicamentos2", (Serializable) medicamentos);
                intent.putExtra("ListaEnfermedades1", (Serializable) enfermedades);
                startActivity(intent);
            }
        });

        alerListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Enfermedad a = snapshot.getValue(Enfermedad.class);
                if (a.getTipo().equals(1)) {
                    adapter.add(a);
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

        refAler.addChildEventListener(alerListener);
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
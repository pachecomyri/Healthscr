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
import com.example.healthscribe.Model.Incompatibilidad;
import com.example.healthscribe.Model.Paciente;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaEnfermHistActivity extends AppCompatActivity {

    private DatabaseReference refEnf, refPac, refHis;
    private ListView listEnf;
    private Button bListEnf;
    private ChildEventListener enfListener;

    final List<Enfermedad> listaEnferm= new ArrayList<Enfermedad>();
    final List<Enfermedad> listaSelEnferm = new ArrayList<Enfermedad>();
    final List<Paciente> listaPac = new ArrayList<Paciente>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_enferm_hist);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refEnf = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");
        refPac = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");
        refHis = FirebaseDatabase.getInstance().getReference("Tablas").child("HistoriaClinica");

        listEnf = (ListView)findViewById(R.id.lv_hist_enferm);
        bListEnf = (Button)findViewById(R.id.b_camb_his_enfer);

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

        final ArrayList<Enfermedad> lista = new ArrayList<>();
        ArrayAdapter<Enfermedad> adapter = new ArrayAdapter<Enfermedad>(this, android.R.layout.simple_list_item_multiple_choice, lista);
        listEnf.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listEnf.setAdapter(adapter);

        bListEnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = getIntent().getIntExtra("PacienteHisto",-1);
                SparseBooleanArray sparseBooleanArray = listEnf.getCheckedItemPositions();

                for(int i = 0; i < sparseBooleanArray.size(); i++) {
                    if (sparseBooleanArray.valueAt(i)) {
                        Enfermedad e = (Enfermedad) listEnf.getItemAtPosition(sparseBooleanArray.keyAt(i));
                        listaSelEnferm.add(e);
                        for(Paciente pac:listaPac){
                            if(pac.getId().equals(id)){
                                HistoriaClinica h= pac.getHistoriaClinica();
                                Integer idH = h.getId();
                                h.setEnfermedades(listaSelEnferm);
                                pac.setHistoriaClinica(h);
                                refPac.child(String.valueOf(id)).setValue(pac);
                                refHis.child(String.valueOf(idH)).setValue(h);
                                Toast.makeText(ListaEnfermHistActivity.this,"Se ha modificado la lista de enfermedades", Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                }

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
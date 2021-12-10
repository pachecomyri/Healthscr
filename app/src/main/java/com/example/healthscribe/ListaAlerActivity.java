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

public class ListaAlerActivity extends AppCompatActivity {

    private DatabaseReference refAler, refPac, refHis;
    private ListView listAler;
    private Button bListAler;
    private ChildEventListener alerListener;

    final List<Enfermedad> listaAler= new ArrayList<Enfermedad>();
    final List<Enfermedad> listaSelAler = new ArrayList<Enfermedad>();
    final ArrayList<Paciente> listaPac = new ArrayList<Paciente>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_aler);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refAler = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");
        refPac = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");
        refHis = FirebaseDatabase.getInstance().getReference("Tablas").child("HistoriaClinica");

        listAler = (ListView)findViewById(R.id.lv_hist_aler);
        bListAler = (Button)findViewById(R.id.b_camb_his_aler);

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
        listAler.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listAler.setAdapter(adapter);

        bListAler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = getIntent().getIntExtra("PacienteHisto",-1);
                SparseBooleanArray sparseBooleanArray = listAler.getCheckedItemPositions();

                for(int i = 0; i < sparseBooleanArray.size(); i++) {
                    if (sparseBooleanArray.valueAt(i)) {
                        Enfermedad a = (Enfermedad) listAler.getItemAtPosition(sparseBooleanArray.keyAt(i));
                        listaSelAler.add(a);
                        for(Paciente pac:listaPac){
                            if(pac.getId().equals(id)){
                                HistoriaClinica h= pac.getHistoriaClinica();
                                Integer idH = h.getId();
                                h.setAlergias(listaSelAler);
                                pac.setHistoriaClinica(h);
                                refPac.child(String.valueOf(id)).setValue(pac);
                                refHis.child(String.valueOf(idH)).setValue(h);
                                Toast.makeText(ListaAlerActivity.this,"Se ha modificado la lista de alergias", Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                }

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
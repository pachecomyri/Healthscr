package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.healthscribe.Model.HistoriaClinica;
import com.example.healthscribe.Model.Paciente;
import com.example.healthscribe.Model.Tratamiento;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TratamientoActivity extends AppCompatActivity {

    private ListView listPacTrat;
    private ArrayAdapter<Tratamiento> tratAdapter;
    private DatabaseReference refPac, refTrat, refHis;
    private ChildEventListener tratListener;

    final List<Paciente> listaPac = new ArrayList<Paciente>();
    final List<Tratamiento> listaTratamiento = new ArrayList<Tratamiento>();
    final List<HistoriaClinica> listaHistoria = new ArrayList<HistoriaClinica>();
    List<Tratamiento> listaTrata= new ArrayList<Tratamiento>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tratamiento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refPac = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");
        refTrat = FirebaseDatabase.getInstance().getReference("Tablas").child("Tratamiento");
        refHis = FirebaseDatabase.getInstance().getReference("Tablas").child("HistoriaClinica");

        listPacTrat = findViewById(R.id.lv_pac_trat);

        refHis.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    HistoriaClinica his = data.getValue(HistoriaClinica.class);
                    listaHistoria.add(his);

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
                    Tratamiento tr = data.getValue(Tratamiento.class);
                    Integer id= getIntent().getIntExtra("idPacHis",-1);

                    for(HistoriaClinica h: listaHistoria){
                        if(h.getId().equals(id)){
                            for(Tratamiento t: h.getTratamientos()){
                                if(t.getId().equals(tr.getId())){
                                    listaTrata.add(tr);
                                }
                            }
                        }

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        final ArrayList<Tratamiento> listaT = new ArrayList<>();
        ArrayAdapter<Tratamiento> adapter = new ArrayAdapter<Tratamiento>(this, android.R.layout.simple_list_item_1, listaTrata);
        listPacTrat.setAdapter(adapter);

       listPacTrat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TratamientoActivity.this, listaTrata.get(position).getMedicamento().getNombre(), Toast.LENGTH_SHORT).show();
                Intent accDatos = new Intent(TratamientoActivity.this, DatosTratamientoActivity.class);
                accDatos.putExtra("tratSelec", listaTrata.get(position));
                startActivity(accDatos);

            }
        });

      tratListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Integer id= getIntent().getIntExtra("idPacHis",-1);
                for(HistoriaClinica h: listaHistoria){
                    if(h.getId().equals(id)){
                        for(Tratamiento t: h.getTratamientos()){
                            adapter.add(t);
                        }

                    }
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

        refTrat.addChildEventListener(tratListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_busqueda, menu);
        MenuItem item = menu.findItem(R.id.menuBusqueda);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tratAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
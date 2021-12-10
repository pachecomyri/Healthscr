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

import com.example.healthscribe.Model.Paciente;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PacienteActivity extends AppCompatActivity {

    private ListView listPaci;
    private ArrayAdapter<Paciente> pacAdapter;
    private DatabaseReference refPac;
    private ChildEventListener pacListener;

    final ArrayList<Paciente> listaPac = new ArrayList<Paciente>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refPac = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");

        listPaci = (ListView)findViewById(R.id.lv_paciente);

        refPac.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Paciente pac = data.getValue(Paciente.class);
                    listaPac.add(pac);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        final ArrayList<Paciente> lista = new ArrayList<>();
        pacAdapter = new ArrayAdapter<Paciente>(PacienteActivity.this, android.R.layout.simple_list_item_1, lista);
        listPaci.setAdapter(pacAdapter);

        listPaci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PacienteActivity.this,lista.get(position).getUsuario().getNombre(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PacienteActivity.this, DatosPacienteActivity.class);
                intent.putExtra("PacienteSelec", lista.get(position).getId());
                startActivity(intent);
            }
        });

        pacListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Paciente p = snapshot.getValue(Paciente.class);
                pacAdapter.add(p);
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

        refPac.addChildEventListener(pacListener);
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
                pacAdapter.getFilter().filter(newText);
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
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

import com.example.healthscribe.Model.Enfermedad;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EnfermedadActivity extends AppCompatActivity {

    private ListView listEnfermedad;
    private ArrayAdapter<Enfermedad> enfermedadAdapter;
    private DatabaseReference refEnf;
    private ChildEventListener enfListener;
    final ArrayList<Enfermedad> listaEnf = new ArrayList<Enfermedad>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enfermedad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refEnf = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");

        listEnfermedad = (ListView)findViewById(R.id.lv_enfermedad);

        refEnf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    Enfermedad e= data.getValue(Enfermedad.class);
                    listaEnf.add(e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        final ArrayList<Enfermedad> lista = new ArrayList<>();
        enfermedadAdapter = new ArrayAdapter<Enfermedad>(EnfermedadActivity.this, android.R.layout.simple_list_item_1, lista);
        listEnfermedad.setAdapter(enfermedadAdapter);

        listEnfermedad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(EnfermedadActivity.this, lista.get(position).getNombre(), Toast.LENGTH_SHORT).show();
                Intent accEnfermedad = new Intent(EnfermedadActivity.this, DatosEnfermedadActivity.class);
                accEnfermedad.putExtra("enfermedadSelec", lista.get(position).getId());
                startActivity(accEnfermedad);
            }
        });

        enfListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Enfermedad e = snapshot.getValue(Enfermedad.class);
                if (e.getTipo().equals(0)) {
                    enfermedadAdapter.add(e);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_busqueda, menu);
        MenuItem menuItem = menu.findItem(R.id.menuBusqueda);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                enfermedadAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //Método botón Introducir
    public void accesoCrearEnfermedad(View view){
        Intent crearEnferm = new Intent(EnfermedadActivity.this,CrearEnfermedadActivity.class);
        startActivity(crearEnferm);
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
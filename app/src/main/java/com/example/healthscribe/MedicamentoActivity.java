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

import com.example.healthscribe.Model.Medicamento;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MedicamentoActivity extends AppCompatActivity {

    private ListView listMedicam;
    private ArrayAdapter<Medicamento> medicamAdapter;
    private DatabaseReference refMedicam;
    private ChildEventListener medicamentoListener;

    final ArrayList<Medicamento> listaMed =new ArrayList<Medicamento>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refMedicam = FirebaseDatabase.getInstance().getReference("Tablas").child("Medicamento");

        listMedicam = (ListView)findViewById(R.id.lv_medicamento);

        refMedicam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Medicamento m = data.getValue(Medicamento.class);
                    listaMed.add(m);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });
        final ArrayList<Medicamento> listaM = new ArrayList<>();
        medicamAdapter = new ArrayAdapter<Medicamento>(MedicamentoActivity.this, android.R.layout.simple_list_item_1, listaM);
        listMedicam.setAdapter(medicamAdapter);

        listMedicam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MedicamentoActivity.this, listaM.get(position).getNombre(), Toast.LENGTH_SHORT).show();
                Intent accMedicamento = new Intent(MedicamentoActivity.this, DatosMedicamentoActivity.class);
                accMedicamento.putExtra("medicamentoSelec", listaM.get(position).getId());
                startActivity(accMedicamento);
            }
        });

        medicamentoListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Medicamento m = snapshot.getValue(Medicamento.class);
                medicamAdapter.add(m);
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

        refMedicam.addChildEventListener(medicamentoListener);

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
                medicamAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //Método botón Crear
    public void accesoCrearMedicamento(View view){
        Intent crearMedicam = new Intent(MedicamentoActivity.this,CrearMedicamentoActivity.class);
        startActivity(crearMedicam);
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
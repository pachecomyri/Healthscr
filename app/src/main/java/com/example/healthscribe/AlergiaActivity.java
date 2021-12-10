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

public class AlergiaActivity extends AppCompatActivity {

    private ListView listAlergia;
    private ArrayAdapter<Enfermedad> alergiaAdapter;
    private DatabaseReference refAler;
    private ChildEventListener alerListener;

    final ArrayList<Enfermedad> listaAler = new ArrayList<Enfermedad>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alergia);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refAler = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");

        listAlergia = (ListView)findViewById(R.id.lv_alergia);

        refAler.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Enfermedad a = data.getValue(Enfermedad.class);
                    listaAler.add(a);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        final ArrayList<Enfermedad> listaA = new ArrayList<>();
        alergiaAdapter = new ArrayAdapter<Enfermedad>(AlergiaActivity.this, android.R.layout.simple_list_item_1, listaA);
        listAlergia.setAdapter(alergiaAdapter);

        listAlergia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AlergiaActivity.this, listaA.get(position).getNombre(), Toast.LENGTH_SHORT).show();
                Intent accAlergia = new Intent(AlergiaActivity.this, DatosAlergiaActivity.class);
                accAlergia.putExtra("alergiaSelec", listaA.get(position).getId());
                startActivity(accAlergia);
            }
        });

        alerListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Enfermedad a =snapshot.getValue(Enfermedad.class);
                if (a.getTipo().equals(1)) {
                    alergiaAdapter.add(a);
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

    //Método botón introducir
    public void accesoCrearAlergia(View view){
        Intent crearAler = new Intent(AlergiaActivity.this, CrearAlergiaActivity.class);
        startActivity(crearAler);
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
                alergiaAdapter.getFilter().filter(newText);
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
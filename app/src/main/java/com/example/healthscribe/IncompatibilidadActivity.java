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

import com.example.healthscribe.Model.Incompatibilidad;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IncompatibilidadActivity extends AppCompatActivity {

    private ListView listIncom;
    private ArrayAdapter<Incompatibilidad> incomAdapter;
    private DatabaseReference refInc;
    private ChildEventListener incomListener;

    final ArrayList<Incompatibilidad> listaInc = new ArrayList<Incompatibilidad>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incompatibilidad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refInc = FirebaseDatabase.getInstance().getReference("Tablas").child("Incompatibilidad");

        listIncom = (ListView)findViewById(R.id.lv_incompatibilidad);

        refInc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Incompatibilidad in = data.getValue(Incompatibilidad.class);
                    listaInc.add(in);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        final ArrayList<Incompatibilidad> listaI = new ArrayList<>();
        incomAdapter = new ArrayAdapter<Incompatibilidad>(IncompatibilidadActivity.this, android.R.layout.simple_list_item_1, listaI);
        listIncom.setAdapter(incomAdapter);

        listIncom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(IncompatibilidadActivity.this, String.valueOf(listaI.get(position).getMedicamento()), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(IncompatibilidadActivity.this, DatosIncompatibilidadActivity.class);
                intent.putExtra("incompatibilidadSelec", listaI.get(position).getId());
                startActivity(intent);

            }
        });

        incomListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Incompatibilidad inc = snapshot.getValue(Incompatibilidad.class);
                incomAdapter.add(inc);

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

        refInc.addChildEventListener(incomListener);


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
                incomAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //Método botón Crear
    public void accesoCrearIncompatibilidad(View view){
        Intent crearIncomp = new Intent(IncompatibilidadActivity.this, MedicamentoSpinnerActivity.class);
        startActivity(crearIncomp);

    }


}
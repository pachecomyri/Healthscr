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
import com.example.healthscribe.Model.Incompatibilidad;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CambiaListaAlerActivity extends AppCompatActivity {

    private DatabaseReference refAler, refInc;
    private ListView listAler;
    private Button bListAler;
    private ChildEventListener alerListener;

    final List<Enfermedad> listaAler= new ArrayList<Enfermedad>();
    final List<Enfermedad> listaSelAler = new ArrayList<Enfermedad>();
    final ArrayList<Incompatibilidad> listaInc = new ArrayList<Incompatibilidad>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_lista_aler);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refInc = FirebaseDatabase.getInstance().getReference("Tablas").child("Incompatibilidad");

        refAler = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");

        listAler = (ListView)findViewById(R.id.list_cambia_aler);
        bListAler = (Button)findViewById(R.id.b_camb_list_aler);

        refInc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Incompatibilidad inc = data.getValue(Incompatibilidad.class);
                    listaInc.add(inc);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

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

        bListAler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = getIntent().getIntExtra("incompatibilidadS", -1);
                SparseBooleanArray sparseBooleanArray = listAler.getCheckedItemPositions();

                for(int i = 0; i < sparseBooleanArray.size(); i++) {
                    if (sparseBooleanArray.valueAt(i)) {
                        Enfermedad a = (Enfermedad) listAler.getItemAtPosition(sparseBooleanArray.keyAt(i));
                        listaSelAler.add(a);
                        for(Incompatibilidad incomp: listaInc){
                            if(incomp.getId().equals(id)){
                                incomp.setListaAlergias(listaSelAler);
                                refInc.child(String.valueOf(id)).setValue(incomp);
                                Toast.makeText(CambiaListaAlerActivity.this, "Se ha modificado la lista de alergias", Toast.LENGTH_SHORT).show();
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
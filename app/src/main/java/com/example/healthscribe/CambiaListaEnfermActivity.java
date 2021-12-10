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

public class CambiaListaEnfermActivity extends AppCompatActivity {

    private DatabaseReference refEnf, refInc;
    private ListView listEnf;
    private Button bListEnf;
    private ChildEventListener enfListener;

    final List<Enfermedad> listaEnferm= new ArrayList<Enfermedad>();
    final List<Enfermedad> listaSelEnferm = new ArrayList<Enfermedad>();
    final ArrayList<Incompatibilidad> listaInc = new ArrayList<Incompatibilidad>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_lista_enferm);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refInc = FirebaseDatabase.getInstance().getReference("Tablas").child("Incompatibilidad");

        refEnf = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");

        listEnf = (ListView)findViewById(R.id.list_cambia_enferm);
        bListEnf = (Button)findViewById(R.id.b_camb_list_enf);

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

        final ArrayList<Enfermedad> lista = new ArrayList<>();
        ArrayAdapter<Enfermedad> adapter = new ArrayAdapter<Enfermedad>(this, android.R.layout.simple_list_item_multiple_choice, lista);
        listEnf.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listEnf.setAdapter(adapter);

        bListEnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = getIntent().getIntExtra("incompatibilidadS", -1);
                SparseBooleanArray sparseBooleanArray = listEnf.getCheckedItemPositions();

                for(int i = 0; i < sparseBooleanArray.size(); i++) {
                    if (sparseBooleanArray.valueAt(i)) {
                        Enfermedad e = (Enfermedad) listEnf.getItemAtPosition(sparseBooleanArray.keyAt(i));
                        listaSelEnferm.add(e);
                        for(Incompatibilidad incomp: listaInc){
                            if(incomp.getId().equals(id)){
                                incomp.setListaEnfermedades(listaSelEnferm);
                                refInc.child(String.valueOf(id)).setValue(incomp);
                                Toast.makeText(CambiaListaEnfermActivity.this, "Se ha modificado la lista de enfermedades", Toast.LENGTH_SHORT).show();
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
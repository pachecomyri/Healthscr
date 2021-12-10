package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthscribe.Model.Enfermedad;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CambiaAlergiaActivity extends AppCompatActivity {

    private EditText etNomSelec, etSintSelec;

    private DatabaseReference refAler;

    final ArrayList<Enfermedad> listaAler = new ArrayList<Enfermedad>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_alergia);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refAler = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");

        etNomSelec = (EditText)findViewById(R.id.txt_nom_aler_sel);
        etSintSelec = (EditText)findViewById(R.id.txt_sint_aler_sel);

        Integer id = getIntent().getIntExtra("alergiaSel", -1);

        refAler.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Enfermedad a = data.getValue(Enfermedad.class);
                    listaAler.add(a);
                    for(Enfermedad aler : listaAler){
                        if(aler.getId().equals(id)){
                            etNomSelec.setText(aler.getNombre());
                            etSintSelec.setText(aler.getSintomas());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });
    }

    public void modificarAlergiaSelec(View view){
        Integer id = getIntent().getIntExtra("alergiaSel", -1);
        String nomS = etNomSelec.getText().toString();
        String sintS = etSintSelec.getText().toString();

        for(Enfermedad aler: listaAler){
            if(aler.getId().equals(id)){
                aler.setNombre(nomS);
                aler.setSintomas(sintS);

                refAler.child(String.valueOf(id)).setValue(aler);

                Toast.makeText(CambiaAlergiaActivity.this, "La alergia "+aler.getNombre()+" ha sido modificada", Toast.LENGTH_LONG).show();
            }
        }

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
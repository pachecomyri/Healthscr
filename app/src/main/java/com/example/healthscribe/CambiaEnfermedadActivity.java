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

public class CambiaEnfermedadActivity extends AppCompatActivity {

    private EditText etNomSelec, etSintSelec;

    private DatabaseReference refEnf;

    final ArrayList<Enfermedad> listaEnf = new ArrayList<Enfermedad>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_enfermedad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refEnf = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");

        etNomSelec = (EditText)findViewById(R.id.txt_nom_enf_sel);
        etSintSelec = (EditText)findViewById(R.id.txt_sin_enf_sel);

        Integer id = getIntent().getIntExtra("enfermedadSel", -1);

        refEnf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Enfermedad e = data.getValue(Enfermedad.class);
                    listaEnf.add(e);
                    for(Enfermedad enf : listaEnf){
                        if(enf.getId().equals(id)){
                            etNomSelec.setText(enf.getNombre());
                            etSintSelec.setText(enf.getSintomas());

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
    //Método modificar
    public void modificarEnfermedadSelec(View view){
        Integer id = getIntent().getIntExtra("enfermedadSel", -1);
        String nomS = etNomSelec.getText().toString();
        String sintS = etSintSelec.getText().toString();

        for(Enfermedad enf : listaEnf){
            if(enf.getId().equals(id)){
                enf.setNombre(nomS);
                enf.setSintomas(sintS);

                refEnf.child(String.valueOf(id)).setValue(enf);

                Toast.makeText(CambiaEnfermedadActivity.this, "La enfermedad "+enf.getNombre()+" ha sido modificada", Toast.LENGTH_LONG).show();
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
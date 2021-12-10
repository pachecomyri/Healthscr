package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
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

public class CrearAlergiaActivity extends AppCompatActivity {

    private EditText etnombre, etsintomas;
    private DatabaseReference refAlergia;
    final ArrayList<Enfermedad> listAlergia = new ArrayList<Enfermedad>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_alergia);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refAlergia = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");

        etnombre = (EditText)findViewById(R.id.txt_nombre_alergia);
        etsintomas = (EditText)findViewById(R.id.txt_sintomas_alergia);

        refAlergia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Enfermedad a = data.getValue(Enfermedad.class);
                    listAlergia.add(a);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });
    }

    public void crearAlergia(View view){
        String nombre = etnombre.getText().toString();
        String sintomas = etsintomas.getText().toString();
        Integer tipo = 1;

        if(!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(sintomas)){
            if(!listAlergia.isEmpty()){
                for(Enfermedad e: listAlergia){
                    if(e.getNombre().equals(nombre)){
                        Toast.makeText(this, "La alergia " + nombre + " ya existe", Toast.LENGTH_LONG).show();
                    }else{
                        Enfermedad en = listAlergia.get(listAlergia.size()-1);
                        Integer id = en.getId()+1;
                        Enfermedad enf = new Enfermedad(id, nombre, tipo, sintomas);
                        refAlergia.child(String.valueOf(id)).setValue(enf);
                        Toast.makeText(this, "La alergia " + nombre + " ha sido registrada", Toast.LENGTH_LONG).show();
                        etnombre.setText("");
                        etsintomas.setText("");
                    }
                }

            }else{
                Integer id = listAlergia.size()+1;
                Enfermedad enf = new Enfermedad(id, nombre, tipo, sintomas);
                refAlergia.child(String.valueOf(id)).setValue(enf);
                Toast.makeText(this, "La alergia " + nombre + " ha sido registrada", Toast.LENGTH_LONG).show();
                etnombre.setText("");
                etsintomas.setText("");
            }

        }else{
            Toast.makeText(this, "Se deben rellenar todos los campos", Toast.LENGTH_LONG).show();
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